package model;

import java.time.LocalDateTime;

/**
 * Represents an abstract event with a unique ID, name, description, schedule, venue, and status.
 */
public abstract class Event implements Identifiable {
    private int eventID;
    private String eventName;
    private String eventDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Venue venue;
    private EventStatus eventStatus;

    /**
     * Constructs an Event with the specified details.
     * @param eventID          the unique ID of the event
     * @param eventName        the name of the event
     * @param eventDescription a description of the event
     * @param startDateTime    the start date and time of the event
     * @param endDateTime      the end date and time of the event
     * @param venue            the venue where the event takes place
     * @param eventStatus      the current status of the event (SCHEDULED, CANCELLED, COMPLETED)
     */
    public Event(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                 LocalDateTime endDateTime, Venue venue, EventStatus eventStatus) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.venue = venue;
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

    /**
     * Gets the name of the event.
     * @return the name of the event
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets a new name for the event.
     * @param eventName the new name of the event
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the description of the event.
     * @return the description of the event
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets a new description for the event.
     * @param eventDescription the new description of the event
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Gets the start date and time of the event.
     * @return the start date and time of the event
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets a new start date and time for the event.
     * @param startDateTime the new start date and time of the event
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the end date and time of the event.
     * @return the end date and time of the event
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets a new end date and time for the event.
     * @param endDateTime the new end date and time of the event
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the venue where the event is held.
     * @return the venue of the event
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * Sets a new venue for the event.
     * @param venue the new venue for the event
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * Gets the current status of the event (SCHEDULED, CANCELLED, COMPLETED).
     * @return the status of the event
     */
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    /**
     * Sets a new status for the event.
     * @param eventStatus the new status of the event
     */
    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * Returns a string representation of the event, including its ID, name, description, schedule, venue, and status.
     * @return a string representing the event's details
     */
    @Override
    public String toString() {
        return "{" + eventID + ", eventName=" + eventName + ", eventDescription=" + eventDescription +
                ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", venue=" + venue +
                ", eventStatus=" + eventStatus + '}';
    }
}
