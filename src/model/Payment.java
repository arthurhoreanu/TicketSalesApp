package model;

import controller.Controller;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "payment_amount", nullable = false)
    private double paymentAmount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus; // e.g., PENDING, COMPLETED, FAILED

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionID;

    static Controller controller = ControllerProvider.getController();


    public Payment() {}

    public Payment(Cart cart, Customer customer, LocalDateTime paymentDate, double paymentAmount,
                   String paymentMethod, String paymentStatus, String currency, String transactionID) {
        if (cart == null || customer == null) {
            throw new IllegalArgumentException("Cart and Customer cannot be null.");
        }
        this.cart = cart;
        this.customer = customer;
        this.paymentDate = paymentDate != null ? paymentDate : LocalDateTime.now();
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.currency = currency;
        this.transactionID = transactionID;
    }

    @Override
    public Integer getID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String toCsv() {
        return paymentID + "," +
                (cart != null ? cart.getID() : "null") + "," +
                (customer != null ? customer.getID() : "null") + "," +
                paymentDate + "," +
                paymentAmount + "," +
                paymentMethod + "," +
                paymentStatus + "," +
                currency + "," +
                transactionID;
    }

    public static Payment fromCsv(String csvLine, Controller controller) {
        String[] fields = csvLine.split(",");
        int paymentID = Integer.parseInt(fields[0].trim());
        Cart cart = controller.findCartByID(Integer.parseInt(fields[1].trim()));
        Customer customer = controller.findCustomerByID(Integer.parseInt(fields[2].trim()));
        LocalDateTime paymentDate = LocalDateTime.parse(fields[3].trim());
        double paymentAmount = Double.parseDouble(fields[4].trim());
        String paymentMethod = fields[5].trim();
        String paymentStatus = fields[6].trim();
        String currency = fields[7].trim();
        String transactionID = fields[8].trim();

        Payment payment = new Payment(cart, customer, paymentDate, paymentAmount, paymentMethod, paymentStatus, currency, transactionID);
        payment.setPaymentID(paymentID);

        return payment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", cartID=" + (cart != null ? cart.getID() : "null") +
                ", customerID=" + (customer != null ? customer.getID() : "null") +
                ", paymentDate=" + paymentDate +
                ", paymentAmount=" + paymentAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", currency='" + currency + '\'' +
                ", transactionID='" + transactionID + '\'' +
                '}';
    }
}
