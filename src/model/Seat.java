package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a seat in a venue section, with details such as its ID, parent row ID, reservation status,
 * and an optional association with an event if reserved.
 */
public class Seat implements Identifiable {
    /** Unique identifier for the seat. */
    private int seatID;

    /** The ID of the parent row where the seat is located. */
    private int rowID;

    /** Indicates whether the seat is reserved. */
    private boolean isReserved;

    /** The ID of the event for which the seat is reserved, or null if not reserved. */
    private Integer reservedForEventID;

    /** Controller for dynamically fetching related entities. */
    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Seat with the specified attributes.
     * Used for loading data from external sources like CSV files or databases.
     *
     * @param seatID the unique ID of the seat
     * @param rowID the ID of the row where the seat is located
     * @param isReserved whether the seat is reserved
     * @param reservedForEventID the ID of the reserved event, null if not reserved
     */
    public Seat(int seatID, int rowID, boolean isReserved, Integer reservedForEventID) {
        this.seatID = seatID;
        this.rowID = rowID;
        this.isReserved = isReserved;
        this.reservedForEventID = reservedForEventID;
    }

    /**
     * Gets the unique ID of the seat.
     *
     * @return the ID of the seat
     */
    @Override
    public Integer getID() {
        return seatID;
    }

    /**
     * Sets the unique ID of the seat.
     *
     * @param seatID the ID to assign to the seat
     */
    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    /**
     * Gets the ID of the parent row where the seat is located.
     *
     * @return the row ID
     */
    public int getRowID() {
        return rowID;
    }

    /**
     * Sets the ID of the parent row where the seat is located.
     *
     * @param rowID the row ID to set
     */
    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    /**
     * Checks whether the seat is reserved.
     *
     * @return true if the seat is reserved, false otherwise
     */
    public boolean isReserved() {
        return isReserved;
    }

    /**
     * Sets the reservation status of the seat.
     *
     * @param reserved true to mark the seat as reserved, false otherwise
     */
    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    /**
     * Gets the ID of the reserved event, if applicable.
     *
     * @return the event ID, or null if the seat is not reserved
     */
    public Integer getReservedForEventID() {
        return reservedForEventID;
    }

    /**
     * Sets the ID of the event for which the seat is reserved.
     *
     * @param reservedForEventID the event ID to associate with the seat
     */
    public void setReservedForEventID(Integer reservedForEventID) {
        this.reservedForEventID = reservedForEventID;
    }
    //todo findRowByID in controller
    /**
     * Fetches the parent Row associated with this seat dynamically.
     *
     * @return the Row object corresponding to this seat's row ID
     */
    public Row getRow() {
        return controller.findRowByID(rowID);
    }

    /**
     * Fetches the Event associated with this seat if reserved.
     *
     * @return the Event object if the seat is reserved, null otherwise
     */
    public Event getReservedForEvent() {
        return reservedForEventID == null ? null : controller.findEventByID(reservedForEventID);
    }

    /**
     * Converts the Seat object into a CSV-formatted string.
     *
     * @return the CSV representation of the Seat
     */
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
     * Creates a Seat object from a CSV-formatted string.
     *
     * @param csvLine the CSV string representing a Seat
     * @return a new Seat object based on the CSV data
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
     * Returns a string representation of the Seat, including its ID, row ID, reservation status,
     * and the associated event ID if reserved.
     *
     * @return a string representation of the Seat
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
}
