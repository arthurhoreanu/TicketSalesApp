package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a venue for events, containing information about its ID, name, location, and capacity.
 */
public class Venue implements Identifiable {
    private int venueID;
    private String venueName;
    private String location;
    private int venueCapacity;

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Venue with the specified attributes.
     * Used for loading from external sources (CSV, DB).
     *
     * @param venueID       the unique ID of the venue
     * @param venueName     the name of the venue
     * @param location      the location of the venue
     * @param venueCapacity the capacity of the venue
     */
    public Venue(int venueID, String venueName, String location, int venueCapacity) {
        this.venueID = venueID;
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
    }

    /**
     * Constructs a Venue for in-memory operations, where an ID will be assigned externally.
     *
     * @param venueName     the name of the venue
     * @param location      the location of the venue
     * @param venueCapacity the capacity of the venue
     */
    public Venue(String venueName, String location, int venueCapacity) {
        this.venueID = 0; // Default ID, to be set externally
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
    }

    @Override
    public Integer getID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getVenueCapacity() {
        return venueCapacity;
    }

    public void setVenueCapacity(int venueCapacity) {
        this.venueCapacity = venueCapacity;
    }
//todo findSectionByVenueID in controller
    public List<Section> getSections() {
        return controller.findSectionsByVenueID(venueID);
    }

    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(venueID),
                venueName,
                location,
                String.valueOf(venueCapacity)
        );
    }

    public static Venue fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int venueID = Integer.parseInt(fields[0].trim());
        String venueName = fields[1].trim();
        String location = fields[2].trim();
        int venueCapacity = Integer.parseInt(fields[3].trim());
        return new Venue(venueID, venueName, location, venueCapacity);
    }

    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, venueID);
        stmt.setString(2, venueName);
        stmt.setString(3, location);
        stmt.setInt(4, venueCapacity);
    }

    public static Venue fromDatabase(ResultSet rs) throws SQLException {
        int venueID = rs.getInt("venueID");
        String venueName = rs.getString("venueName");
        String location = rs.getString("location");
        int venueCapacity = rs.getInt("venueCapacity");
        return new Venue(venueID, venueName, location, venueCapacity);
    }

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
