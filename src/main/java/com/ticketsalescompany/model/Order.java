package com.ticketsalescompany.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents an Order placed by a user, containing metadata like user ID, order date, and status.
 */
@Entity
@Table(name = "order") // "order" is a reserved keyword in SQL
public class Order implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderID;

    @Column(name = "user_id", nullable = false)
    private int userID;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    /**
     * Default constructor for JPA and serialization.
     */
    public Order() {}

    /**
     * Constructs an Order with specified attributes.
     *
     * @param orderID   the unique ID of the order
     * @param userID    the ID of the user who placed the order
     * @param orderDate the date and time the order was placed
     * @param status    the current status of the order
     */
    public Order(int orderID, int userID, LocalDateTime orderDate, OrderStatus status) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.status = status;
    }

    /**
     * Constructs an Order with default status as PENDING and orderDate as current time.
     *
     * @param userID the ID of the user who placed the order
     */
    public Order(int userID) {
        this(0, userID, LocalDateTime.now(), OrderStatus.PENDING);
    }

    @Override
    public Integer getID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", userID=" + userID +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }

    // CSV Methods
    public String toCsv() {
        return String.join(",",
                String.valueOf(orderID),
                String.valueOf(userID),
                orderDate.toString(),
                status.name()
        );
    }

    public static Order fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int orderID = Integer.parseInt(fields[0].trim());
        int userID = Integer.parseInt(fields[1].trim());
        LocalDateTime orderDate = LocalDateTime.parse(fields[2].trim());
        OrderStatus status = OrderStatus.valueOf(fields[3].trim());
        return new Order(orderID, userID, orderDate, status);
    }
}
