package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a seat in a venue section, with details such as row, seat number, and reservation status.
 */
public class Seat implements Identifiable {
    private static int seatCounter = 1;
    private int seatID;
    private int rowID;
    //todo rethink about this attribute and method related to this bool
    private boolean isReserved;
    private Integer reservedForEventID; // Links to Event if reserved, null otherwise todo

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Seat with the specified attributes.
     * @param seatID          the unique ID of the seat
     * @param rowID           the ID of the row where the seat is located
     * @param isReserved      whether the seat is reserved
     * @param reservedForEventID the ID of the reserved event, null if not reserved
     */
    public Seat(int seatID, int rowID, boolean isReserved, Integer reservedForEventID) {
        this.seatID = seatCounter++;
        this.rowID = rowID;
        this.isReserved = isReserved;
        this.reservedForEventID = reservedForEventID;
    }
    //TODO VERY IMPORTANT!!!
    /**
     * Fetches the row associated with this seat dynamically.
     *
     * @return the Row object corresponding to this seat's rowID
     *//*
    public Row getRow() {
        return controller.findRowByID(rowID);
    }

    *//**
     * Fetches the event for which this seat is reserved, if applicable.
     *
     * @return the Event object if the seat is reserved, null otherwise
     *//*
    public Event getReservedForEvent() {
        return reservedForEventID == null ? null : controller.findEventByID(reservedForEventID);
    }*/


    /**
     * Returns a string representation of the seat, including its ID, section, row number, seat number, and reserved event.
     * @return a string representing the seat's details
     */
    @Override
    public String toString() {
        return "Seat{" +
                "seatID=" + seatID +
                ", rowID=" + rowID +
                ", isReserved=" + isReserved +
                ", reservedForEventID=" + reservedForEventID +
                '}';
    }

    /**
     * Creates a Seat object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string.
     * @return the deserialized Seat object.
     */
    public static Seat fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int seatID = Integer.parseInt(fields[0].trim());
        int rowID = Integer.parseInt(fields[1].trim());
        boolean isReserved = Boolean.parseBoolean(fields[2].trim());
        Integer reservedForEventID = fields[3].trim().equals("null") ? null : Integer.parseInt(fields[3].trim());
        return new Seat(seatID, rowID, isReserved, reservedForEventID);
    }

    /**
     * Converts the Seat object into a CSV-formatted string.
     * @return the CSV-formatted string representing the Seat.
     */

    //TODO change CSV s to accommodate the Row class
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(seatID),
                String.valueOf(rowID),
                String.valueOf(isReserved),
                reservedForEventID == null ? "null" : String.valueOf(reservedForEventID)
        );
    }

    /**
     * Saves the Seat object to a database.
     *
     * @param stmt the PreparedStatement for inserting the seat
     * @throws SQLException if a database error occurs
     */
    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, this.seatID);
        stmt.setInt(2, this.rowID);
        stmt.setBoolean(3, this.isReserved);
        stmt.setObject(4, this.reservedForEventID); // Use setObject to handle nulls
    }
    /**
     * Creates a Seat object from a database result set.
     *
     * @param rs the ResultSet containing seat data
     * @return the deserialized Seat object
     * @throws SQLException if a database error occurs
     */
    public static Seat fromDatabase(ResultSet rs) throws SQLException {
        int seatID = rs.getInt("seatID");
        int rowID = rs.getInt("rowID");
        boolean isReserved = rs.getBoolean("isReserved");
        Integer reservedForEventID = rs.getObject("reservedForEventID") != null ? rs.getInt("reservedForEventID") : null;
        return new Seat(seatID, rowID, isReserved, reservedForEventID);
    }


    /**
     * Gets the unique ID of the seat.
     * @return the ID of the seat
     */
    @Override
    public Integer getID() {
        return this.seatID;
    }
    /**
     * Gets the ID of the row associated with this seat.
     *
     * @return the row ID
     */
    public int getRowID() {
        return rowID;
    }

    /**
     * Sets the ID of the row for this seat.
     *
     * @param rowID the row ID to set
     */
    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    /**
     * Gets the reservation status of the seat.
     *
     * @return true if the seat is reserved, false otherwise
     */
    public boolean isReserved() {
        return isReserved;
    }

    /**
     * Sets the reservation status of the seat.
     *
     * @param reserved the reservation status to set
     */
    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    /**
     * Gets the ID of the reserved event, if applicable.
     *
     * @return the reserved event ID, or null if not reserved
     */
    public Integer getReservedForEventID() {
        return reservedForEventID;
    }

    /**
     * Sets the ID of the reserved event.
     *
     * @param reservedForEventID the reserved event ID to set
     */
    public void setReservedForEventID(Integer reservedForEventID) {
        this.reservedForEventID = reservedForEventID;
    }

}
