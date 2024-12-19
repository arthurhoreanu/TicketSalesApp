package controller;

import model.*;
import service.ShoppingCartTicketService;

import java.util.List;

/**
 * Controller class for managing ShoppingCartTicket-related operations.
 */
public class ShoppingCartTicketController {
    private final ShoppingCartTicketService shoppingCartTicketService;

    /**
     * Constructs a ShoppingCartTicketController with the specified ShoppingCartTicketService.
     *
     * @param shoppingCartTicketService the service for shopping cart ticket-related operations
     */
    public ShoppingCartTicketController(ShoppingCartTicketService shoppingCartTicketService) {
        this.shoppingCartTicketService = shoppingCartTicketService;
    }

    /**
     * Adds a ticket to a shopping cart for a specific event.
     *
     * @param shoppingCart the shopping cart to add the ticket to
     * @param event        the event associated with the ticket
     * @param ticket       the ticket to add to the shopping cart
     */
    public void addTicketToShoppingCart(ShoppingCart shoppingCart, Event event, Ticket ticket) {
        boolean isAdded = shoppingCartTicketService.addTicketToShoppingCart(shoppingCart, event, ticket);
        if (isAdded) {
            System.out.println("Ticket successfully added to the shopping cart for event: " + event.getEventName());
        } else {
            System.out.println("Failed to add ticket. Shopping cart contains tickets for a different event.");
        }
    }

    /**
     * Retrieves and prints all tickets in a shopping cart.
     *
     * @param shoppingCart the shopping cart to retrieve tickets for
     */
    public void getTicketsByShoppingCart(ShoppingCart shoppingCart) {
        List<ShoppingCartTicket> tickets = shoppingCartTicketService.getTicketsByShoppingCart(shoppingCart);
        if (tickets.isEmpty()) {
            System.out.println("The shopping cart is empty.");
        } else {
            System.out.println("Tickets in the shopping cart:");
            tickets.forEach(cartTicket -> System.out.println("- Event: " + cartTicket.getEvent().getEventName() +
                    ", Ticket ID: " + cartTicket.getTicket().getID()));
        }
    }

    /**
     * Removes a ticket from a shopping cart.
     *
     * @param shoppingCartTicket the ShoppingCartTicket to remove
     */
    public void removeTicketFromShoppingCart(ShoppingCartTicket shoppingCartTicket) {
        boolean isRemoved = shoppingCartTicketService.removeTicketFromShoppingCart(shoppingCartTicket);
        if (isRemoved) {
            System.out.println("Ticket successfully removed from the shopping cart.");
        } else {
            System.out.println("Failed to remove the ticket from the shopping cart.");
        }
    }

    /**
     * Finds a shopping cart ticket by its ID and prints the result.
     *
     * @param shoppingCartTicketID the ID of the ShoppingCartTicket to find
     */
    public void findShoppingCartTicketByID(int shoppingCartTicketID) {
        ShoppingCartTicket shoppingCartTicket = shoppingCartTicketService.findShoppingCartTicketByID(shoppingCartTicketID);
        if (shoppingCartTicket != null) {
            System.out.println("Shopping cart ticket found: Ticket ID: " + shoppingCartTicket.getTicket().getID() +
                    ", Event: " + shoppingCartTicket.getEvent().getEventName());
        } else {
            System.out.println("No shopping cart ticket found with ID: " + shoppingCartTicketID);
        }
    }
}
