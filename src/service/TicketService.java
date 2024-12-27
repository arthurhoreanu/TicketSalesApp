package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Service class for managing ticket-related operations.
 */
public class TicketService {

    private final IRepository<Ticket> ticketRepository;
    private final FileRepository<Ticket> ticketFileRepository;
    private final SeatService seatService;

    private static final double VIP_PERCENTAGE_INCREASE = 50.0; // +50% față de EARLY_BIRD
    private static final int EARLY_BIRD_STOCK = 100; // Număr de bilete EARLY_BIRD
    private static final double LAST_MINUTE_DISCOUNT = -20.0; // -20% în ziua evenimentului

    /**
     * Constructs a TicketService with dependencies for managing tickets, seats, and events.
     *
     * @param ticketRepository the repository for ticket persistence.
     * @param seatService      the service for managing seat operations.
     */
    public TicketService(IRepository<Ticket> ticketRepository, SeatService seatService) {
        this.ticketRepository = ticketRepository;
        this.seatService = seatService;

        // Initialize file repository for CSV operations
        this.ticketFileRepository = new FileRepository<>("src/repository/data/tickets.csv", Ticket::fromCsv);
        syncFromCsv();
    }

    /**
     * Synchronizes tickets from the CSV file into the main repository.
     */
    private void syncFromCsv() {
        List<Ticket> tickets = ticketFileRepository.getAll();
        for (Ticket ticket : tickets) {
            ticketRepository.create(ticket);
        }
    }

    /**
     * Generates tickets for an event based on its available seats.
     *
     * @param event         the event for which tickets are generated.
     * @param basePrice     the base price for early bird tickets.
     * @return the list of generated tickets.
     */
    public List<Ticket> generateTicketsForEvent(Event event, double basePrice) {
        List<Seat> availableSeats = seatService.getAvailableSeatsInVenue(event.getVenueID());

        // Distribuirea tipurilor de bilete
        List<Ticket> earlyBirdTickets = generateEarlyBirdTickets(availableSeats, event, basePrice);
        List<Ticket> vipTickets = generateVipTickets(availableSeats, event, basePrice);
        List<Ticket> standardTickets = generateStandardTickets(availableSeats, event, basePrice);

        // Salvăm biletele generate
        List<Ticket> allTickets = new ArrayList<>();
        allTickets.addAll(earlyBirdTickets);
        allTickets.addAll(vipTickets);
        allTickets.addAll(standardTickets);

        for (Ticket ticket : allTickets) {
            ticketRepository.create(ticket);
            ticketFileRepository.create(ticket);
        }
        return allTickets;
    }

    /**
     * Generates EARLY_BIRD tickets for an event.
     */
    private List<Ticket> generateEarlyBirdTickets(List<Seat> availableSeats, Event event, double basePrice) {
        return availableSeats.stream()
                .limit(EARLY_BIRD_STOCK)
                .map(seat -> new Ticket(0, event, seat, null, basePrice, TicketType.EARLY_BIRD))
                .collect(Collectors.toList());
    }

    /**
     * Generates VIP tickets for an event.
     */
    private List<Ticket> generateVipTickets(List<Seat> availableSeats, Event event, double basePrice) {
        double vipPrice = basePrice * (1 + VIP_PERCENTAGE_INCREASE / 100);
        return availableSeats.stream()
                .filter(seat -> seat.getRow().getRowCapacity() <= 5) // VIP logic
                .map(seat -> new Ticket(0, event, seat, null, vipPrice, TicketType.VIP))
                .collect(Collectors.toList());
    }

    /**
     * Generates STANDARD tickets for an event with dynamic pricing.
     */
    private List<Ticket> generateStandardTickets(List<Seat> availableSeats, Event event, double basePrice) {
        LocalDateTime eventDate = event.getStartDateTime();
        double dynamicPrice = calculateDynamicStandardPrice(basePrice, eventDate);

        return availableSeats.stream()
                .skip(EARLY_BIRD_STOCK)
                .map(seat -> new Ticket(0, event, seat, null, dynamicPrice, TicketType.STANDARD))
                .collect(Collectors.toList());
    }

