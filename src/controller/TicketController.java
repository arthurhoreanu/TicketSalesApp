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
     * @param basePrice     the base price for early bird tickets
     */
    public List<Ticket> generateTicketsForEvent(Event event, double basePrice) {
        if (event == null) {
            System.out.println("Invalid event. Cannot generate tickets.");
            return null;
        }
        List<Ticket> tickets = ticketService.generateTicketsForEvent(event, basePrice);
        if (tickets.isEmpty()) {
            System.out.println("No tickets were generated for the event: " + event.getEventName());
        } else {
            System.out.println("Tickets successfully generated for event: " + event.getEventName());
            tickets.forEach(ticket -> System.out.println(ticket));
        }
        return tickets;
    }

    /**
     * Reserves a ticket for a customer.
     *
     * @param ticket   the ticket to reserve
     * @param customer the customer reserving the ticket
     */
    public void reserveTicket(Ticket ticket, Customer customer) {
        if (ticket == null || customer == null) {
            System.out.println("Invalid ticket or customer. Reservation failed.");
            return;
        }

        if (ticket.isSold()) {
            System.out.println("Ticket is already reserved. Reservation failed.");
        } else {
            ticketService.reserveTicket(ticket, customer);
            System.out.println("Ticket reserved successfully for customer: " + customer.getUsername());
        }
    }

    /**
     * Releases a previously reserved ticket.
     *
     * @param ticket the ticket to release
     */
    public void releaseTicket(Ticket ticket) {
        if (ticket == null) {
            System.out.println("Invalid ticket. Release operation failed.");
            return;
        }

        if (!ticket.isSold()) {
            System.out.println("Ticket is not reserved. Release operation failed.");
        } else {
            ticketService.releaseTicket(ticket);
            System.out.println("Ticket released successfully.");
        }
    }

    /**
     * Retrieves all tickets associated with a specific event.
     *
     * @param event the event to retrieve tickets for
     * @return the list of tickets for the event
     */
    public List<Ticket> getTicketsByEvent(Event event) {
        if (event == null) {
            System.out.println("Invalid event. Cannot retrieve tickets.");
            return List.of();
        }

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
        if (event == null) {
            System.out.println("Invalid event. Cannot retrieve available tickets.");
            return List.of();
        }

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
     * Finds tickets by the ID of the cart they belong to.
     *
     * @param cartID the ID of the cart
     * @return the list of tickets associated with the cart
     */
    public List<Ticket> findTicketsByCartID(int cartID) {
        if (cartID <= 0) {
            System.out.println("Invalid cart ID. Cannot retrieve tickets.");
            return List.of();
        }

        List<Ticket> tickets = ticketService.findTicketsByCartID(cartID);
        if (tickets.isEmpty()) {
            System.out.println("No tickets found for cart ID: " + cartID);
        } else {
            System.out.println("Tickets associated with cart ID: " + cartID);
            tickets.forEach(ticket -> System.out.println(ticket));
        }
        return tickets;
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param ticketID the ID of the ticket to delete
     */
    public void deleteTicket(int ticketID) {
        if (ticketID <= 0) {
            System.out.println("Invalid ticket ID. Cannot delete ticket.");
            return;
        }

        Ticket ticket = ticketService.findTicketByID(ticketID);
        if (ticket == null) {
            System.out.println("Ticket with ID " + ticketID + " not found. Cannot delete.");
        } else {
            ticketService.deleteTicket(ticketID);
            System.out.println("Ticket with ID " + ticketID + " deleted successfully.");
        }
    }

    /**
     * Finds a ticket by its unique ID.
     *
     * @param ticketID the ID of the ticket to find
     * @return the Ticket object, or null if not found
     */
    public Ticket findTicketByID(int ticketID) {
        if (ticketID <= 0) {
            System.out.println("Invalid ticket ID. Cannot find ticket.");
            return null;
        }

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
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No tickets provided. Total price is 0.");
            return 0;
        }

        double totalPrice = ticketService.calculateTotalPrice(tickets);
        System.out.println("Total price for selected tickets: " + totalPrice);
        return totalPrice;
    }
}
