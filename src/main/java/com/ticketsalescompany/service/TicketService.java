package com.ticketsalescompany.service;

import com.ticketsalescompany.model.*;
import com.ticketsalescompany.repository.FileRepository;
import com.ticketsalescompany.repository.IRepository;
import com.ticketsalescompany.repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing ticket-related operations, including generating, reserving,
 * releasing, and retrieving tickets.
 */
public class TicketService {

    private final IRepository<Ticket> ticketRepository;
    private final FileRepository<Ticket> ticketFileRepository;
    private final DBRepository<Ticket> ticketDatabaseRepository;
    private final SeatService seatService;
    private final EventService eventService;
    private final VenueService venueService;

    /**
     * Constructs a TicketService with dependencies for ticket, seat, event, and venue management.
     *
     * @param ticketRepository the unified repository for managing tickets.
     * @param seatService      the service for managing seats and reservations.
     * @param eventService     the service for managing events.
     * @param venueService     the service for managing venues and seat availability.
     */
    public TicketService(IRepository<Ticket> ticketRepository, SeatService seatService,
                         EventService eventService, VenueService venueService) {
        this.ticketRepository = ticketRepository;
        this.seatService = seatService;
        this.eventService = eventService;
        this.venueService = venueService;

        // Initialize file and database repositories
        this.ticketFileRepository = new FileRepository<>("src/repository/data/tickets.csv", Ticket::fromCsv);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.ticketDatabaseRepository = new DBRepository<>(entityManagerFactory, Ticket.class);

        // Sync tickets from file and database
        syncFromCsv();
        syncFromDatabase();
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
     * Synchronizes tickets from the database into the main repository.
     */
    private void syncFromDatabase() {
        List<Ticket> tickets = ticketDatabaseRepository.getAll();
        for (Ticket ticket : tickets) {
            ticketRepository.create(ticket);
        }
    }

    /**
     * Generates tickets for a specific event based on available seats.
     *
     * @param event         the event for which tickets are generated.
     * @param standardPrice the price for standard tickets.
     * @param vipPrice      the price for VIP tickets.
     * @return a list of generated tickets.
     */
    public List<Ticket> generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        // Resolve the Venue using the venueID from Event
        Venue venue = venueService.findVenueByID(event.getVenueID());
        //todo if venue null case

        // Retrieve available seats
        List<Seat> availableSeats = venueService.getAvailableSeats(venue, event);
        List<Ticket> generatedTickets = availableSeats.stream()
                .map(seat -> {
                    TicketType type = determineTicketType(seat);
                    double price = (type == TicketType.VIP) ? vipPrice : standardPrice;
                    return new Ticket(event, seat.getSection(), seat, price, type);
                })
                .collect(Collectors.toList());

        // Save tickets to repositories
        for (Ticket ticket : generatedTickets) {
            ticketRepository.create(ticket);
            ticketFileRepository.create(ticket);
            ticketDatabaseRepository.create(ticket);
        }
        return generatedTickets;
    }

    /**
     * Determines the ticket type based on seat characteristics.
     *
     * @param seat the seat for which to determine the ticket type.
     * @return the ticket type (VIP or STANDARD).
     */
    private TicketType determineTicketType(Seat seat) {
        return seat.getRow().getRowCapacity() == 1 ? TicketType.VIP : TicketType.STANDARD;
    }

    /**
     * Reserves a ticket for a purchaser if it is not already sold.
     *
     * @param ticket        the ticket to reserve.
     * @param purchaserName the name of the purchaser.
     * @return true if the reservation is successful, false otherwise.
     */
    public boolean reserveTicket(Ticket ticket, String purchaserName) {
        if (!ticket.isSold()) {
            ticket.markAsSold(purchaserName);
            seatService.reserveSeatForEvent(ticket.getSeat(), ticket.getEvent());
            // Update repositories
            updateTicketInRepositories(ticket);
            return true;
        }
        return false;
    }

    /**
     * Releases a previously reserved ticket, making it available again.
     *
     * @param ticket the ticket to release.
     * @return true if the ticket was successfully released, false otherwise.
     */
    public boolean releaseTicket(Ticket ticket) {
        if (ticket.isSold()) {
            ticket.setSold(false);
            ticket.setPurchaserName(null);
            ticket.setPurchaseDate(null);
            seatService.clearSeatReservationForEvent(ticket.getSeat(), ticket.getEvent());

            // Update repositories
            updateTicketInRepositories(ticket);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all tickets for a specific event.
     *
     * @param event the event to retrieve tickets for.
     * @return a list of tickets for the event.
     */
    public List<Ticket> getTicketsByEvent(Event event) {
        return ticketRepository.getAll().stream()
                .filter(ticket -> ticket.getEvent().equals(event))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all available tickets (unsold) for a specific event.
     *
     * @param event the event to retrieve available tickets for.
     * @return a list of available tickets.
     */
    public List<Ticket> getAvailableTicketsForEvent(Event event) {
        return getTicketsByEvent(event).stream()
                .filter(ticket -> !ticket.isSold())
                .collect(Collectors.toList());
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param ticketID the ID of the ticket to delete.
     * @return true if the ticket was deleted, false otherwise.
     */
    public boolean deleteTicket(int ticketID) {
        Ticket ticket = findTicketByID(ticketID);
        if (ticket != null) {
            ticketRepository.delete(ticketID);
            ticketFileRepository.delete(ticketID);
            ticketDatabaseRepository.delete(ticketID);
            return true;
        }
        return false;
    }

    /**
     * Finds a ticket by its unique ID.
     *
     * @param ticketID the ID of the ticket to find.
     * @return the Ticket object, or null if not found.
     */
    public Ticket findTicketByID(int ticketID) {
        return ticketRepository.read(ticketID);
    }

    /**
     * Updates a ticket in all repositories.
     *
     * @param ticket the ticket to update.
     */
    protected void updateTicketInRepositories(Ticket ticket) {
        ticketRepository.update(ticket);
        ticketFileRepository.update(ticket);
        ticketDatabaseRepository.update(ticket);
    }

    /**
     * Calculates the total price of a list of tickets.
     *
     * @param tickets the list of tickets.
     * @return the total price of the tickets.
     */
    public double calculateTotalPrice(List<Ticket> tickets) {
        return tickets.stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
    }
}
