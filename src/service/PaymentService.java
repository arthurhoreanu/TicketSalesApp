package service;

import model.Cart;
import model.Customer;
import model.Payment;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for managing payment operations.
 */
public class PaymentService {

    /**
     * Simulates an online payment.
     *
     * @param cart           The cart associated with the payment.
     * @param cardNumber     The card number entered by the user.
     * @param cardholderName The name of the cardholder.
     * @param expiryMonth    The expiry month of the card (1-12).
     * @param expiryYear     The expiry year of the card (YYYY format).
     * @param cvv            The CVV code of the card.
     * @param currency       The currency for the payment.
     * @throws IllegalArgumentException If any of the input validation checks fail.
     */
    public void processPayment(Cart cart, String cardNumber, String cardholderName,
                               int expiryMonth, int expiryYear, String cvv, String currency) {
        validateCardDetails(cardNumber, cardholderName, expiryMonth, expiryYear, cvv);

        Customer customer = cart.getCustomer();
        double paymentAmount = cart.calculateTotalPrice();

        if (paymentAmount <= 0) {
            throw new IllegalArgumentException("Cannot process payment. Total amount is invalid.");
        }

        // Simulating a payment gateway transaction ID
        String transactionID = UUID.randomUUID().toString();

        // Create a new Payment object
        Payment payment = new Payment(
                cart,
                customer,
                LocalDateTime.now(),
                paymentAmount,
                "CARD", // Payment method
                "COMPLETED", // Status can be adjusted based on gateway response
                currency,
                transactionID
        );

        // Link the payment to the cart and mark as processed
        cart.setPaymentProcessed(true);
        cart.setPayment(payment);
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
            throw new IllegalArgumentException("Invalid card number. Must be 16 digits.");
        }
        if (cardholderName == null || cardholderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Cardholder name cannot be empty.");
        }
        if (expiryMonth < 1 || expiryMonth > 12) {
            throw new IllegalArgumentException("Invalid expiry month. Must be between 1 and 12.");
        }
        if (expiryYear < LocalDateTime.now().getYear() ||
                (expiryYear == LocalDateTime.now().getYear() && expiryMonth < LocalDateTime.now().getMonthValue())) {
            throw new IllegalArgumentException("Card has expired.");
        }
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            throw new IllegalArgumentException("Invalid CVV. Must be 3 or 4 digits.");
        }
    }
}