    /**
     * Calculates dynamic price for STANDARD tickets based on event proximity.
     */
    private double calculateDynamicStandardPrice(double basePrice, LocalDateTime eventDate) {
        LocalDateTime now = LocalDateTime.now();
        long daysToEvent = java.time.Duration.between(now, eventDate).toDays();

        if (daysToEvent > 30) {
            return basePrice * 1.1; // +10% dacă evenimentul e la mai mult de 30 de zile
        } else if (daysToEvent <= 30 && daysToEvent > 7) {
            return basePrice * 1.2; // +20% între 7 și 30 de zile
        } else if (daysToEvent <= 7 && daysToEvent > 1) {
            return basePrice * 1.5; // +50% cu o săptămână înainte
        } else {
            return basePrice * (1 + LAST_MINUTE_DISCOUNT / 100); // Reducere de ultim moment (-20%)
        }
    }

    /**
     * Reserves a ticket for a specific customer.
     *
     * @param ticket   the ticket to reserve.
     * @param customer the customer reserving the ticket.
     */
    public void reserveTicket(Ticket ticket, Customer customer) {
        ticket.setSold(true);
        ticket.setCustomer(customer);
        ticket.setPurchaseDate(LocalDateTime.now());

        seatService.reserveSeat(ticket.getSeat().getID(), ticket.getEvent(), customer, ticket.getPrice(), ticket.getTicketType());
        updateTicket(ticket);
    }

    /**
     * Releases a previously reserved ticket.
     *
     * @param ticket the ticket to release.
     */
    public void releaseTicket(Ticket ticket) {
        ticket.setSold(false);
        ticket.setCustomer(null);
        ticket.setPurchaseDate(null);

        seatService.unreserveSeat(ticket.getSeat().getID());
        updateTicket(ticket);
    }

    /**
     * Retrieves all tickets for a specific event.
     *
     * @param event the event for which to retrieve tickets.
     * @return the list of tickets.
     */
    public List<Ticket> getTicketsByEvent(Event event) {
        return ticketRepository.getAll().stream()
                .filter(ticket -> ticket.getEvent().equals(event))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all available (unsold) tickets for a specific event.
     *
     * @param event the event for which to retrieve available tickets.
     * @return the list of available tickets.
     */
    public List<Ticket> getAvailableTicketsForEvent(Event event) {
        return getTicketsByEvent(event).stream()
                .filter(ticket -> !ticket.isSold())
                .collect(Collectors.toList());
    }

    /**
     * Finds tickets by the ID of the cart they belong to.
     *
     * @param cartID the ID of the cart.
     * @return the list of tickets associated with the cart.
     */
    public List<Ticket> findTicketsByCartID(int cartID) {
        return ticketRepository.getAll().stream()
                .filter(ticket -> ticket.getCart() != null && ticket.getCart().getID() == cartID)
                .collect(Collectors.toList());
    }

    /**
     * Updates a ticket in all repositories.
     *
     * @param ticket the ticket to update.
     */
    private void updateTicket(Ticket ticket) {
        ticketRepository.update(ticket);
        ticketFileRepository.update(ticket);
    }

    /**
     * Finds a ticket by its ID.
     *
     * @param ticketID the ID of the ticket to find.
     * @return the ticket if found, or null otherwise.
     */
    public Ticket findTicketByID(int ticketID) {
        return ticketRepository.read(ticketID);
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param ticketID the ID of the ticket to delete.
     */
    public void deleteTicket(int ticketID) {
        Ticket ticket = findTicketByID(ticketID);
        ticketRepository.delete(ticketID);
        ticketFileRepository.delete(ticketID);
    }

    /**
     * Calculates the total price of a list of tickets.
     *
     * @param tickets the list of tickets.
     * @return the total price.
     */
    public double calculateTotalPrice(List<Ticket> tickets) {
        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }
}