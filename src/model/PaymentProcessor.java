// TODO JavaDocs

package model;

public interface PaymentProcessor {
    boolean enterPaymentDetails(String cardNumber, int cvv, String cardOwner, String expirationDate);

    boolean processPayment(double totalPrice);
}
/*
The PaymentProcessor interface allows you to define a contract for payment processing without
specifying how it's done. This gives you flexibility because you can create multiple implementations
of PaymentProcessor (like BasicPaymentProcessor) to handle different payment processing methods or
to work with different payment gateways.
*/

