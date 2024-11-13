package controller;

import model.Event;
import model.Ticket;
import service.TicketService;

import java.util.List;

/**
 * The TicketController class provides methods for managing tickets related to events.
 * It allows generating, retrieving, reserving, releasing, and deleting tickets, as well as calculating ticket prices.
 */
public class TicketController {
    private final TicketService ticketService;

    /**
     * Constructs a TicketController with the specified TicketService.
     *
     * @param ticketService the service for ticket-related operations
     */
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Generates tickets for a specified event with given prices for standard and VIP tickets.
     *
     * @param event         the event for which tickets are generated
     * @param standardPrice the price for standard tickets
     * @param vipPrice      the price for VIP tickets
     */
    public void generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        List<Ticket> generatedTickets = ticketService.generateTicketsForEvent(event, standardPrice, vipPrice);
        if (!generatedTickets.isEmpty()) {
            System.out.println("Tickets generated successfully for event: " + event.getEventName() + " (" + generatedTickets.size() + " tickets)");
        } else {
            System.out.println("No tickets generated for event: " + event.getEventName());
        }
    }

    /**
     * Retrieves and displays all available tickets for a specified event.
     *
     * @param event the event for which available tickets are retrieved
     */
    public void getAvailableTicketsForEvent(Event event) {
        List<Ticket> availableTickets = ticketService.getAvailableTicketsForEvent(event);
        if (!availableTickets.isEmpty()) {
            System.out.println("Available tickets for event: " + event.getEventName());
            for (Ticket ticket : availableTickets) {
                System.out.println("- Ticket ID: " + ticket.getID() + ", Price: $" + ticket.getPrice() + ", Seat: " +
                        "Row " + ticket.getSeat().getRowNumber() + ", Seat " + ticket.getSeat().getSeatNumber());
            }
        } else {
            System.out.println("No available tickets for event: " + event.getEventName());
        }
    }

    /**
     * Reserves a ticket for a specified event and purchaser.
     *
     * @param ticket       the ticket to be reserved
     * @param purchaserName the name of the person reserving the ticket
     */
    public void reserveTicket(Ticket ticket, String purchaserName) {
        boolean reserved = ticketService.reserveTicket(ticket, purchaserName);
        if (reserved) {
            System.out.println("Ticket ID: " + ticket.getID() + " has been reserved for " + purchaserName);
        } else {
            System.out.println("Ticket ID: " + ticket.getID() + " could not be reserved. It may already be sold.");
        }
    }

    /**
     * Releases a reserved ticket, making it available again.
     *
     * @param ticket the ticket to be released
     */
    public void releaseTicket(Ticket ticket) {
        boolean released = ticketService.releaseTicket(ticket);
        if (released) {
            System.out.println("Ticket ID: " + ticket.getID() + " has been released and is now available.");
        } else {
            System.out.println("Ticket ID: " + ticket.getID() + " is not currently reserved.");
        }
    }

    /**
     * Retrieves ticket information by ticket ID.
     *
     * @param ticketId the ID of the ticket to be retrieved
     * @return the ticket with the specified ID, or null if not found
     */
    public Ticket getTicketByID(int ticketId) {
        Ticket ticket = ticketService.getTicketByID(ticketId);
        if (ticket != null) {
            System.out.println("Ticket found: " + ticket);
        } else {
            System.out.println("No ticket found with ID: " + ticketId);
        }
        return ticket;
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param ticketId the ID of the ticket to be deleted
     */
    public void deleteTicket(int ticketId) {
        boolean deleted = ticketService.deleteTicket(ticketId);
        if (deleted) {
            System.out.println("Ticket with ID " + ticketId + " has been deleted.");
        } else {
            System.out.println("Ticket with ID " + ticketId + " could not be deleted. It may not exist.");
        }
    }

    /**
     * Calculates and displays the total price of a list of tickets.
     *
     * @param tickets the list of tickets for which to calculate the total price
     */
    public void calculateTotalPrice(List<Ticket> tickets) {
        double totalPrice = ticketService.calculateTotalPrice(tickets);
        System.out.println("Total price for selected tickets: $" + totalPrice);
    }

    /**
     * Retrieves a list of tickets associated with a specific event ID.
     *
     * @param eventId the ID of the event for which to retrieve tickets
     * @return the list of tickets for the specified event
     */
    public List<Ticket> getTicketsByEvent(int eventId) {
        return ticketService.getTicketsByEvent(eventId);
    }
}
