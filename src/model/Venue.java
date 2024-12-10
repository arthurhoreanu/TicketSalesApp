package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a venue for events, containing information about its ID, name, location, capacity, and sections.
 */
public class Venue implements Identifiable {
    private static int venueCounter = 1;
    private int venueID;
    private String venueName;
    private String location;
    private int venueCapacity;
    public List<Section> sections;

    // Controller for accessing sections dynamically
    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Venue with the specified ID, name, location, capacity, and sections.
     * @param venueID       the unique ID of the venue
     * @param venueName     the name of the venue
     * @param location      the location of the venue
     * @param venueCapacity the capacity of the venue
     */
    public Venue(int venueID, String venueName, String location, int venueCapacity) {
        this.venueID = venueCounter++;
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
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

    /**
     * Creates a Venue object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string.
     * @return the deserialized Venue object.
     */
    public static Venue fromCsv(String csvLine){
        String[] fields = csvLine.split(",");
        int venueID = Integer.parseInt(fields[0].trim());
        String venueName = fields[1].trim();
        String location = fields[2].trim();
        int venueCapacity = Integer.parseInt(fields[3].trim());
        return new Venue(venueID, venueName, location, venueCapacity);
    }

    /**
     * Converts the Venue object into a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the Venue.
     */
    @Override
    public String toCsv(){
        return String.join(",",
                String.valueOf(venueID),
                venueName,
                location,
                String.valueOf(venueCapacity)
        );
    }

    /**
     * Saves the Venue object to a database.
     *
     * @param stmt the PreparedStatement for inserting the venue
     * @throws SQLException if a database error occurs
     */
    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, this.venueID);
        stmt.setString(2, this.venueName);
        stmt.setString(3, this.location);
        stmt.setInt(4, this.venueCapacity);
    }
    /**
     * Creates a Venue object from a database result set.
     *
     * @param rs the ResultSet containing venue data
     * @return the deserialized Venue object
     * @throws SQLException if a database error occurs
     */
    public static Venue fromDatabase(ResultSet rs) throws SQLException {
        int venueID = rs.getInt("venueID");
        String venueName = rs.getString("venueName");
        String location = rs.getString("location");
        int venueCapacity = rs.getInt("venueCapacity");
        return new Venue(venueID, venueName, location, venueCapacity);
    }


    //TODo VERY IMPORTANT!!!!!
    /**
     * Fetches all sections for this venue dynamically.
     *
     * @return a list of sections belonging to this venue
     *//*
    public List<Section> getSections() {
        return controller.findSectionsByVenueID(venueID);
    }*/


    /**
     * Gets the unique ID of the venue.
     * @return the ID of the venue
     */
    @Override
    public Integer getID() {
        return this.venueID;
    }

    /**
     * Gets the name of the venue.
     * @return the name of the venue
     */
    public String getVenueName() {
        return venueName;
    }

    /**
     * Sets the name of the venue.
     * @param venueName the new name of the venue
     */
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    /**
     * Gets the location of the venue.
     * @return the location of the venue
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the venue.
     * @param location the new location of the venue
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the capacity of the venue.
     * @return the capacity of the venue
     */
    public int getVenueCapacity() {
        return venueCapacity;
    }

    /**
     * Sets the capacity of the venue.
     * @param venueCapacity the new capacity of the venue
     */
    public void setVenueCapacity(int venueCapacity) {
        this.venueCapacity = venueCapacity;
    }


    //TODO don't forget about this
   /* *//**
     * Adds a section to this venue in the database.
     *
     * @param section the section to add
     *//*
    public void addSection(Section section) {
        section.setVenueID(this.venueID);
        controller.saveSection(section);
    }*/

}
