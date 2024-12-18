package com.ticketsalescompany.model;

/**
 * Defines a contract for payment processing, allowing flexibility in how payments are processed.
 * Implementing classes, such as {@code BasicPaymentProcessor}, can provide specific logic for
 * entering payment details and processing payments. This interface supports different payment
 * processing methods or integrations with various payment gateways.
 */
public interface PaymentProcessor {

    /**
     * Enters and validates payment details, such as card number, CVV, card owner name, and expiration date.
     *
     * @param cardNumber The credit or debit card number.
     * @param cvv The card verification value, typically a 3-digit code on the back of the card.
     * @param cardOwner The name of the card owner as it appears on the card.
     * @param expirationDate The expiration date of the card in the format MM/YY.
     * @return {@code true} if the payment details are valid; {@code false} otherwise.
     */
    boolean enterPaymentDetails(String cardNumber, int cvv, String cardOwner, String expirationDate);

    /**
     * Processes the payment for the specified amount.
     *
     * @param totalPrice The total price of the transaction to be processed.
     * @return {@code true} if the payment is successful; {@code false} otherwise.
     */
    boolean processPayment(double totalPrice);
}
