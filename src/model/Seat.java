package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a seat in a venue section, with details such as row, seat number, and reservation status.
 */
public class Seat implements Identifiable {
    private int seatID;
    private int rowID; // Links to the parent Row
    private boolean isReserved;
    private Integer reservedForEventID; // Nullable ID for reserved Event

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Seat with the specified attributes.
     * Used for loading from external sources (CSV, DB).
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
     * Constructs a Seat for in-memory operations, where an ID will be assigned externally.
     *
     * @param rowID the ID of the row where the seat is located
     * @param isReserved whether the seat is reserved
     * @param reservedForEventID the ID of the reserved event, null if not reserved
     */
    public Seat(int rowID, boolean isReserved, Integer reservedForEventID) {
        this.seatID = 0; // Default ID, to be set externally
        this.rowID = rowID;
        this.isReserved = isReserved;
        this.reservedForEventID = reservedForEventID;
    }

    @Override
    public Integer getID() {
        return seatID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Integer getReservedForEventID() {
        return reservedForEventID;
    }

    public void setReservedForEventID(Integer reservedForEventID) {
        this.reservedForEventID = reservedForEventID;
    }
    //todo findRowByID in controller
    public Row getRow() {
        return controller.findRowByID(rowID);
    }

    public Event getReservedForEvent() {
        return reservedForEventID == null ? null : controller.findEventByID(reservedForEventID);
    }

    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(seatID),
                String.valueOf(rowID),
                String.valueOf(isReserved),
                reservedForEventID == null ? "null" : String.valueOf(reservedForEventID)
        );
    }

    public static Seat fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int seatID = Integer.parseInt(fields[0].trim());
        int rowID = Integer.parseInt(fields[1].trim());
        boolean isReserved = Boolean.parseBoolean(fields[2].trim());
        Integer reservedForEventID = fields[3].trim().equals("null") ? null : Integer.parseInt(fields[3].trim());
        return new Seat(seatID, rowID, isReserved, reservedForEventID);
    }

    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, this.seatID);
        stmt.setInt(2, this.rowID);
        stmt.setBoolean(3, this.isReserved);
        stmt.setObject(4, this.reservedForEventID);
    }

    public static Seat fromDatabase(ResultSet rs) throws SQLException {
        int seatID = rs.getInt("seatID");
        int rowID = rs.getInt("rowID");
        boolean isReserved = rs.getBoolean("isReserved");
        Integer reservedForEventID = rs.getObject("reservedForEventID") != null ? rs.getInt("reservedForEventID") : null;
        return new Seat(seatID, rowID, isReserved, reservedForEventID);
    }

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
