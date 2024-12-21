package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a venue for events, containing details such as its unique ID, name, location, and capacity.
 * Includes relationships with Sections (1:N).
 */
@Entity
@Table(name = "venue")
public class Venue implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int venueID;

    @Column(name = "venue_name", nullable = false)
    private String venueName;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "venue_capacity", nullable = false)
    private int venueCapacity;

    @Column(name = "has_seats", nullable = false)
    private boolean hasSeats;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    /**
     * Default constructor for JPA, CSV, and InMemory compatibility.
     * Initializes the sections list to avoid NullPointerException.
     */
    public Venue() {
        this.sections = new ArrayList<>();
    }

    /**
     * Constructs a Venue with the specified attributes.
     *
     * @param venueID       the unique ID of the venue
     * @param venueName     the name of the venue
     * @param location      the location of the venue
     * @param venueCapacity the total capacity of the venue
     * @param hasSeats      true if the venue has seats, false otherwise
     */
    public Venue(int venueID, String venueName, String location, int venueCapacity, boolean hasSeats) {
        this();
        this.venueID = venueID;
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
        this.hasSeats = hasSeats;
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

    public boolean isHasSeats() {
        return hasSeats;
    }

    public void setHasSeats(boolean hasSeats) {
        this.hasSeats = hasSeats;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    /**
     * Adds a Section to the Venue and maintains bidirectional relationship.
     *
     * @param section The section to add.
     */
    public void addSection(Section section) {
        sections.add(section);
        section.setVenue(this); // Maintain bidirectional relationship
    }

    /**
     * Removes a Section from the Venue and maintains bidirectional relationship.
     *
     * @param section The section to remove.
     */
    public void removeSection(Section section) {
        sections.remove(section);
        section.setVenue(null); // Break bidirectional relationship
    }

    @Override
    public String toString() {
        return "Venue{" +
                "venueID=" + venueID +
                ", venueName='" + venueName + '\'' +
                ", location='" + location + '\'' +
                ", venueCapacity=" + venueCapacity +
                ", hasSeats=" + hasSeats +
                ", sections=" + sections.size() +
                '}';
    }

    // CSV Methods

    /**
     * Converts the Venue object into a CSV representation.
     *
     * @return A comma-separated string representing the venue.
     */
    public String toCsv() {
        return String.join(",",
                String.valueOf(getID()),
                getVenueName(),
                getLocation(),
                String.valueOf(getVenueCapacity()),
                String.valueOf(hasSeats)
        );
    }

    /**
     * Creates a Venue object from a CSV string.
     *
     * @param csvLine The CSV string.
     * @return A Venue object.
     */
    public static Venue fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int venueID = Integer.parseInt(fields[0].trim());
        String venueName = fields[1].trim();
        String location = fields[2].trim();
        int venueCapacity = Integer.parseInt(fields[3].trim());
        boolean hasSeats = Boolean.parseBoolean(fields[4].trim());
        Venue venue = new Venue(venueID, venueName, location, venueCapacity, hasSeats);

        // Initialize sections list for InMemory/CSV compatibility
        venue.setSections(new ArrayList<>());
        return venue;
    }
}
