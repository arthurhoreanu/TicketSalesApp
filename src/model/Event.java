package model;

import controller.Controller;
//todo test ana are mere

import java.sql.ResultSet;
import java.sql.SQLException;
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
    private int venueID; // Foreign key to Venue
    private EventStatus eventStatus;

    static Controller controller = ControllerProvider.getController();

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
     * Creates an Event object from a ResultSet.
     * @param rs the ResultSet containing Event data.
     * @return an Event object populated with data from the ResultSet.
     * @throws SQLException if an SQL error occurs.
     */
    public static Event fromDB(ResultSet rs) throws SQLException {
        int id = rs.getInt("eventID");
        String type = rs.getString("eventType").trim();
        String eventName = rs.getString("eventName").trim();
        String eventDescription = rs.getString("eventDescription").trim();
        LocalDateTime startDateTime = rs.getTimestamp("startDateTime").toLocalDateTime();
        LocalDateTime endDateTime = rs.getTimestamp("endDateTime").toLocalDateTime();
        int venueID = rs.getInt("venueID");
        EventStatus status = EventStatus.valueOf(rs.getString("eventStatus").trim());

        switch (type) {
            case "Concert":
                return new Concert(id, eventName, eventDescription, startDateTime, endDateTime, venueID, status);
            case "Sports Event":
                return new SportsEvent(id, eventName, eventDescription, startDateTime, endDateTime, venueID, status);
            default:
                throw new IllegalArgumentException("Unknown event type: " + type);
        }
    }

    /**
     * Parses a CSV line to create an Event object.
     * @param csvLine A single line from a CSV file representing an event. The fields are expected in this order:
     *                ID, Type, Name, Description, StartDateTime, EndDateTime, VenueID, EventStatus.
     * @return An Event object corresponding to the provided CSV data.
     * @throws IllegalArgumentException if the event type is not recognized.
     */
    public static Event fromCSV(String csvLine) {
        String[] fields = csvLine.split(",");
        if (fields.length != 8) {
            throw new IllegalArgumentException("Malformed CSV line for Event: " + csvLine);
        }

        int id = Integer.parseInt(fields[0].trim());
        String type = fields[1].trim();
        String eventName = fields[2].trim();
        String eventDescription = fields[3].trim();
        LocalDateTime startDateTime = LocalDateTime.parse(fields[4].trim());
        LocalDateTime endDateTime = LocalDateTime.parse(fields[5].trim());
        int venueID = Integer.parseInt(fields[6].trim());
        EventStatus status = EventStatus.valueOf(fields[7].trim());

        switch (type) {
            case "Concert":
                return new Concert(id, eventName, eventDescription, startDateTime, endDateTime, venueID, status);
            case "Sports Event":
                return new SportsEvent(id, eventName, eventDescription, startDateTime, endDateTime, venueID, status);
            default:
                throw new IllegalArgumentException("Unknown event type: " + type);
        }
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