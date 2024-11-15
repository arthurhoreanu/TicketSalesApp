package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an Order placed by a User, containing multiple Tickets.
 * Each Order has a unique ID, associated user, list of tickets, date of order, and order status.
 */
public class Order implements Identifiable {
    private static int orderCounter = 1; // Counter for generating unique order IDs
    private int orderID;
    private User user;
    private List<Ticket> tickets;
    private LocalDateTime orderDate;
    private OrderStatus status;

    public static Order fromCsvFormat(String csvLine) {
        return null;
    }

    @Override
    public String toCsvFormat() {
        return "";
    }

    /**
     * Constructs an Order with a specified user and list of tickets.
     * Initializes the order ID, sets the order date to the current time, and defaults the status to PENDING.
     *
     * @param user    the user who placed the order
     * @param tickets the list of tickets included in the order
     */
    public Order(User user, List<Ticket> tickets) {
        this.orderID = orderCounter++;
        this.user = user;
        this.tickets = tickets;
        this.orderDate = LocalDateTime.now(); // Sets the order date to the current time
        this.status = OrderStatus.PENDING; // Default status is PENDING
    }

    /**
     * Retrieves the unique ID of the order.
     *
     * @return the order ID
     */
    @Override
    public Integer getID() {
        return this.orderID;
    }

    /**
     * Sets the unique ID of the order.
     *
     * @param orderID the new order ID
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     * Retrieves the user who placed the order.
     *
     * @return the user associated with this order
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who placed the order.
     *
     * @param user the user associated with this order
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieves the list of tickets in the order.
     *
     * @return the list of tickets included in this order
     */
    public List<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Sets the list of tickets in the order.
     *
     * @param tickets the new list of tickets for this order
     */
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Sets the status of the order.
     *
     * @param status the new status for this order
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Checks if the order is completed.
     *
     * @return true if the order status is COMPLETED, otherwise false
     */
    public boolean isCompleted() {
        return this.status == OrderStatus.COMPLETED;
    }

    /**
     * Returns a string representation of the Order, including its ID, user, tickets, order date, and status.
     *
     * @return a string representation of the order
     */
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
