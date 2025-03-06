package main.java.com.ticketsalesapp.model.venue;

import main.java.com.ticketsalesapp.controller.ApplicationController;
import main.java.com.ticketsalesapp.model.ControllerProvider;
import main.java.com.ticketsalesapp.model.Identifiable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a section within a venue, containing details such as name, capacity,
 * and its associated venue. Each section can dynamically retrieve its rows
 * and venue using IDs and a controller.
 */
@Entity
@Table(name = "section")
public class Section implements Identifiable {

    private static int idCounter = 1; // Static counter for unique IDs

    @Id
    @Column(name = "section_id", nullable = false)
    private int sectionID;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @Column(name = "section_capacity", nullable = false)
    private int sectionCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Row> rows = new ArrayList<>();

    static ApplicationController applicationController = ControllerProvider.getController();

    // Default constructor for JPA
    public Section() {}

    /**
     * Constructs a Section with the specified attributes.
     *
     * @param sectionID       the unique ID of the section
     * @param sectionName     the name of the section
     * @param sectionCapacity the seating capacity of the section
     * @param venue           the Venue object associated with this section
     */
    public Section(int sectionID, String sectionName, int sectionCapacity, Venue venue) {
        this.sectionID = (sectionID > 0) ? sectionID : idCounter++;
        this.sectionName = sectionName;
        this.sectionCapacity = sectionCapacity;
        this.venue = venue;
    }

    @Override
    public Integer getID() {
        return sectionID;
    }

    @Override
    public void setID(int sectionID) {
        this.sectionID = sectionID;
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

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows.clear(); // Clear existing rows to avoid duplicates
        for (Row row : rows) {
            addRow(row); // Use addRow to maintain bidirectional relationship
        }
    }

    /**
     * Adds a Row to the Section and maintains bidirectional relationship.
     *
     * @param row The Row to add.
     */
    public void addRow(Row row) {
        rows.add(row);
        row.setSection(this); // Maintain bidirectional relationship
    }

    /**
     * Removes a Row from the Section and maintains bidirectional relationship.
     *
     * @param row The Row to remove.
     */
    public void removeRow(Row row) {
        rows.remove(row);
        row.setSection(null); // Break bidirectional relationship
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionID=" + sectionID +
                ", sectionName='" + sectionName + '\'' +
                ", sectionCapacity=" + sectionCapacity +
                ", venue=" + (venue != null ? venue.getID() : "null") +
                ", rowsCount=" + rows.size() +
                '}';
    }

    // CSV Methods

    /**
     * Converts the Section object into a CSV representation.
     *
     * @return A comma-separated string representing the Section.
     */
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(sectionID),
                sectionName,
                String.valueOf(sectionCapacity),
                String.valueOf(venue != null ? venue.getID() : "null")
        );
    }

    /**
     * Creates a Section object from a CSV string.
     *
     * @param csvLine The CSV string.
     * @return A Section object.
     */
    public static Section fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int sectionID = Integer.parseInt(fields[0].trim());
        String sectionName = fields[1].trim();
        int sectionCapacity = Integer.parseInt(fields[2].trim());
        int venueID = Integer.parseInt(fields[3].trim());

        // Create the Section object with the Venue retrieved from the controller
        Section section = new Section(sectionID, sectionName, sectionCapacity, applicationController.findVenueByID(venueID));

        // No need to initialize rows; it's already initialized
        return section;
    }
}
