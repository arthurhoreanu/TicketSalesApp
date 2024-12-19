package model;

/**
 * BasicPaymentProcessor is a simple, mock implementation of the PaymentProcessor interface for testing purposes.
 * It provides functionality to simulate payment processing without needing real-world payment details
 * The class contains simulated logic for card validation
 * and payment limits.
 */
public class BasicPaymentProcessor implements PaymentProcessor {

    /**
     * Simulates entering payment details and validates them.
     * Checks that the card number has 16 digits and the CVV is a 3-digit number.
     *
     * @param cardNumber      the credit card number as a 16-digit string
     * @param cvv             the CVV code as a 3-digit integer
     * @param cardOwner       the name of the card owner
     * @param expirationDate  the expiration date of the card in the format MM/YY
     * @return true if the payment details are valid; false otherwise
     */
    @Override
    public boolean enterPaymentDetails(String cardNumber, int cvv, String cardOwner, String expirationDate) {
        // Example validation: Check that the card number length is valid.
        if (cardNumber.length() != 16 || cvv < 100 || cvv > 999) {
            return false; // Invalid details
        }

        // Simulate successful entry of valid details
        return true;
    }

    /**
     * Simulates processing a payment.
     * For demonstration, it fails payments over a certain amount threshold (e.g., $1000).
     *
     * @param totalPrice the total amount to be charged
     * @return true if the payment is successful; false if it fails due to amount limit
     */
    @Override
    public boolean processPayment(double totalPrice) {
        // For simulation purposes, assume any payment over a certain amount fails.
        if (totalPrice > 1000) { // Simulate a limit check for demonstration
            return false;
        }

        // Payment succeeds
        return true;
    }
}
