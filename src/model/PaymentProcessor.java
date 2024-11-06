package model;

public interface PaymentProcessor {
    boolean enterPaymentDetails(String cardNumber, int cvv, String cardOwner, String expirationDate);
    boolean processPayment(double totalPrice);
}
