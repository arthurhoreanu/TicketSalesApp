package model;

import java.time.LocalDateTime;

public class Order implements Identifiable {
    private int orderID;
    private int userID; // ID of the user who placed the order
    private LocalDateTime orderDate;
    private OrderStatus status;

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
     * @param orderID the unique ID of the order
     * @param userID  the ID of the user who placed the order
     */
    public Order(int orderID, int userID) {
        this(orderID, userID, LocalDateTime.now(), OrderStatus.PENDING);
    }

    @Override
    public Integer getID() {
        return orderID;
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

    /**
     * Converts the Order to a CSV-formatted string.
     *
     * @return the CSV string
     */
    public String toCsv() {
        return String.join(",",
                String.valueOf(orderID),
                String.valueOf(userID),
                orderDate.toString(),
                status.name()
        );
    }

    /**
     * Creates an Order object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string
     * @return the deserialized Order object
     */
    public static Order fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int orderID = Integer.parseInt(fields[0].trim());
        int userID = Integer.parseInt(fields[1].trim());
        LocalDateTime orderDate = LocalDateTime.parse(fields[2].trim());
        OrderStatus status = OrderStatus.valueOf(fields[3].trim());
        return new Order(orderID, userID, orderDate, status);
    }
}
