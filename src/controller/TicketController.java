// TODO JavaDocs

package controller;

import model.Event;
import model.Ticket;
import service.TicketService;

import java.util.List;

public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Generates tickets for an event with specified prices for standard and VIP tickets
    public void generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        List<Ticket> generatedTickets = ticketService.generateTicketsForEvent(event, standardPrice, vipPrice);
        if (!generatedTickets.isEmpty()) {
            System.out.println("Tickets generated successfully for event: " + event.getEventName() + " (" + generatedTickets.size() + " tickets)");
        } else {
            System.out.println("No tickets generated for event: " + event.getEventName());
        }
    }

    // Retrieves all available tickets for a specific event
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

    // Reserves a ticket for a specific event
    public void reserveTicket(Ticket ticket, String purchaserName) {
        boolean reserved = ticketService.reserveTicket(ticket, purchaserName);
        if (reserved) {
            System.out.println("Ticket ID: " + ticket.getID() + " has been reserved for " + purchaserName);
        } else {
            System.out.println("Ticket ID: " + ticket.getID() + " could not be reserved. It may already be sold.");
        }
    }

    // Releases a reserved ticket, making it available again
    public void releaseTicket(Ticket ticket) {
        boolean released = ticketService.releaseTicket(ticket);
        if (released) {
            System.out.println("Ticket ID: " + ticket.getID() + " has been released and is now available.");
        } else {
            System.out.println("Ticket ID: " + ticket.getID() + " is not currently reserved.");
        }
    }

    // Retrieves ticket information by ticket ID
    public Ticket getTicketByID(int ticketId) {
        Ticket ticket = ticketService.getTicketByID(ticketId);
        if (ticket != null) {
            System.out.println("Ticket found: " + ticket);
        } else {
            System.out.println("No ticket found with ID: " + ticketId);
        }
        return ticket;
    }

    // Deletes a ticket by ID
    public void deleteTicket(int ticketId) {
        boolean deleted = ticketService.deleteTicket(ticketId);
        if (deleted) {
            System.out.println("Ticket with ID " + ticketId + " has been deleted.");
        } else {
            System.out.println("Ticket with ID " + ticketId + " could not be deleted. It may not exist.");
        }
    }

    // Calculates the total price of a list of tickets
    public void calculateTotalPrice(List<Ticket> tickets) {
        double totalPrice = ticketService.calculateTotalPrice(tickets);
        System.out.println("Total price for selected tickets: $" + totalPrice);
    }

    public List<Ticket> getTicketsByEvent(int eventId) {
        return ticketService.getTicketsByEvent(eventId);
    }
}
