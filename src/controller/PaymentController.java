package controller;

import model.Cart;
import service.PaymentService;

public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void processPayment(Cart cart, String cardNumber, String cardholderName,
                               int expiryMonth, int expiryYear, String cvv, String currency) {
        try {
            paymentService.processPayment(cart, cardNumber, cardholderName, expiryMonth, expiryYear, cvv, currency);
            System.out.println("Payment processed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Payment failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during payment processing: " + e.getMessage());
        }
    }
}
