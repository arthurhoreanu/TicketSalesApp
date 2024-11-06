package model;

public interface PaymentProcessor {
    boolean enterPaymentDetails(String cardNumber, int cvv, String cardOwner, String expirationDate);
    boolean processPayment(double totalPrice);
}

//TODO think about the logic of the methods, convert their return to string ass well
