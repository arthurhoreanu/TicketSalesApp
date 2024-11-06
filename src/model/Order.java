package model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private static int orderCounter = 1; //this would be nice to have
    private int orderID;
    private User user;
    private List<Ticket> tickets;
    private LocalDateTime orderDate;

    public Order(User user, List<Ticket> tickets) {
        //this.orderCounter = orderCounter;
        this.orderID = orderID;
        this.user = user;
        this.tickets = tickets;
        this.orderDate = orderDate;
    }

    public int getOrderID() {
        return orderID;
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

    //TODO method to implement the orderCounter needed

    // in service later I guess
    public void showProcessedOrder() {
        System.out.println("Order ID: " + orderID);
        System.out.println("User: " + user.getUsername());
        System.out.println("Order Date: " + orderDate);
        for (Ticket ticket : tickets) {
            System.out.println(" - Ticket ID: " + ticket.getTicketID() + ", Event: " + ticket.getEvent().getEventName());
        }
    }
//TODO solve the incremented order number problem!!! Check it out in the constructor, should we have a method to increment it? We have to convert it to string too

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", user=" + user + ", tickets=" + tickets + ", orderDate=" + orderDate + '}';
    }
}

