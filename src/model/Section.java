package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a section within a venue, containing details such as name, capacity,
 * and its associated venue. Each section can dynamically retrieve its rows
 * and venue using IDs and a controller.
 */
public class Section implements Identifiable {


    private int sectionID;
    private String sectionName;
    private int sectionCapacity;
    private int venueID;

    /**
     * The controller used for fetching related data dynamically.
     */
    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Section with the specified attributes.
     * Used for loading from external sources such as CSV files or databases.
     *
     * @param sectionID       the unique ID of the section
     * @param sectionName     the name of the section
     * @param sectionCapacity the seating capacity of the section
     * @param venueID         the ID of the venue where the section is located
     */
    public Section(int sectionID, String sectionName, int sectionCapacity, int venueID) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionCapacity = sectionCapacity;
        this.venueID = venueID;
    }

    /**
     * Gets the unique ID of this section.
     *
     * @return the section ID
     */
    @Override
    public Integer getID() {
        return sectionID;
    }

    /**
     * Sets the unique ID of this section.
     *
     * @param sectionID the new section ID
     */
    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    /**
     * Gets the name of this section.
     *
     * @return the section name
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the name of this section.
     *
     * @param sectionName the new section name
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * Gets the seating capacity of this section.
     *
     * @return the section's seating capacity
     */
    public int getSectionCapacity() {
        return sectionCapacity;
    }

    /**
     * Sets the seating capacity of this section.
     *
     * @param sectionCapacity the new seating capacity
     */
    public void setSectionCapacity(int sectionCapacity) {
        this.sectionCapacity = sectionCapacity;
    }

    /**
     * Gets the ID of the venue to which this section belongs.
     *
     * @return the venue ID
     */
    public int getVenueID() {
        return venueID;
    }

    /**
     * Sets the ID of the venue to which this section belongs.
     *
     * @param venueID the new venue ID
     */
    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }
//todo findVenueByID in controller
    /**
     * Fetches the Venue object associated with this section using the venue ID.
     *
     * @return the Venue object
     */
    public Venue getVenue() {
        return controller.findVenueByID(venueID);
    }
//todo findRowsBySectionID in controller

    /**
     * Fetches the list of rows within this section dynamically using the section ID.
     *
     * @return a list of Row objects in this section
     */
    public List<Row> getRows() {
        return controller.findRowsBySectionID(sectionID);
    }

    /**
     * Converts this section into a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the section
     */
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(sectionID),
                sectionName,
                String.valueOf(sectionCapacity),
                String.valueOf(venueID)
        );
    }

    /**
     * Creates a Section object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string
     * @return the deserialized Section object
     */
    public static Section fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int sectionID = Integer.parseInt(fields[0].trim());
        String sectionName = fields[1].trim();
        int sectionCapacity = Integer.parseInt(fields[2].trim());
        int venueID = Integer.parseInt(fields[3].trim());
        return new Section(sectionID, sectionName, sectionCapacity, venueID);
    }

    /**
     * Saves this section to a database using a prepared statement.
     *
     * @param stmt the PreparedStatement for inserting the section
     * @throws SQLException if a database error occurs
     */
    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, sectionID);
        stmt.setString(2, sectionName);
        stmt.setInt(3, sectionCapacity);
        stmt.setInt(4, venueID);
    }

    /**
     * Creates a Section object from a database result set.
     *
     * @param rs the ResultSet containing section data
     * @return the deserialized Section object
     * @throws SQLException if a database error occurs
     */
    public static Section fromDatabase(ResultSet rs) throws SQLException {
        int sectionID = rs.getInt("sectionID");
        String sectionName = rs.getString("sectionName");
        int sectionCapacity = rs.getInt("sectionCapacity");
        int venueID = rs.getInt("venueID");
        return new Section(sectionID, sectionName, sectionCapacity, venueID);
    }

    /**
     * Returns a string representation of the section, including its ID, name, capacity, and associated venue ID.
     *
     * @return a string representing the section
     */
    @Override
    public String toString() {
        return "Section{" +
                "sectionID=" + sectionID +
                ", sectionName='" + sectionName + '\'' +
                ", sectionCapacity=" + sectionCapacity +
                ", venueID=" + venueID +
                '}';
    }
}
