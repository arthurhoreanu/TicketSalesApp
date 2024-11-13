package controller;

import model.Ticket;
import service.ShoppingCartService;
import service.CustomerService;
import model.Order;

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
    public void addTicketToCart(Ticket ticket) {
        boolean isAdded = shoppingCartService.addTicketToCart(customerService.getCurrentCustomer().getShoppingCart(), ticket);
        if (isAdded) {
            System.out.println("Ticket added to cart successfully.");
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
        boolean isRemoved = shoppingCartService.removeTicketFromCart(customerService.getCurrentCustomer().getShoppingCart(), ticket);
        if (isRemoved) {
            System.out.println("Ticket removed from cart successfully.");
        } else {
            System.out.println("Ticket not found in the cart.");
        }
    }

    /**
     * Clears all tickets from the current customer's shopping cart.
     */
    public void clearCart() {
        shoppingCartService.clearCart(customerService.getCurrentCustomer().getShoppingCart());
        System.out.println("Shopping cart has been cleared.");
    }

    /**
     * Checks out the current customer's shopping cart, marking tickets as sold, and returns the created Order.
     *
     * @return the Order created during checkout, or null if checkout failed
     */
    public Order checkout() {
        return shoppingCartService.checkout(customerService.getCurrentCustomer().getShoppingCart());
    }

    /**
     * Updates the total price of items in the current customer's shopping cart.
     */
    public void updateTotalPrice() {
        shoppingCartService.updateTotalPrice(customerService.getCurrentCustomer().getShoppingCart());
        System.out.println("Total price has been updated. Current total: $" + customerService.getCurrentCustomer().getShoppingCart().getTotalPrice());
    }

    /**
     * Retrieves the total price of the current customer's shopping cart.
     *
     * @return the total price of items in the shopping cart
     */
    public double getTotalPrice() {
        return shoppingCartService.getTotalPrice(customerService.getCurrentCustomer().getShoppingCart());
    }

    /**
     * Retrieves and displays the total price of the current customer's shopping cart.
     */
    public void printTotalPrice() {
        double totalPrice = getTotalPrice();  // Reuse the getTotalPrice() method
        System.out.println("Total price of the shopping cart: $" + totalPrice);
    }
}
