package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a sports event, extending the general Event class with a list of participating athletes.
 */
public class SportsEvent extends Event {

    /**
     * Constructs a SportsEvent with the specified attributes.
     * @param eventID         the unique ID of the event
     * @param eventName       the name of the event
     * @param eventDescription a description of the event
     * @param startDateTime   the start date and time of the event
     * @param endDateTime     the end date and time of the event
     * @param venue           the venue where the event is held
     * @param eventStatus     the current status of the event (SCHEDULED, CANCELLED, COMPLETED)
     */
    public SportsEvent(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus);
    }

    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, getID());
        stmt.setString(2, "Sports Event");
        stmt.setString(3, getEventName());
        stmt.setString(4, getEventDescription());
        stmt.setTimestamp(5, Timestamp.valueOf(getStartDateTime()));
        stmt.setTimestamp(6, Timestamp.valueOf(getEndDateTime()));
        stmt.setInt(7, getVenue().getID());
        stmt.setString(8, getEventStatus().name());
    }

    /**
     * Returns a string representation of the sports event, including its athletes.
     * @return a string representing the sports event details
     */
    @Override
    public String toString() {
        return "SportsEvent{" +  ", " + super.toString() + '}';
    }

    @Override
    public String toCsv() {
        return getID() + "," +
                "Sports Event," +
                getEventName() + "," +
                getEventDescription() + "," +
                getStartDateTime() + "," +
                getEndDateTime() + "," +
                getVenue().getVenueName() + "," +
                getEventStatus() + ",";
    }
}
