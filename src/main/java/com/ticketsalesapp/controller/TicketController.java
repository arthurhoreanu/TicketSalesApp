package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.model.event.Event;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.ticket.TicketType;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.service.TicketService;

import java.util.List;

/**
 * Controller for managing ticket-related operations.
 */
public class TicketController {
    private final TicketService ticketService;

    /**
     * Constructs a TicketController with the specified TicketService.
     *
     * @param ticketService The service for managing tickets.
     */
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Generates tickets for an event.
     *
     * @param event     the event for which tickets are generated.
     * @param basePrice the base price for EARLY_BIRD tickets.
     * @return the list of generated tickets.
     */
    public List<Ticket> generateTicketsForEvent(Event event, double basePrice, int earlyBirdCount, int vipCount, int standardCount) {
        List<Ticket> tickets = ticketService.generateTicketsForEvent(event, basePrice, earlyBirdCount, vipCount, standardCount);
        if(tickets.isEmpty()) {
            System.out.println("Failed to generate tickets for event");
        }
        return tickets;
    }

    /**
     * Reserves a ticket for a customer.
     *
     * @param ticket   the ticket to reserve.
     * @param customer the customer reserving the ticket.
     */
    public void reserveTicket(Ticket ticket, Customer customer) {
        if (ticket == null || customer == null) {
            System.out.println("Invalid ticket or customer.");
            return;
        }
        ticketService.reserveTicket(ticket, customer);
        System.out.println("Ticket reserved successfully for customer: " + customer);
    }

    /**
     * Releases a reserved ticket.
     *
     * @param ticket the ticket to release.
     */
    public void releaseTicket(Ticket ticket) {
        if (ticket == null) {
            System.out.println("Invalid ticket.");
            return;
        }
        ticketService.releaseTicket(ticket);
        System.out.println("Ticket released successfully.");
    }

    /**
     * Retrieves tickets for a specific event.
     *
     * @param event the event for which tickets are retrieved.
     * @return the list of tickets for the event.
     */
    public List<Ticket> getTicketsByEvent(Event event) {
        if (event == null) {
            System.out.println("Invalid event.");
            return null;
        }
        List<Ticket> tickets = ticketService.getTicketsByEvent(event);
        if (!tickets.isEmpty()) {
            System.out.println("Tickets for Event ID " + event.getID() + ":");
            tickets.forEach(System.out::println);
        } else {
            System.out.println("No tickets found for Event ID " + event.getID() + ".");
        }
        return tickets;
    }

    /**
     * Retrieves available tickets for a specific event.
     *
     * @param event the event for which available tickets are retrieved.
     * @return the list of available tickets for the event.
     */
    public List<Ticket> getAvailableTicketsForEvent(Event event) {
        if (event == null) {
            System.out.println("Invalid event.");
            return null;
        }
        List<Ticket> availableTickets = ticketService.getAvailableTicketsForEvent(event);
        if (!availableTickets.isEmpty()) {
            System.out.println("Available tickets for Event ID " + event.getID() + ":");
            availableTickets.forEach(System.out::println);
        } else {
            System.out.println("No available tickets for Event ID " + event.getID() + ".");
        }
        return availableTickets;
    }

    /**
     * Finds tickets associated with a specific shopping cart.
     *
     * @param cartID the ID of the shopping cart.
     * @return the list of tickets in the shopping cart.
     */
    public List<Ticket> findTicketsByCartID(int cartID) {
        if (cartID <= 0) {
            System.out.println("Invalid cart ID.");
            return null;
        }
        List<Ticket> tickets = ticketService.findTicketsByCartID(cartID);
        if (!tickets.isEmpty()) {
            System.out.println("Tickets in Cart ID " + cartID + ":");
            tickets.forEach(System.out::println);
        } else {
            System.out.println("No tickets found in Cart ID " + cartID + ".");
        }
        return tickets;
    }

    /**
     * Finds a ticket by its ID.
     *
     * @param ticketID the ID of the ticket to find.
     * @return the ticket if found, or null otherwise.
     */
    public Ticket findTicketByID(int ticketID) {
        if (ticketID <= 0) {
            System.out.println("Invalid ticket ID.");
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
     * Deletes a ticket by its ID.
     *
     * @param ticketID the ID of the ticket to delete.
     */
    public void deleteTicket(int ticketID) {
        if (ticketID <= 0) {
            System.out.println("Invalid ticket ID.");
            return;
        }
        ticketService.deleteTicket(ticketID);
        System.out.println("Ticket with ID " + ticketID + " deleted successfully.");
    }

    /**
     * Calculates the total price of a list of tickets.
     *
     * @param tickets the list of tickets.
     * @return the total price of the tickets.
     */
    public double calculateTotalPrice(List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No tickets provided.");
            return 0.0;
        }
        double totalPrice = ticketService.calculateTotalPrice(tickets);
        System.out.println("Total price of tickets: " + totalPrice);
        return totalPrice;
    }

    public List<String> getTicketAvailabilityByType(Event event) {
        return ticketService.getTicketAvailabilityByType(event);
    }

    public List<Ticket> getAvailableTicketsByType(Event event, TicketType ticketType) {
        return ticketService.getAvailableTicketsByType(event, ticketType);
    }

    public List<Ticket> getTicketsByCustomer(Customer customer) {
        return ticketService.getTicketsByCustomer(customer);}
}
