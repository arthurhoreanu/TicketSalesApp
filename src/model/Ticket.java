package model;

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

    @Column(name = "event_id", nullable = false)
    private int eventId;

    @Column(name = "section_id", nullable = false)
    private int sectionId;

    @Column(name = "seat_id")
    private Integer seatId;

    @Column(name = "price", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type", nullable = false)
    private TicketType ticketType;

    @Column(name = "is_sold", nullable = false)
    private boolean isSold;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "purchaser_name")
    private String purchaserName;

    /**
     * Default constructor for JPA and serialization.
     */
    public Ticket() {}

    /**
     * Constructs a Ticket with the specified attributes.
     *
     * @param eventId     the ID of the associated event
     * @param sectionId   the ID of the section
     * @param seatId      the ID of the seat (nullable for general admission)
     * @param price       the price of the ticket
     * @param ticketType  the type of the ticket (STANDARD, VIP, EARLY_ACCESS, etc.)
     */
    public Ticket(int eventId, int sectionId, Integer seatId, double price, TicketType ticketType) {
        this.eventId = eventId;
        this.sectionId = sectionId;
        this.seatId = seatId;
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

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
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
                ", eventId=" + eventId +
                ", sectionId=" + sectionId +
                ", seatId=" + (seatId != null ? seatId : "null") +
                ", price=" + price +
                ", ticketType=" + ticketType +
                ", isSold=" + isSold +
                ", purchaseDate=" + purchaseDate +
                ", purchaserName='" + purchaserName + '\'' +
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
                String.valueOf(eventId),
                String.valueOf(sectionId),
                seatId != null ? String.valueOf(seatId) : "null",
                String.valueOf(price),
                ticketType.name(),
                String.valueOf(isSold),
                purchaseDate != null ? purchaseDate.toString() : "null",
                purchaserName != null ? purchaserName : "null"
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
        int eventId = Integer.parseInt(fields[1].trim());
        int sectionId = Integer.parseInt(fields[2].trim());
        Integer seatId = fields[3].trim().equals("null") ? null : Integer.parseInt(fields[3].trim());
        double price = Double.parseDouble(fields[4].trim());
        TicketType ticketType = TicketType.valueOf(fields[5].trim());
        boolean isSold = Boolean.parseBoolean(fields[6].trim());
        LocalDateTime purchaseDate = fields[7].trim().equals("null") ? null : LocalDateTime.parse(fields[7].trim());
        String purchaserName = fields[8].trim().equals("null") ? null : fields[8].trim();

        Ticket ticket = new Ticket(eventId, sectionId, seatId, price, ticketType);
        ticket.setTicketID(ticketID);
        ticket.setSold(isSold);
        ticket.setPurchaseDate(purchaseDate);
        ticket.setPurchaserName(purchaserName);
        return ticket;
    }
}