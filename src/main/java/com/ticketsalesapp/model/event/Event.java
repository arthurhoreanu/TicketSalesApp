package main.java.com.ticketsalesapp.model.event;

import lombok.Getter;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.Identifiable;

import java.time.LocalDateTime;

/**
 * Represents an abstract event with a unique ID, name, description, schedule, venue ID, and status.
 */
public abstract class Event implements Identifiable {
    private int eventID;
    @Getter
    @Setter
    private String eventName;
    @Getter
    @Setter
    private String eventDescription;
    @Setter
    @Getter
    private LocalDateTime startDateTime;
    @Getter
    @Setter
    private LocalDateTime endDateTime;
    @Getter
    @Setter
    private int venueID;
    @Getter
    @Setter
    private EventStatus eventStatus;
    @Setter
    @Getter
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