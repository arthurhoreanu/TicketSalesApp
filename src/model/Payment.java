package model;

import controller.Controller;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Payment {
    private int paymentID;
    private Cart cart;
    private Customer customer;
    private LocalDateTime paymentDate;
    private double paymentAmount;
    private String paymentMethod;
    private String paymentStatus; // e.g., PENDING, COMPLETED, FAILED
    private String currency;
    private String transactionID;

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

    // Getters and setters
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

}