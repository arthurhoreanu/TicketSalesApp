package model;

import java.time.LocalDateTime;

public class Ticket implements Identifiable {
    private int ticketID;
    private Event event;
    private Section section;
    private Seat seat;
    private double price;
    private LocalDateTime purchaseDate;
    private String purchaserName;
    private TicketType ticketType;
    private boolean isSold;

    public Ticket(int ticketID, Event event, Section section, Seat seat, double price, TicketType ticketType) {
        this.ticketID = ticketID;
        this.event = event;
        this.section = section;
        this.seat = seat;
        this.price = price;
        this.ticketType = ticketType;
        this.isSold = false; //the ticket is not sold initially
    }

    @Override
    public Integer getID() {
        return this.ticketID;
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

    public void setPrice(double price) {
        this.price = price;
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

    // In Ticket class
    public int getEventId() {
        return event.getID(); // Assuming `event` is an `Event` object in Ticket
    }


    //TODO why not
    public void markAsSold(String purchaserName) {
        this.isSold = true;
        this.purchaserName = purchaserName;
        this.purchaseDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Ticket{" + "ticketID=" + ticketID + ", event=" + event + ", section=" + section + ", seat=" + seat + ", price=" + price + ", purchaseDate=" + purchaseDate + ", purchaserName='" + purchaserName + '\'' + ", ticketType=" + ticketType + ", isSold=" + isSold + '}';
    }
}
