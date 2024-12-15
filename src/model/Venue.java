package model;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a venue for events, containing details such as its unique ID, name, location, and capacity.
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

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    /**
     * Default constructor for JPA and serialization.
     */
    public Venue() {}

    /**
     * Constructs a Venue with the specified attributes.
     * Typically used for initializing in-memory objects or loading from external sources.
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

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
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

    // CSV Methods
    public String toCsv() {
        return String.join(",",
                String.valueOf(getID()),
                getVenueName(),
                getLocation(),
                String.valueOf(getVenueCapacity())
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
}
