package model;

import java.util.List;

/**
 * Represents a section within a venue, containing details such as name, capacity, associated venue, and seats.
 */
public class Section implements Identifiable {
    private int sectionID;
    private String sectionName;
    private int sectionCapacity;
    private Venue venue;
    private List<Seat> seats;

    @Override
    public String toCsvFormat() {
        return "";
    }

    /**
     * Constructs a Section with the specified attributes.
     * @param sectionID      the unique ID of the section
     * @param sectionName    the name of the section
     * @param sectionCapacity the seating capacity of the section
     * @param venue          the venue where the section is located
     * @param seats          the list of seats in this section
     */
    public Section(int sectionID, String sectionName, int sectionCapacity, Venue venue, List<Seat> seats) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionCapacity = sectionCapacity;
        this.venue = venue;
        this.seats = seats;
    }

    /**
     * Gets the unique ID of the section.
     * @return the ID of the section
     */
    @Override
    public Integer getID() {
        return this.sectionID;
    }

    /**
     * Gets the venue associated with the section.
     * @return the venue of the section
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * Sets the venue for this section.
     * @param venue the venue to set for this section
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * Gets the list of seats in this section.
     * @return the list of seats in the section
     */
    public List<Seat> getSeats() {
        return seats;
    }

    /**
     * Gets the name of the section.
     * @return the name of the section
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Gets the seating capacity of the section.
     * @return the seating capacity of the section
     */
    public int getSectionCapacity() {
        return sectionCapacity;
    }

    /**
     * Returns a string representation of the section, including its ID, name, capacity, and venue.
     * @return a string representing the section's details
     */
    @Override
    public String toString() {
        return "Section{" +
                "sectionID=" + sectionID +
                ", sectionName='" + sectionName + '\'' +
                ", sectionCapacity=" + sectionCapacity +
                ", venue=" + venue +
                '}';
    }
}
