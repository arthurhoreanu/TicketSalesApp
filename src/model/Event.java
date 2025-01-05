package model;

import java.time.LocalDateTime;

/**
 * Represents an abstract event with a unique ID, name, description, schedule, venue ID, and status.
 */
public abstract class Event implements Identifiable {
    private int eventID;
    private String eventName;
    private String eventDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int venueID;
    private EventStatus eventStatus;
    private double basePrice;

    public Event() {}

    public Event(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                 LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.venueID = venueID;
        this.eventStatus = eventStatus;
    }

    @Override
    public Integer getID() {
        return eventID;
    }

    @Override
    public void setID(int eventID) {
        this.eventID = eventID;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * Returns a string representation of the event.
     * @return a string representing the event's details.
     */
    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", venueID=" + venueID +
                ", eventStatus=" + eventStatus +
                '}';
    }
}