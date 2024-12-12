package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a venue for events, containing details such as its unique ID, name, location, and capacity.
 */
public class Venue implements Identifiable {

    private int venueID;
    private String venueName;
    private String location;
    private int venueCapacity;

    /** Controller for dynamic data retrieval. */
    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Venue with the specified attributes.
     * Typically used for loading venue data from external sources like CSV files or a database.
     *
     * @param venueID       the unique ID of the venue
     * @param venueName     the name of the venue
     * @param location      the location of the venue
     * @param venueCapacity the total capacity of the venue
     */
    public Venue(int venueID, String venueName, String location, int venueCapacity) {
        this.venueID = venueID;
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
    }

    /**
     * Constructs a Venue for in-memory operations, where the ID will be assigned externally.
     *
     * @param venueName     the name of the venue
     * @param location      the location of the venue
     * @param venueCapacity the total capacity of the venue
     */
    public Venue(String venueName, String location, int venueCapacity) {
        this.venueID = 0; // Default ID, to be set externally
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
    }

    /**
     * Retrieves the unique ID of the venue.
     *
     * @return the ID of the venue
     */
    @Override
    public Integer getID() {
        return venueID;
    }

    /**
     * Sets the unique ID of the venue.
     *
     * @param venueID the ID to set for the venue
     */
    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    /**
     * Retrieves the name of the venue.
     *
     * @return the name of the venue
     */
    public String getVenueName() {
        return venueName;
    }

    /**
     * Sets the name of the venue.
     *
     * @param venueName the name to set for the venue
     */
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    /**
     * Retrieves the location of the venue.
     *
     * @return the location of the venue
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the venue.
     *
     * @param location the location to set for the venue
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the total capacity of the venue.
     *
     * @return the capacity of the venue
     */
    public int getVenueCapacity() {
        return venueCapacity;
    }

    /**
     * Sets the total capacity of the venue.
     *
     * @param venueCapacity the capacity to set for the venue
     */
    public void setVenueCapacity(int venueCapacity) {
        this.venueCapacity = venueCapacity;
    }
//todo findSectionByVenueID in controller
    /**
     * Dynamically retrieves the list of sections associated with this venue.
     *
     * @return a list of {@link Section} objects associated with the venue
     */
    public List<Section> getSections() {
        return controller.findSectionsByVenueID(venueID);
    }

    /**
     * Converts the venue's attributes into a CSV-formatted string.
     *
     * @return a CSV representation of the venue
     */
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(venueID),
                venueName,
                location,
                String.valueOf(venueCapacity)
        );
    }

    /**
     * Creates a Venue object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string representing a venue
     * @return a {@link Venue} object with the parsed data
     */
    public static Venue fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int venueID = Integer.parseInt(fields[0].trim());
        String venueName = fields[1].trim();
        String location = fields[2].trim();
        int venueCapacity = Integer.parseInt(fields[3].trim());
        return new Venue(venueID, venueName, location, venueCapacity);
    }

    /**
     * Saves the venue's attributes to the database using a prepared statement.
     *
     * @param stmt the {@link PreparedStatement} to use for saving the venue
     * @throws SQLException if an SQL error occurs
     */
    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, venueID);
        stmt.setString(2, venueName);
        stmt.setString(3, location);
        stmt.setInt(4, venueCapacity);
    }

    /**
     * Creates a Venue object from a database result set.
     *
     * @param rs the {@link ResultSet} containing the venue's data
     * @return a {@link Venue} object with the data from the database
     * @throws SQLException if an SQL error occurs
     */
    public static Venue fromDatabase(ResultSet rs) throws SQLException {
        int venueID = rs.getInt("venueID");
        String venueName = rs.getString("venueName");
        String location = rs.getString("location");
        int venueCapacity = rs.getInt("venueCapacity");
        return new Venue(venueID, venueName, location, venueCapacity);
    }

    /**
     * Returns a string representation of the venue's attributes.
     *
     * @return a string representation of the venue
     */
    @Override
    public String toString() {
        return "Venue{" +
                "venueID=" + venueID +
                ", venueName='" + venueName + '\'' +
                ", location='" + location + '\'' +
                ", venueCapacity=" + venueCapacity +
                '}';
    }
}
