package main.java.com.ticketsalesapp.service;

import lombok.RequiredArgsConstructor;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.ticket.Cart;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.event.Event;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Cart-related operations.
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final BaseRepository<Cart> cartBaseRepository;

    /**
     * Creates a new cart for a given customer and event.
     *
     * @param customer The customer for whom the cart is created.
     * @param event    The event associated with the cart.
     * @return The newly created Cart object.
     */
    public Cart createCart(Customer customer, Event event) {
        if (customer == null) {
            throw new ValidationException("Customer cannot be null.");
        }
        if (event == null) {
            throw new ValidationException("Event cannot be null.");
        }
        Cart cart = new Cart(customer, event);
        cartBaseRepository.create(cart);
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
        if (cart == null) {
            throw new ValidationException("Cart cannot be null.");
        }
        if (ticket == null) {
            throw new ValidationException("Ticket cannot be null.");
        }
        try {
            if (ticket.isSold()) {
                throw new IllegalArgumentException("Ticket is already sold.");
            }

            // Mark the ticket as reserved
            ticket.setSold(true);
            ticket.setCart(cart);
            cart.addTicket(ticket);

            updateTotalPrice(cart); // Update total price after adding a ticket
            cartBaseRepository.update(cart);
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
        if (cart == null) {
            throw new ValidationException("Cart cannot be null.");
        }
        if (ticket == null) {
            throw new ValidationException("Ticket cannot be null.");
        }
        try {
            cart.removeTicket(ticket);
            cartBaseRepository.update(cart);
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
        if (cart == null) {
            throw new ValidationException("Cart cannot be null.");
        }
        double totalPrice = cart.calculateTotalPrice();
        cart.setTotalPrice(totalPrice);
        cartBaseRepository.update(cart);
    }

    /**
     * Clears the cart by removing all tickets and resetting the total price.
     *
     * @param cart The cart to clear.
     */
    public void clearCart(Cart cart) {
        if (cart == null) {
            throw new ValidationException("Cart cannot be null.");
        }
        cart.clearCart();
        cart.setTotalPrice(0.0);
        cartBaseRepository.update(cart);
    }

    /**
     * Finalizes the cart by converting its contents into a purchase history.
     *
     * @param cart The cart to finalize.
     * @throws UnsupportedOperationException If `PurchaseHistoryService` is not yet implemented.
     */
    public void finalizeCart(Cart cart) {
        if (cart.isPaymentProcessed()) {
            throw new BusinessLogicException("Payment has already been processed for this cart.");
        }
        cart.setPaymentProcessed(true);
        clearCart(cart);
    }

    /**
     * Finds a cart by its ID.
     *
     * @param cartID The ID of the cart to find.
     * @return The cart if found, or null otherwise.
     */
    public Optional<Cart> findCartByID(int cartID) {
        return cartBaseRepository.read(cartID);
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

    /**
     * Simulates an online payment.
     *
     * @param cart           The cart associated with the payment.
     * @param cardNumber     The card number entered by the user.
     * @param cardholderName The name of the cardholder.
     * @param expiryMonth    The expiry month of the card (1-12).
     * @param expiryYear     The expiry year of the card (YYYY format).
     * @param cvv            The CVV code of the card.
     * @throws IllegalArgumentException If any of the input validation checks fail.
     */
    public void processPayment(Cart cart, String cardNumber, String cardholderName,
                               int expiryMonth, int expiryYear, String cvv) {
        validateCardDetails(cardNumber, cardholderName, expiryMonth, expiryYear, cvv);

        Customer customer = cart.getCustomer();
        double paymentAmount = cart.calculateTotalPrice();

        if (paymentAmount <= 0) {
            throw new BusinessLogicException("Cannot process payment. Total amount is invalid.");
        }

        for (Ticket ticket : cart.getTickets()) {
            ticket.markAsSold(customer);
            ticket.setCustomer(customer);
        }

        cart.setPaymentProcessed(true);
    }

    /**
     * Validates the card details provided by the user.
     *
     * @param cardNumber      The card number.
     * @param cardholderName  The name on the card.
     * @param expiryMonth     The expiry month (1-12).
     * @param expiryYear      The expiry year (YYYY format).
     * @param cvv             The CVV code (3-4 digits).
     * @throws IllegalArgumentException If validation fails.
     */
    private void validateCardDetails(String cardNumber, String cardholderName,
                                     int expiryMonth, int expiryYear, String cvv) {
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d{16}")) {
            throw new ValidationException("Invalid card number. Must be 16 digits.");
        }
        if (cardholderName == null || cardholderName.trim().isEmpty()) {
            throw new ValidationException("Cardholder name cannot be empty.");
        }
        if (expiryMonth < 1 || expiryMonth > 12) {
            throw new ValidationException("Invalid expiry month. Must be between 1 and 12.");
        }
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth cardExpiry = YearMonth.of(expiryYear, expiryMonth);
        if (cardExpiry.isBefore(currentYearMonth)) {
            throw new ValidationException("Card has expired.");
        }
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            throw new ValidationException("Invalid CVV. Must be 3 or 4 digits.");
        }
    }

}