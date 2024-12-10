package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a concert event, which is a type of event featuring a list of artists.
 */
public class Concert extends Event {

    /**
     * Constructs a Concert with the specified details and list of performing artists.
     * @param eventID          the unique ID of the concert
     * @param eventName        the name of the concert
     * @param eventDescription a description of the concert
     * @param startDateTime    the start date and time of the concert
     * @param endDateTime      the end date and time of the concert
     * @param venue            the venue where the concert takes place
     * @param eventStatus      the current status of the concert (SCHEDULED, CANCELLED, COMPLETED)
     */
    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus);
    }

    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, getID());
        stmt.setString(2, getEventName());
        stmt.setString(3, getEventDescription());
        stmt.setTimestamp(4, Timestamp.valueOf(getStartDateTime()));
        stmt.setTimestamp(5, Timestamp.valueOf(getEndDateTime()));
        stmt.setInt(6, getVenue().getID());
        stmt.setString(7, getEventStatus().name());
    }

    /**
     * Returns a string representation of the concert, including its details and the list of artists.
     * @return a string representing the concert's details and artists
     */
    @Override
    public String toString() {
        return "Concert" + super.toString() + '}';
    }

    @Override
    public String toCsv() {
        return getID() + "," +
                "Concert," +
                getEventName() + "," +
                getEventDescription() + "," +
                getStartDateTime() + "," +
                getEndDateTime() + "," +
                getVenue().getVenueName() + "," +
                getEventStatus();
    }

}
