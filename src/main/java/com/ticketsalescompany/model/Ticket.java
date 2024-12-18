package com.ticketsalescompany.model;

import com.ticketsalescompany.controller.Controller;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a ticket for an event, including details about the event, seat, price, purchaser, and sale status.
 */
@Entity
@Table(name = "ticket")
public class Ticket implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "purchaser_name")
    private String purchaserName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type", nullable = false)
    private TicketType ticketType;

    @Column(name = "is_sold", nullable = false)
    private boolean isSold;

    /**
     * Default constructor for JPA and serialization.
     */
    public Ticket() {}

    static Controller controller = ControllerProvider.getController();


    /**
     * Constructs a Ticket with the specified attributes.
     *
     * @param ticketID    the unique ID of the ticket
     * @param event       the Event object associated with the ticket
     * @param section     the Section object where the ticket is located
     * @param seat        the Seat object assigned to the ticket
     * @param price       the price of the ticket
     * @param ticketType  the type of the ticket (STANDARD, VIP, EARLY_ACCESS)
     */
    public Ticket(int ticketID, Event event, Section section, Seat seat, double price, TicketType ticketType) {
        this.ticketID = ticketID;
        this.event = event;
        this.section = section;
        this.seat = seat;
        this.price = price;
        this.ticketType = ticketType;
        this.isSold = false; // Initially not sold
    }

    /**
     * Constructs a Ticket without an ID, for cases where the ID is generated automatically.
     *
     * @param event       the Event object associated with the ticket
     * @param section     the Section object where the ticket is located
     * @param seat        the Seat object assigned to the ticket
     * @param price       the price of the ticket
     * @param ticketType  the type of the ticket (STANDARD, VIP, EARLY_ACCESS)
     */
    public Ticket(Event event, Section section, Seat seat, double price, TicketType ticketType) {
        this.event = event;
        this.section = section;
        this.seat = seat;
        this.price = price;
        this.ticketType = ticketType;
        this.isSold = false; // Initially not sold
    }

    @Override
    public Integer getID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
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

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    /**
     * Marks the ticket as sold, assigning the purchaser name and purchase date.
     *
     * @param purchaserName the name of the purchaser
     */
    public void markAsSold(String purchaserName) {
        this.isSold = true;
        this.purchaserName = purchaserName;
        this.purchaseDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID=" + ticketID +
                ", event=" + (event != null ? event.getID() : "null") +
                ", section=" + (section != null ? section.getID() : "null") +
                ", seat=" + (seat != null ? seat.getID() : "null") +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                ", purchaserName='" + purchaserName + '\'' +
                ", ticketType=" + ticketType +
                ", isSold=" + isSold +
                '}';
    }

    // CSV Methods
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(ticketID),
                event != null ? String.valueOf(event.getID()) : "null",
                section != null ? String.valueOf(section.getID()) : "null",
                seat != null ? String.valueOf(seat.getID()) : "null",
                String.valueOf(price),
                ticketType.name(),
                String.valueOf(isSold),
                purchaserName == null ? "null" : purchaserName,
                purchaseDate == null ? "null" : purchaseDate.toString()
        );
    }

    public static Ticket fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int ticketID = Integer.parseInt(fields[0].trim());
        int eventID = Integer.parseInt(fields[1].trim());
        int sectionID = Integer.parseInt(fields[2].trim());
        int seatID = Integer.parseInt(fields[3].trim());
        double price = Double.parseDouble(fields[4].trim());
        TicketType ticketType = TicketType.valueOf(fields[5].trim());
        boolean isSold = Boolean.parseBoolean(fields[6].trim());
        String purchaserName = fields[7].trim().equals("null") ? null : fields[7].trim();
        LocalDateTime purchaseDate = fields[8].trim().equals("null") ? null : LocalDateTime.parse(fields[8].trim());

        Event event = controller.findEventByID(eventID);
        Section section = controller.findSectionByID(sectionID);
        Seat seat = controller.findSeatByID(seatID);

        Ticket ticket = new Ticket(ticketID, event, section, seat, price, ticketType);
        ticket.setSold(isSold);
        ticket.setPurchaserName(purchaserName);
        ticket.setPurchaseDate(purchaseDate);
        return ticket;
    }
}
