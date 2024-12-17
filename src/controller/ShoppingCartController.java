package controller;

import model.*;
import service.ShoppingCartService;
import service.CustomerService;

import java.util.List;

/**
 * The ShoppingCartController class provides methods to manage the current customer's shopping cart.
 * It allows adding, removing, clearing tickets, checking out, and displaying the total price of items in the cart.
 */
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CustomerService customerService;

    /**
     * Constructs a ShoppingCartController with the specified ShoppingCartService and CustomerService.
     *
     * @param shoppingCartService the service for shopping cart-related operations
     * @param customerService     the service for customer-related operations
     */
    public ShoppingCartController(ShoppingCartService shoppingCartService, CustomerService customerService) {
        this.shoppingCartService = shoppingCartService;
        this.customerService = customerService;
    }

    /**
     * Adds a ticket to the current customer's shopping cart.
     *
     * @param ticket the ticket to add to the cart
     */
    public void addTicketToCart(Event event, Ticket ticket) {
        ShoppingCart shoppingCart = customerService.getCurrentCustomer().getShoppingCart();
        boolean isAdded = shoppingCartService.addTicketToCart(shoppingCart, event, ticket);
        if (isAdded) {
            System.out.println("Ticket successfully added to the cart.");
        } else {
            System.out.println("Ticket is already in the cart.");
        }
    }

    /**
     * Removes a ticket from the current customer's shopping cart.
     *
     * @param ticket the ticket to remove from the cart
     */
    public void removeTicketFromCart(Ticket ticket) {
        ShoppingCart shoppingCart = customerService.getCurrentCustomer().getShoppingCart();
        boolean isRemoved = shoppingCartService.removeTicketFromCart(shoppingCart, ticket);
        if (isRemoved) {
            System.out.println("Ticket successfully removed from the cart.");
        } else {
            System.out.println("Ticket not found in the cart.");
        }
    }

    /**
     * Clears all tickets from the current customer's shopping cart.
     */
    public void clearCart() {
        ShoppingCart shoppingCart = customerService.getCurrentCustomer().getShoppingCart();
        shoppingCartService.clearCart(shoppingCart);
        System.out.println("Shopping cart has been cleared.");
    }

    public List<ShoppingCartTicket> getTicketsByShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartService.getTicketsByShoppingCart(shoppingCart);
    }

    public void findShoppingCartByID(int shoppingCartID) {
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartByID(shoppingCartID);
        if (shoppingCart != null) {
            System.out.println("Shopping cart found: ID " + shoppingCartID + ", Total Price: $" + shoppingCart.getTotalPrice());
        } else {
            System.out.println("Shopping cart with ID " + shoppingCartID + " not found.");
        }
    }

}
