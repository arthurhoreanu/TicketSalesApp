package model;

import controller.Controller;

import javax.persistence.MappedSuperclass;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
/**
 * Represents an abstract event with a unique ID, name, description, schedule, venue ID, and status.
 */
@MappedSuperclass
public abstract class Event implements Identifiable {
    private int eventID;
    private String eventName;
    private String eventDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int venueID; // Foreign key to Venue
    private EventStatus eventStatus;
    static Controller controller = ControllerProvider.getController();

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    protected Event() {}

    /**
     * Constructs an Event with the specified details.
     * @param eventID          the unique ID of the event
     * @param eventName        the name of the event
     * @param eventDescription a description of the event
     * @param startDateTime    the start date and time of the event
     * @param endDateTime      the end date and time of the event
     * @param venueID          the ID of the venue where the event takes place
     * @param eventStatus      the current status of the event (SCHEDULED, CANCELLED, COMPLETED)
     */
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

    /**
     * Gets the unique ID of the event.
     * @return the event ID
     */
    @Override
    public Integer getID() {
        return this.eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public int getVenueID() {
        return venueID;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
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