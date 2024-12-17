package model;

import javax.persistence.*;

/**
 * Represents a mapping between an Order and a Ticket.
 */
@Entity
@Table(name = "order_ticket")
public class OrderTicket implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderTicketID;

    @Column(name = "order_id", nullable = false)
    private int orderID;

    @Column(name = "ticket_id", nullable = false)
    private int ticketID;

    /**
     * Default constructor for JPA and serialization.
     */
    public OrderTicket() {}

    /**
     * Constructs an OrderTicket with the specified attributes.
     *
     * @param orderTicketID the unique ID of the OrderTicket (optional if auto-generated)
     * @param orderID       the ID of the associated order
     * @param ticketID      the ID of the associated ticket
     */
    public OrderTicket(int orderTicketID, int orderID, int ticketID) {
        this.orderTicketID = orderTicketID;
        this.orderID = orderID;
        this.ticketID = ticketID;
    }

    /**
     * Constructs an OrderTicket without specifying an ID (used when the ID is auto-generated).
     *
     * @param orderID  the ID of the associated order
     * @param ticketID the ID of the associated ticket
     */
    public OrderTicket(int orderID, int ticketID) {
        this.orderID = orderID;
        this.ticketID = ticketID;
    }

    @Override
    public Integer getID() {
        return orderTicketID;
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
                "orderTicketID=" + orderTicketID +
                ", orderID=" + orderID +
                ", ticketID=" + ticketID +
                '}';
    }

    // CSV Methods
    public String toCsv() {
        return String.join(",",
                String.valueOf(orderTicketID),
                String.valueOf(orderID),
                String.valueOf(ticketID)
        );
    }

    public static OrderTicket fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int orderTicketID = Integer.parseInt(fields[0].trim());
        int orderID = Integer.parseInt(fields[1].trim());
        int ticketID = Integer.parseInt(fields[2].trim());
        return new OrderTicket(orderTicketID, orderID, ticketID);
    }
}
