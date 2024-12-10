package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a section within a venue, containing details such as name, capacity, and associated venue.
 */
public class Section implements Identifiable {
    private int sectionID;
    private String sectionName;
    private int sectionCapacity;
    private int venueID; // Links to parent Venue

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Section with the specified attributes.
     * Used for loading from external sources (CSV, DB).
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
     * Constructs a Section for in-memory operations, where an ID will be assigned externally.
     *
     * @param sectionName     the name of the section
     * @param sectionCapacity the seating capacity of the section
     * @param venueID         the ID of the venue where the section is located
     */
    public Section(String sectionName, int sectionCapacity, int venueID) {
        this.sectionID = 0; // Default ID, to be set externally
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
//todo findVenueByID in controller
    public Venue getVenue() {
        return controller.findVenueByID(venueID);
    }
//todo findRowsBySectionID in controller
    public List<Row> getRows() {
        return controller.findRowsBySectionID(sectionID);
    }

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

    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, sectionID);
        stmt.setString(2, sectionName);
        stmt.setInt(3, sectionCapacity);
        stmt.setInt(4, venueID);
    }

    public static Section fromDatabase(ResultSet rs) throws SQLException {
        int sectionID = rs.getInt("sectionID");
        String sectionName = rs.getString("sectionName");
        int sectionCapacity = rs.getInt("sectionCapacity");
        int venueID = rs.getInt("venueID");
        return new Section(sectionID, sectionName, sectionCapacity, venueID);
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
}
