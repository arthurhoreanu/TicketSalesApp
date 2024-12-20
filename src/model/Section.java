package model;

import controller.Controller;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a section within a venue, containing details such as name, capacity,
 * and its associated venue. Each section can dynamically retrieve its rows
 * and venue using IDs and a controller.
 */
@Entity
@Table(name = "section")
public class Section implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sectionID;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @Column(name = "section_capacity", nullable = false)
    private int sectionCapacity;

    @Column(name = "venue_id", nullable = false)
    private int venueID; // Foreign key for Venue

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Row> rows; // Relationship 0:N with Row

    /**
     * Default constructor for JPA and serialization.
     */
    public Section() {
    }

    static Controller controller = ControllerProvider.getController();


    /**
     * Constructs a Section with the specified attributes.
     *
     * @param sectionID       the unique ID of the section
     * @param sectionName     the name of the section
     * @param sectionCapacity the seating capacity of the section
     * @param venueID         the Venue object associated with this section
     */
    public Section(int sectionID, String sectionName, int sectionCapacity, int venueID) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionCapacity = sectionCapacity;
        this.venueID = venueID;
    }

    @Override
    public Integer getID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSectionCapacity() {
        return sectionCapacity;
    }

    public void setSectionCapacity(int sectionCapacity) {
        this.sectionCapacity = sectionCapacity;
    }

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }


    @Override
    public String toString() {
        return "Section{" +
                "sectionID=" + sectionID +
                ", sectionName='" + sectionName + '\'' +
                ", sectionCapacity=" + sectionCapacity +
                ", venueID=" + venueID +
                '}';
    }

    // CSV Methods
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(sectionID),
                sectionName,
                String.valueOf(sectionCapacity),
                String.valueOf(venueID)
        );
    }

    public static Section fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int sectionID = Integer.parseInt(fields[0].trim());
        String sectionName = fields[1].trim();
        int sectionCapacity = Integer.parseInt(fields[2].trim());
        int venueID = Integer.parseInt(fields[3].trim());
        return new Section(sectionID, sectionName, sectionCapacity, venueID);
    }
}
