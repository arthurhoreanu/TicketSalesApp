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
        paymentService.processPayment(cart, cardNumber, cardholderName, expiryMonth, expiryYear, cvv, currency);
        System.out.println("Payment processed successfully");
    }
}
