package model;

import javax.persistence.*;

/**
 * Represents a mapping between an Order and a Ticket.
 */
@Entity
@Table(name = "order_ticket")
public class OrderTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_id", nullable = false)
    private int orderID;

    @Column(name = "ticket_id", nullable = false)
    private int ticketID;

    /**
     * Default constructor for JPA and serialization.
     */
    public OrderTicket() {}

    /**
     * Constructs an OrderTicket with specified attributes.
     *
     * @param orderID  the ID of the associated order
     * @param ticketID the ID of the associated ticket
     */
    public OrderTicket(int orderID, int ticketID) {
        this.orderID = orderID;
        this.ticketID = ticketID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    @Override
    public String toString() {
        return "OrderTicket{" +
                "id=" + id +
                ", orderID=" + orderID +
                ", ticketID=" + ticketID +
                '}';
    }

    // CSV Methods
    public String toCsv() {
        return String.join(",",
                String.valueOf(orderID),
                String.valueOf(ticketID)
        );
    }

    public static OrderTicket fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int orderID = Integer.parseInt(fields[0].trim());
        int ticketID = Integer.parseInt(fields[1].trim());
        return new OrderTicket(orderID, ticketID);
    }
}
