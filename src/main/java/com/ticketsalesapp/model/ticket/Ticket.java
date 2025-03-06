package main.java.com.ticketsalesapp.model.ticket;

import main.java.com.ticketsalesapp.controller.ApplicationController;
import main.java.com.ticketsalesapp.model.*;
import main.java.com.ticketsalesapp.model.event.Event;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.venue.Seat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a ticket for an event, including details about the event, seat, price, purchaser, and sale status.
 */
@Entity
@Table(name = "ticket")
public class Ticket implements Identifiable {

    @Id
    @Column(name = "ticket_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event; // Abstract class; can be Concert or SportsEvent

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_id")
    private Seat seat; // Nullable for general admission

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "price", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type", nullable = false)
    private TicketType ticketType;

    @Column(name = "is_sold", nullable = false)
    private boolean isSold;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    static ApplicationController applicationController = ControllerProvider.getController();

    /**
     * Default constructor for JPA and serialization.
     */
    public Ticket() {}

    /**
     * Constructs a Ticket with the specified attributes.
     *
     * @param event       the event associated with the ticket
     * @param seat        the seat associated with the ticket (nullable for general admission)
     * @param customer    the customer purchasing the ticket
     * @param price       the price of the ticket
     * @param ticketType  the type of the ticket (e.g., STANDARD, VIP)
     */
    public Ticket(int ticketID, Event event, Seat seat, Customer customer, double price, TicketType ticketType) {
        this.ticketID = ticketID;
        this.event = event;
        this.seat = seat;
        this.customer = customer;
        this.price = price;
        this.ticketType = ticketType;
        this.isSold = false; // Initially not sold
    }

    @Override
    public Integer getID() {
        return ticketID;
    }

    @Override
    public void setID(int ticketID) {
        this.ticketID = ticketID;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /**
     * Marks the ticket as sold, assigning the purchase date.
     */
    public void markAsSold(Customer customer) {
        this.isSold = true;
        this.purchaseDate = LocalDateTime.now();
        this.customer = customer;
        seat.setReserved(true);
    }

    public void adjustPrice(double percentage) {
        if (percentage == 0) {
            return;
        }
        if (percentage < -100) {
            throw new IllegalArgumentException("Reduction percentage cannot exceed 100%.");
        }
        this.price *= (1 + percentage / 100);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID=" + ticketID +
                ", event=" + (event != null ? event.getID() : "null") +
                ", seat=" + (seat != null ? seat.getID() : "null") +
                ", customer=" + (customer != null ? customer.getID() : "null") +
                ", price=" + price +
                ", ticketType=" + ticketType +
                ", isSold=" + isSold +
                ", purchaseDate=" + purchaseDate +
                '}';
    }

    // CSV Methods
    /**
     * Converts the Ticket object into a CSV representation.
     *
     * @return A comma-separated string representing the ticket.
     */
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(ticketID),
                event != null ? String.valueOf(event.getID()) : "null",
                seat != null ? String.valueOf(seat.getID()) : "null",
                customer != null ? String.valueOf(customer.getID()) : "null",
                String.valueOf(price),
                ticketType.name(),
                String.valueOf(isSold),
                purchaseDate != null ? purchaseDate.toString() : "null"
        );
    }

    /**
     * Creates a Ticket object from a CSV string.
     *
     * @param csvLine The CSV string.
     * @return A Ticket object.
     */
    public static Ticket fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int ticketID = Integer.parseInt(fields[0].trim());
        int eventID = Integer.parseInt(fields[1].trim());
        Integer seatID = fields[2].trim().equals("null") ? null : Integer.parseInt(fields[2].trim());
        int customerID = Integer.parseInt(fields[3].trim());
        double price = Double.parseDouble(fields[4].trim());
        TicketType ticketType = TicketType.valueOf(fields[5].trim());
        boolean isSold = Boolean.parseBoolean(fields[6].trim());
        LocalDateTime purchaseDate = fields[7].trim().equals("null") ? null : LocalDateTime.parse(fields[7].trim());

        Event event = applicationController.findEventByID(eventID);
        Seat seat = seatID != null ? applicationController.findSeatByID(seatID) : null;
        Customer customer = applicationController.findCustomerByID(customerID);

        Ticket ticket = new Ticket(ticketID, event, seat, customer, price, ticketType);
        ticket.setSold(isSold);
        ticket.setPurchaseDate(purchaseDate);

        return ticket;
    }
}