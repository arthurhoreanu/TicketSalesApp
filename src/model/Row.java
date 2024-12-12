package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a row within a section, containing details such as its ID, capacity, and associated section.
 * A row can dynamically fetch its associated seats and parent section using provided methods.
 */
public class Row implements Identifiable {

    private int rowID;
    private int rowCapacity;
    private int sectionID;

    /**
     * Controller instance used to dynamically fetch associated data such as seats or sections.
     */
    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Row object with a specified ID, capacity, and section ID.
     * This constructor is typically used when loading data from external sources such as CSV files or databases.
     *
     * @param rowID       The unique ID of the row.
     * @param rowCapacity The capacity of the row.
     * @param sectionID   The ID of the section that this row belongs to.
     */
    public Row(int rowID, int rowCapacity, int sectionID) {
        this.rowID = rowID;
        this.rowCapacity = rowCapacity;
        this.sectionID = sectionID;
    }
    /**
     * Retrieves the unique ID of the row.
     *
     * @return The unique ID of the row.
     */
    @Override
    public Integer getID() {
        return rowID;
    }

    /**
     * Sets the unique ID of the row.
     *
     * @param rowID The unique ID to assign to the row.
     */
    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    /**
     * Retrieves the capacity of the row.
     *
     * @return The capacity of the row.
     */
    public int getRowCapacity() {
        return rowCapacity;
    }

    /**
     * Sets the capacity of the row.
     *
     * @param rowCapacity The capacity to assign to the row.
     */
    public void setRowCapacity(int rowCapacity) {
        this.rowCapacity = rowCapacity;
    }

    /**
     * Retrieves the ID of the section that this row belongs to.
     *
     * @return The ID of the section.
     */
    public int getSectionID() {
        return sectionID;
    }

    /**
     * Sets the ID of the section that this row belongs to.
     *
     * @param sectionID The ID of the section to assign to the row.
     */
    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    /**
     * Retrieves the parent Section object that this row belongs to, using the sectionID.
     *
     * @return The Section object associated with this row.
     */
    public Section getSection() {
        return controller.findSectionByID(sectionID);
    }
//todo findSeatByRowID in controller
    /**
     * Dynamically retrieves the list of seats associated with this row.
     *
     * @return A list of Seat objects belonging to this row.
     */
    public List<Seat> getSeats() {
        return controller.findSeatsByRowID(rowID);
    }

    /**
     * Converts this Row object into a CSV-formatted string.
     * The format includes the rowID, rowCapacity, and sectionID.
     *
     * @return A CSV-formatted string representing this row.
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
     * Creates a Row object from a CSV-formatted string.
     * The CSV format should include the rowID, rowCapacity, and sectionID, separated by commas.
     *
     * @param csvLine The CSV-formatted string representing a Row.
     * @return The deserialized Row object.
     */
    public static Row fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int rowID = Integer.parseInt(fields[0].trim());
        int rowCapacity = Integer.parseInt(fields[1].trim());
        int sectionID = Integer.parseInt(fields[2].trim());
        return new Row(rowID, rowCapacity, sectionID);
    }

    /**
     * Saves the Row object to a database using a PreparedStatement.
     *
     * @param stmt The PreparedStatement for saving the row data.
     * @throws SQLException If a database error occurs.
     */
    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, rowID);
        stmt.setInt(2, rowCapacity);
        stmt.setInt(3, sectionID);
    }

    /**
     * Creates a Row object from a database ResultSet.
     *
     * @param rs The ResultSet containing row data.
     * @return The deserialized Row object.
     * @throws SQLException If a database error occurs.
     */
    public static Row fromDatabase(ResultSet rs) throws SQLException {
        int rowID = rs.getInt("rowID");
        int rowCapacity = rs.getInt("rowCapacity");
        int sectionID = rs.getInt("sectionID");
        return new Row(rowID, rowCapacity, sectionID);
    }

    /**
     * Provides a string representation of the Row object, including its ID, capacity, and associated section ID.
     *
     * @return A string representation of the row.
     */
    @Override
    public String toString() {
        return "Row{" +
                "rowID=" + rowID +
                ", rowCapacity=" + rowCapacity +
                ", sectionID=" + sectionID +
                '}';
    }
}
