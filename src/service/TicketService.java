package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing ticket-related operations, including generating, reserving, releasing,
 * and calculating the total price of tickets.
 */
public class TicketService {
    private final IRepository<Ticket> ticketRepository;
    private final SeatService seatService;
    private final EventService eventService;
    private final VenueService venueService; // Dependency on VenueService for seat management
    private final FileRepository<Ticket> ticketFileRepository;

    /**
     * Constructs a TicketService with the specified dependencies.
     *
     * @param ticketRepository the repository for managing Ticket persistence.
     * @param seatService      the service for managing seat reservations.
     * @param eventService     the service for managing events.
     * @param venueService     the service for managing venue-related operations.
     */
    public TicketService(IRepository<Ticket> ticketRepository, SeatService seatService, EventService eventService, VenueService venueService) {
        this.ticketRepository = ticketRepository;
        this.seatService = seatService;
        this.eventService = eventService;
        this.venueService = venueService;
        this.ticketFileRepository = new FileRepository<>("src/repository/data/tickets.csv", Ticket::fromCsv);
        List<Ticket> ticketsFromFile = ticketFileRepository.getAll();
        for (Ticket ticket : ticketsFromFile) {
            ticketRepository.create(ticket);
        }
    }

    /**
     * Retrieves a list of available seats for a specific event using VenueService.
     *
     * @param event the event for which to retrieve available seats.
     * @return a list of available seats for the specified event.
     */
    public List<Seat> getAvailableSeatsForEvent(Event event) {
        return venueService.getAvailableSeatsList(event.getVenue(), event);
    }

    /**
     * Generates tickets for a specified event based on available seats and assigns them
     * a standard or VIP price.
     *
     * @param event         the event for which tickets are generated.
     * @param standardPrice the price for standard tickets.
     * @param vipPrice      the price for VIP tickets.
     * @return a list of generated tickets for the event.
     */
    public List<Ticket> generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        List<Ticket> generatedTickets = new ArrayList<>();
        List<Seat> availableSeats = venueService.getAvailableSeatsList(event.getVenue(), event);
        for (Seat seat : availableSeats) {
            TicketType type = seat.getRowNumber() == 1 ? TicketType.VIP : TicketType.STANDARD;
            double price = (type == TicketType.VIP) ? vipPrice : standardPrice;
            Ticket ticket = new Ticket(ticketRepository.getAll().size() + 1, event, seat.getSection(), seat, price, type);
            ticketRepository.create(ticket);
            ticketFileRepository.create(ticket);
            generatedTickets.add(ticket);
        }
        return generatedTickets;
    }


    /**
     * Determines the type of a ticket based on the seat's characteristics.
     * In this example, seats in the first row are considered VIP.
     *
     * @param seat the seat for which to determine the ticket type.
     * @return the type of ticket, either VIP or STANDARD.
     */
    private TicketType determineTicketType(Seat seat) {
        return seat.getRowNumber() == 1 ? TicketType.VIP : TicketType.STANDARD;
    }

    /**
     * Retrieves all available (unsold) tickets for a specific event.
     *
     * @param event the event for which to retrieve available tickets.
     * @return a list of available tickets for the event.
     */
    public List<Ticket> getAvailableTicketsForEvent(Event event) {
        List<Ticket> availableTickets = new ArrayList<>();
        List<Ticket> allTickets = ticketRepository.getAll();

        for (int i = 0; i < allTickets.size(); i++) {
            Ticket ticket = allTickets.get(i);
            if (ticket.getEvent().equals(event) && !ticket.isSold()) {
                availableTickets.add(ticket);
            }
        }
        return availableTickets;
    }

    /**
     * Reserves a ticket for a purchaser if it is not already sold.
     *
     * @param ticket       the ticket to reserve.
     * @param purchaserName the name of the purchaser.
     * @return true if the ticket was successfully reserved, false if it was already sold.
     */
    public boolean reserveTicket(Ticket ticket, String purchaserName) {
        if (!ticket.isSold()) {
            ticket.markAsSold(purchaserName);
            seatService.reserveSeatForEvent(ticket.getSeat(), ticket.getEvent());
            ticketRepository.update(ticket);
            ticketFileRepository.update(ticket);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Releases a previously reserved ticket, making it available again.
     *
     * @param ticket the ticket to release.
     * @return true if the ticket was successfully released, false if it was not reserved.
     */
    public boolean releaseTicket(Ticket ticket) {
        if (ticket.isSold()) {
            ticket.setSold(false);
            seatService.clearSeatReservationForEvent(ticket.getSeat(), ticket.getEvent());
            ticketRepository.update(ticket);
            ticketFileRepository.update(ticket);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a ticket by its unique ID.
     *
     * @param ticketId the ID of the ticket to retrieve.
     * @return the ticket with the specified ID, or null if no such ticket exists.
     */
    public Ticket getTicketByID(int ticketId) {
        List<Ticket> tickets = ticketRepository.getAll();
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            if (ticket.getID() == ticketId) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * Deletes a ticket by its unique ID.
     *
     * @param ticketId the ID of the ticket to delete.
     * @return true if the ticket was successfully deleted, false if it was not found.
     */
    public boolean deleteTicket(int ticketId) {
        Ticket ticket = getTicketByID(ticketId);
        if (ticket != null) {
            ticketRepository.delete(ticketId);
            ticketFileRepository.delete(ticketId);
            return true;
        }
        return false;
    }

    /**
     * Calculates the total price of a list of tickets.
     *
     * @param tickets the list of tickets to calculate the total price for.
     * @return the total price of the tickets.
     */
    public double calculateTotalPrice(List<Ticket> tickets) {
        double totalPrice = 0;
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            totalPrice += ticket.getPrice();
        }
        return totalPrice;
    }

    /**
     * Retrieves a list of tickets for a specific event by the event's ID.
     *
     * @param eventId the ID of the event for which to retrieve tickets.
     * @return a list of tickets associated with the specified event.
     */
    public List<Ticket> getTicketsByEvent(int eventId) {
        List<Ticket> ticketsForEvent = new ArrayList<>();
        for (Ticket ticket : ticketRepository.getAll()) {
            if (ticket.getEventId() == eventId) {
                ticketsForEvent.add(ticket);
            }
        }
        return ticketsForEvent;
    }
}
