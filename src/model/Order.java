package model;

import java.time.LocalDateTime;
import java.util.List;

public class Order implements Identifiable {
    private static int orderCounter = 1;
    private int orderID;
    private User user;
    private List<Ticket> tickets;
    private LocalDateTime orderDate;
    private OrderStatus status; // New field to track order status

    public Order(User user, List<Ticket> tickets) {
        this.orderID = orderCounter++;
        this.user = user;
        this.tickets = tickets;
        this.orderDate = LocalDateTime.now(); // Sets the order date to the current time
        this.status = OrderStatus.PENDING; // Default status is PENDING
    }

    @Override
    public Integer getID() {
        return this.orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
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

    public boolean isCompleted() {
        return this.status == OrderStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", user=" + user +
                ", tickets=" + tickets +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }
}
