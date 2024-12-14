package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Represents a ticket for an event, including details about the event, seat, price, purchaser, and sale status.
 */
public class Ticket implements Identifiable {
    private int ticketID;
    private int eventID;
    private int sectionID;
    private int seatID;
    private double price;
    private LocalDateTime purchaseDate;
    private String purchaserName;
    private TicketType ticketType;
    private boolean isSold;

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Ticket with the specified attributes.
     *
     * @param ticketID   the unique ID of the ticket
     * @param eventID    the ID of the associated event
     * @param sectionID  the ID of the associated section
     * @param seatID     the ID of the associated seat
     * @param price      the price of the ticket
     * @param ticketType the type of the ticket (STANDARD, VIP, EARLY_ACCESS)
     */
    public Ticket(int ticketID, int eventID, int sectionID, int seatID, double price, TicketType ticketType) {
        this.ticketID = ticketID;
        this.eventID = eventID;
        this.sectionID = sectionID;
        this.seatID = seatID;
        this.price = price;
        this.ticketType = ticketType;
        this.isSold = false; // the ticket is initially not sold
    }

    /**
     * Returns a string representation of the ticket, including ID, event, section, seat, price, and sale details.
     *
     * @return a string representing the ticket's details
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID=" + ticketID +
                ", eventID=" + eventID +
                ", sectionID=" + sectionID +
                ", seatID=" + seatID +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                ", purchaserName='" + purchaserName + '\'' +
                ", ticketType=" + ticketType +
                ", isSold=" + isSold +
                '}';
    }

    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(ticketID),
                String.valueOf(eventID),
                String.valueOf(sectionID),
                String.valueOf(seatID),
                String.valueOf(price),
                ticketType.name(),
                String.valueOf(isSold),
                purchaserName == null ? "null" : purchaserName,
                purchaseDate == null ? "null" : purchaseDate.toString()
        );
    }

    public static Ticket fromCSV(String csvLine) {
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

        Ticket ticket = new Ticket(ticketID, eventID, sectionID, seatID, price, ticketType);
        ticket.setSold(isSold);
        ticket.setPurchaserName(purchaserName);
        ticket.setPurchaseDate(purchaseDate);
        return ticket;
    }

    @Override
    public Integer getID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public int getSeatID() {
        return seatID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public double getPrice() {
        return price;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        this.isSold = sold;
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

    // Dynamically fetch related entities
    public Event getEvent() {
        return controller.findEventByID(eventID);
    }

    public Section getSection() {
        return controller.findSectionByID(sectionID);
    }

    public Seat getSeat() {
        return controller.findSeatByID(seatID);
    }
    //todo in service
    /**
     * /**
     * Marks the ticket as sold and records the purchaser's name and purchase date.
     *
     * @param purchaserName the name of the purchaser
     */
    public void markAsSold(String purchaserName) {
        this.isSold = true;
        this.purchaserName = purchaserName;
        this.purchaseDate = LocalDateTime.now();
    }
}
