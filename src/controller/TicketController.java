package controller;

import model.*;
import service.TicketService;

import java.util.List;

/**
 * The TicketController class handles operations related to tickets,
 * including generation, reservation, release, and retrieval.
 */
public class TicketController {

    private final TicketService ticketService;

    /**
     * Constructs a TicketController with the specified TicketService.
     *
     * @param ticketService the service managing ticket-related operations
     */
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Generates tickets for an event based on seat availability and pricing.
     *
     * @param event         the event for which tickets are generated
     * @param standardPrice the price for standard tickets
     * @param vipPrice      the price for VIP tickets
     */
    public void generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        List<Ticket> tickets = ticketService.generateTicketsForEvent(event, standardPrice, vipPrice);
        if (tickets.isEmpty()) {
            System.out.println("No tickets were generated for the event: " + event.getEventName());
        } else {
            System.out.println("Tickets successfully generated for event: " + event.getEventName());
            tickets.forEach(ticket -> System.out.println(ticket));
        }
    }

    /**
     * Reserves a ticket for a purchaser.
     *
     * @param ticket        the ticket to reserve
     * @param purchaserName the name of the purchaser
     */
    public void reserveTicket(Ticket ticket, String purchaserName) {
        boolean success = ticketService.reserveTicket(ticket, purchaserName);
        if (success) {
            System.out.println("Ticket reserved successfully for: " + purchaserName);
        } else {
            System.out.println("Failed to reserve ticket. It may already be sold.");
        }
    }

    /**
     * Releases a previously reserved ticket.
     *
     * @param ticket the ticket to release
     */
    public void releaseTicket(Ticket ticket) {
        boolean success = ticketService.releaseTicket(ticket);
        if (success) {
            System.out.println("Ticket released successfully.");
        } else {
            System.out.println("Failed to release ticket. It may not be reserved.");
        }
    }

    /**
     * Retrieves all tickets associated with a specific event.
     *
     * @param event the event to retrieve tickets for
     * @return the list of tickets for the event
     */
    public List<Ticket> getTicketsByEvent(Event event) {
        List<Ticket> tickets = ticketService.getTicketsByEvent(event);
        if (tickets.isEmpty()) {
            System.out.println("No tickets found for the event: " + event.getEventName());
        } else {
            System.out.println("Tickets for event: " + event.getEventName());
            tickets.forEach(ticket -> System.out.println(ticket));
        }
        return tickets;
    }

    /**
     * Retrieves available (unsold) tickets for a specific event.
     *
     * @param event the event to retrieve available tickets for
     * @return the list of available tickets
     */
    public List<Ticket> getAvailableTicketsForEvent(Event event) {
        List<Ticket> availableTickets = ticketService.getAvailableTicketsForEvent(event);
        if (availableTickets.isEmpty()) {
            System.out.println("No available tickets for the event: " + event.getEventName());
        } else {
            System.out.println("Available tickets for event: " + event.getEventName());
            availableTickets.forEach(ticket -> System.out.println(ticket));
        }
        return availableTickets;
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param ticketID the ID of the ticket to delete
     */
    public void deleteTicket(int ticketID) {
        boolean success = ticketService.deleteTicket(ticketID);
        if (success) {
            System.out.println("Ticket with ID " + ticketID + " deleted successfully.");
        } else {
            System.out.println("Failed to delete ticket. Ticket ID not found: " + ticketID);
        }
    }

    /**
     * Finds a ticket by its unique ID.
     *
     * @param ticketID the ID of the ticket to find
     * @return the Ticket object, or null if not found
     */
    public Ticket findTicketByID(int ticketID) {
        Ticket ticket = ticketService.findTicketByID(ticketID);
        if (ticket != null) {
            System.out.println("Ticket found: " + ticket);
        } else {
            System.out.println("Ticket with ID " + ticketID + " not found.");
        }
        return ticket;
    }

    /**
     * Calculates the total price for a list of tickets.
     *
     * @param tickets the list of tickets
     * @return the total price of the tickets
     */
    public double calculateTotalPrice(List<Ticket> tickets) {
        double totalPrice = ticketService.calculateTotalPrice(tickets);
        System.out.println("Total price for selected tickets: " + totalPrice);
        return totalPrice;
    }
}
