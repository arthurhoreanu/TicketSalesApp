package service;

import model.Event;
import model.Seat;
import model.Ticket;
import model.TicketType;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class TicketService {
    private final IRepository<Ticket> ticketRepository;
    private final SeatService seatService;
    private final EventService eventService;
    private final VenueService venueService; // Added dependency on VenueService

    public TicketService(IRepository<Ticket> ticketRepository, SeatService seatService, EventService eventService, VenueService venueService) {
        this.ticketRepository = ticketRepository;
        this.seatService = seatService;
        this.eventService = eventService;
        this.venueService = venueService; // Initialize VenueService
    }

    // New method to retrieve available seats for an event using VenueService
    public List<Seat> getAvailableSeatsForEvent(Event event) {
        return venueService.getAvailableSeatsList(event.getVenue(), event);
    }


    // Generates tickets for an event based on available seats in the event's venue
    public List<Ticket> generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        List<Ticket> generatedTickets = new ArrayList<>();

        // Use the new method to get available seats for the event
        List<Seat> availableSeats = getAvailableSeatsForEvent(event);

        for (int i = 0; i < availableSeats.size(); i++) {
            Seat seat = availableSeats.get(i);
            TicketType type = determineTicketType(seat);
            double price = (type == TicketType.VIP) ? vipPrice : standardPrice;
            Ticket ticket = new Ticket(ticketRepository.getAll().size() + 1, event, seat.getSection(), seat, price, type);
            ticketRepository.create(ticket);
            generatedTickets.add(ticket);
        }

        return generatedTickets;
    }

    // Determines ticket type based on custom logic (e.g., VIP if in certain rows)
    private TicketType determineTicketType(Seat seat) {
        return seat.getRowNumber() == 1 ? TicketType.VIP : TicketType.STANDARD;
    }

    // Retrieves all available tickets for a specific event
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

    // Reserves a ticket for a specific event
    public boolean reserveTicket(Ticket ticket, String purchaserName) {
        if (!ticket.isSold()) {
            ticket.markAsSold(purchaserName);
            seatService.reserveSeatForEvent(ticket.getSeat(), ticket.getEvent());
            ticketRepository.update(ticket);
            return true;
        } else {
            return false;
        }
    }

    // Releases a reserved ticket, making it available again
    public boolean releaseTicket(Ticket ticket) {
        if (ticket.isSold()) {
            ticket.setSold(false);
            seatService.clearSeatReservationForEvent(ticket.getSeat(), ticket.getEvent());
            ticketRepository.update(ticket);
            return true;
        } else {
            return false;
        }
    }

    // Retrieves ticket by ID
    public Ticket getTicketById(int ticketId) {
        List<Ticket> tickets = ticketRepository.getAll();
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            if (ticket.getID() == ticketId) {
                return ticket;
            }
        }
        return null;
    }

    // Deletes a ticket by ID
    public boolean deleteTicket(int ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            ticketRepository.delete(ticketId);
            return true;
        }
        return false;
    }

    // Calculates the total price of a list of tickets
    public double calculateTotalPrice(List<Ticket> tickets) {
        double totalPrice = 0;
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            totalPrice += ticket.getPrice();
        }
        return totalPrice;
    }

    // In TicketService class
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
