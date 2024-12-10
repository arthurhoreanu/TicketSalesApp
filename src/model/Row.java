package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a row within a section, containing details such as its ID, capacity, and associated section.
 */
public class Row implements Identifiable {
    private static int rowCounter = 1; // For in-memory ID assignment
    private int rowID;
    private int rowCapacity;
    private int sectionID; // Links to parent Section

    // Controller to dynamically fetch related seats
    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Row with the specified attributes.
     *
     * @param rowID       the unique ID of the row
     * @param rowCapacity the capacity of the row
     * @param sectionID   the ID of the section where the row is located
     */
    public Row(int rowID, int rowCapacity, int sectionID) {
        this.rowID = rowCounter++;
        this.rowCapacity = rowCapacity;
        this.sectionID = sectionID;
    }
//todo VERY IMPORTANT!!!
    /**
     * Fetches all seats for this row dynamically.
     *
     * @return a list of seats belonging to this row
     *//*
    public List<Seat> getSeats() {
        return controller.findSeatsByRowID(rowID);
    }
*/
    @Override
    public String toString() {
        return "Row{" +
                "rowID=" + rowID +
                ", rowCapacity=" + rowCapacity +
                ", sectionID=" + sectionID +
                '}';
    }

    /**
     * Creates a Row object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string
     * @return the deserialized Row object
     */
    public static Row fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int rowID = Integer.parseInt(fields[0].trim());
        int rowCapacity = Integer.parseInt(fields[1].trim());
        int sectionID = Integer.parseInt(fields[2].trim());
        return new Row(rowID, rowCapacity, sectionID);
    }

    /**
     * Converts the Row object into a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the Row
     */
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(rowID),
                String.valueOf(rowCapacity),
                String.valueOf(sectionID)
        );
    }

    /**
     * Saves the Row object to a database.
     *
     * @param stmt the PreparedStatement for inserting the row
     * @throws SQLException if a database error occurs
     */
    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, this.rowID);
        stmt.setInt(2, this.rowCapacity);
        stmt.setInt(3, this.sectionID);
    }

    /**
     * Creates a Row object from a database result set.
     *
     * @param rs the ResultSet containing row data
     * @return the deserialized Row object
     * @throws SQLException if a database error occurs
     */
    public static Row fromDatabase(ResultSet rs) throws SQLException {
        int rowID = rs.getInt("rowID");
        int rowCapacity = rs.getInt("rowCapacity");
        int sectionID = rs.getInt("sectionID");
        return new Row(rowID, rowCapacity, sectionID);
    }

    /**
     * Gets the unique ID of the row.
     *
     * @return the ID of the row
     */
    @Override
    public Integer getID() {
        return rowID;
    }

    /**
     * Gets the capacity of the row.
     *
     * @return the capacity of the row
     */
    public int getRowCapacity() {
        return rowCapacity;
    }

    /**
     * Sets the capacity of the row.
     *
     * @param rowCapacity the new capacity of the row
     */
    public void setRowCapacity(int rowCapacity) {
        this.rowCapacity = rowCapacity;
    }

    /**
     * Gets the ID of the section associated with this row.
     *
     * @return the section ID
     */
    public int getSectionID() {
        return sectionID;
    }

    /**
     * Sets the ID of the section for this row.
     *
     * @param sectionID the section ID to set
     */
    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }
}
