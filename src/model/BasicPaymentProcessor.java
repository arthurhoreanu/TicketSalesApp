package model;

public class BasicPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean enterPaymentDetails(String cardNumber, int cvv, String cardOwner, String expirationDate) {
        // Here you would add actual validation logic. For now, we'll simulate success.

        // Example validation: Check that the card number length is valid.
        if (cardNumber.length() != 16 || cvv < 100 || cvv > 999) {
            return false; // Invalid details
        }

        // Simulate successful entry of valid details
        return true;
    }

    @Override
    public boolean processPayment(double totalPrice) {
        // Here you would connect to a payment gateway or processor.

        // For simulation purposes, assume any payment over a certain amount fails.
        if (totalPrice > 1000) { // Simulate a limit check for demonstration
            return false;
        }

        // Payment succeeds
        return true;
    }
}
/*
BasicPaymentProcessor is a simple, mock implementation of PaymentProcessor for testing purposes.
It provides the functionality to "process payments" without needing real-world details like
secure connections to banking networks. Instead, it contains simulated logic, such as checking
that card numbers are of a certain length or limiting the payment amount.




*/
