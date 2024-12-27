package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing ticket-related operations.
 */
public class TicketService {

    private final IRepository<Ticket> ticketRepository;
    private final FileRepository<Ticket> ticketFileRepository;
    private final SeatService seatService;
    private final EventService eventService;

    /**
     * Constructs a TicketService with dependencies for managing tickets, seats, and events.
     *
     * @param ticketRepository the repository for ticket persistence.
     * @param seatService      the service for managing seat operations.
     * @param eventService     the service for managing event operations.
     */
    public TicketService(IRepository<Ticket> ticketRepository, SeatService seatService, EventService eventService) {
        this.ticketRepository = ticketRepository;
        this.seatService = seatService;
        this.eventService = eventService;

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
     * @param standardPrice the price for standard tickets.
     * @param vipPrice      the price for VIP tickets.
     * @return the list of generated tickets.
     */
    public List<Ticket> generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        List<Seat> availableSeats = seatService.getAvailableSeatsInVenue(event.getVenueID());
        List<Ticket> tickets = availableSeats.stream().map(seat -> {
            TicketType type = determineTicketType(seat);
            double price = (type == TicketType.VIP) ? vipPrice : standardPrice;
            return new Ticket(0, event, seat, null, price, type);
        }).collect(Collectors.toList());

        for (Ticket ticket : tickets) {
            ticketRepository.create(ticket);
            ticketFileRepository.create(ticket);
        }

        return tickets;
    }

    /**
     * Determines the ticket type based on seat characteristics.
     *
     * @param seat the seat for which to determine the ticket type.
     * @return the ticket type.
     */
    private TicketType determineTicketType(Seat seat) {
        return (seat.getRow().getRowCapacity() <= 5) ? TicketType.VIP : TicketType.STANDARD;
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
