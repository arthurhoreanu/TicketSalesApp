package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents the association between an Event and a Performer (Artist or Athlete).
 */
public class LineUp implements Identifiable{
    private int eventID;       // Foreign key to Event
    private int performerID;   // Foreign key to either Artist or Athlete

    /**
     * Constructs a LineUp with the specified event and performer IDs.
     * @param eventID the ID of the associated Event
     * @param performerID the ID of the associated Performer (Artist or Athlete)
     */
    public LineUp(int eventID, int performerID) {
        this.eventID = eventID;
        this.performerID = performerID;
    }

    /**
     * Gets the ID of the associated Event.
     * @return the event ID
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Sets the ID of the associated Event.
     * @param eventID the event ID
     */
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    /**
     * Gets the ID of the associated Performer.
     * @return the performer ID
     */
    public int getPerformerID() {
        return performerID;
    }

    /**
     * Sets the ID of the associated Performer.
     * @param performerID the performer ID
     */
    public void setPerformerID(int performerID) {
        this.performerID = performerID;
    }

    /**
     * Converts the current LineUp object into a PreparedStatement for database operations.
     * @param stmt the PreparedStatement to populate with LineUp data.
     * @throws SQLException if an SQL error occurs.
     */
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, this.eventID);
        stmt.setInt(2, this.performerID);
    }

    /**
     * Creates a LineUp object from a ResultSet.
     * @param rs the ResultSet containing LineUp data.
     * @return a LineUp object populated with data from the ResultSet.
     * @throws SQLException if an SQL error occurs.
     */
    public static LineUp fromDatabase(ResultSet rs) throws SQLException {
        int eventID = rs.getInt("eventID");
        int performerID = rs.getInt("performerID");
        return new LineUp(eventID, performerID);
    }

    /**
     * Returns a string representation of the LineUp.
     * @return a string representing the LineUp details.
     */
    @Override
    public String toString() {
        return "LineUp{" +
                "eventID=" + eventID +
                ", performerID=" + performerID +
                '}';
    }

    @Override
    public Integer getID() {
        return null;
    }

    /**
     * Converts the current LineUp object into a CSV-formatted string.
     * Format: {eventID,performerID}
     * @return a CSV-formatted string representing the current LineUp.
     */
    public String toCsv() {
        return eventID + "," + performerID;
    }

    /**
     * Creates a LineUp object from a CSV-formatted string.
     * The input string is expected to have the format: {eventID,performerID}.
     * @param csvLine the CSV-formatted string representing a LineUp.
     * @return a LineUp object parsed from the input string.
     * @throws IllegalArgumentException if the input string is malformed.
     */
    public static LineUp fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        if (fields.length != 2) {
            throw new IllegalArgumentException("Malformed CSV line for LineUp: " + csvLine);
        }
        int eventID = Integer.parseInt(fields[0].trim());
        int performerID = Integer.parseInt(fields[1].trim());
        return new LineUp(eventID, performerID);
    }
}