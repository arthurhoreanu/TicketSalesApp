package model;

import java.util.List;

/**
 * Represents a venue for events, containing information about its ID, name, location, capacity, and sections.
 */
public class Venue implements Identifiable {
    private int venueID;
    private String venueName;
    private String location;
    private int venueCapacity;
    public List<Section> sections;

    public Venue fromCsvFormat(String csvLine) {
        return null;
    }

    @Override
    public String toCsvFormat() {
        return "";
    }

    /**
     * Constructs a Venue with the specified ID, name, location, capacity, and sections.
     * @param venueID       the unique ID of the venue
     * @param venueName     the name of the venue
     * @param location      the location of the venue
     * @param venueCapacity the capacity of the venue
     * @param sections      the list of sections within the venue
     */
    public Venue(int venueID, String venueName, String location, int venueCapacity, List<Section> sections) {
        this.venueID = venueID;
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
        this.sections = sections;
    }

    /**
     * Gets the unique ID of the venue.
     * @return the ID of the venue
     */
    @Override
    public Integer getID() {
        return this.venueID;
    }

    /**
     * Gets the list of sections within the venue.
     * @return the list of sections
     */
    public List<Section> getSections() {
        return this.sections;
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

    /**
     * Returns a string representation of the venue, including the venue ID, name, location, and capacity.
     * @return a string containing the venue details
     */
    @Override
    public String toString() {
        return "Venue{" + "venueID=" + venueID + ", venueName='" + venueName + '\'' + ", location='" + location + '\'' + ", venueCapacity=" + venueCapacity + '}';
    }
}
