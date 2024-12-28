package service;

import model.Cart;
import model.Customer;
import model.Event;
import model.Ticket;
import repository.FileRepository;
import repository.IRepository;

import java.util.List;

/**
 * Service class for managing Cart-related operations.
 */
public class CartService {

    // TODO db
    private final IRepository<Cart> cartRepository;
    private final FileRepository<Cart> cartFileRepository;

    /**
     * Constructs a CartService with dependencies for managing carts.
     *
     * @param cartRepository the repository for managing Cart persistence.
     */
    public CartService(IRepository<Cart> cartRepository) {
        this.cartRepository = cartRepository;
        this.cartFileRepository = new FileRepository<>("src/repository/data/carts.csv", Cart::fromCsv);
        syncFromCsv();
    }

    /**
     * Synchronizes carts from the CSV file into the main repository.
     */
    private void syncFromCsv() {
        List<Cart> carts = cartFileRepository.getAll();
        for (Cart cart : carts) {
            cartRepository.create(cart);
        }
    }

    /**
     * Creates a new cart for a given customer and event.
     *
     * @param customer The customer for whom the cart is created.
     * @param event    The event associated with the cart.
     * @return The newly created Cart object.
     */
    public Cart createCart(Customer customer, Event event) {
        Cart cart = new Cart(customer, event);
        cartRepository.create(cart);
        cartFileRepository.create(cart);
        return cart;
    }

    /**
     * Adds a ticket to the cart, ensuring all tickets belong to the same event.
     *
     * @param cart   The cart to which the ticket is added.
     * @param ticket The ticket to add to the cart.
     * @return true if the ticket was successfully added, false otherwise.
     */
    public boolean addTicketToCart(Cart cart, Ticket ticket) {
        try {
            cart.addTicket(ticket);
            cartRepository.update(cart);
            cartFileRepository.update(cart);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a ticket from the cart.
     *
     * @param cart   The cart from which the ticket is removed.
     * @param ticket The ticket to remove.
     * @return true if the ticket was successfully removed, false otherwise.
     */
    public boolean removeTicketFromCart(Cart cart, Ticket ticket) {
        try {
            cart.removeTicket(ticket);
            cartRepository.update(cart);
            cartFileRepository.update(cart);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the total price of the cart based on its tickets.
     *
     * @param cart The cart to update.
     */
    private void updateTotalPrice(Cart cart) {
        double totalPrice = cart.calculateTotalPrice();
        cart.setTotalPrice(totalPrice);
        cartRepository.update(cart);
        cartFileRepository.update(cart);
    }

    /**
     * Clears the cart by removing all tickets and resetting the total price.
     *
     * @param cart The cart to clear.
     */
    public void clearCart(Cart cart) {
        cart.clearCart();
        cart.setTotalPrice(0.0);
        cartRepository.update(cart);
        cartFileRepository.update(cart);
    }

    /**
     * Finalizes the cart by converting its contents into a purchase history.
     *
     * @param cart The cart to finalize.
     * @throws UnsupportedOperationException If `PurchaseHistoryService` is not yet implemented.
     */
    public void finalizeCart(Cart cart) {
        if (cart.isPaymentProcessed()) {
            throw new IllegalStateException("Payment has already been processed for this cart.");
        }
        // Placeholder for PurchaseHistoryService interaction
        // PurchaseHistoryService.createPurchaseHistory(cart);
        cart.setPaymentProcessed(true);
        clearCart(cart);
    }

    /**
     * Finds a cart by its ID.
     *
     * @param cartID The ID of the cart to find.
     * @return The cart if found, or null otherwise.
     */
    public Cart findCartByID(int cartID) {
        return cartRepository.read(cartID);
    }

    /**
     * Retrieves all tickets in a given cart.
     *
     * @param cart The cart for which to retrieve tickets.
     * @return A list of tickets in the cart.
     */
    public List<Ticket> getTicketsInCart(Cart cart) {
        return cart.getTickets();
    }
}