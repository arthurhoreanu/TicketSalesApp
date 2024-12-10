package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Represents a section within a venue, containing details such as name, capacity, associated venue, and seats.
 */
public class Section implements Identifiable {
    private static int sectionCounter = 1;
    private int sectionID;
    private String sectionName;
    private int sectionCapacity;
    private int venueID;

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Section with the specified attributes.
     * @param sectionID      the unique ID of the section
     * @param sectionName    the name of the section
     * @param sectionCapacity the seating capacity of the section
     * @param venueID         the ID of the venue where the section is located
     */
    public Section(int sectionID, String sectionName, int sectionCapacity, int venueID) {
        this.sectionID = sectionCounter++;
        this.sectionName = sectionName;
        this.sectionCapacity = sectionCapacity;
        this.venueID = venueID;
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
    //todo VERY IMPORTANT!!!!
   /* *//**
     * Fetches all rows for this section dynamically.
     *
     * @return a list of rows belonging to this section
     *//*
    public List<Row> getRows() {
        return controller.findRowsBySectionID(sectionID);
    }*/

    /**
     * Creates a Section object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string.
     * @return the deserialized Section object.
     */
    public static Section fromCsv(String csvLine){
        String[] fields = csvLine.split(",");
        int sectionID = Integer.parseInt(fields[0].trim());
        String sectionName = fields[1].trim();
        int sectionCapacity = Integer.parseInt(fields[2].trim());
        int venueID = Integer.parseInt(fields[3].trim());
        return new Section(sectionID, sectionName, sectionCapacity, venueID);
    }

    /**
     * Converts the Section object into a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the Section.
     */
   @Override
   public String toCsv(){
       return String.join(",",
               String.valueOf(sectionID),
               sectionName,
               String.valueOf(sectionCapacity),
               String.valueOf(venueID)
       );
   }

    /**
     * Saves the Section object to a database.
     *
     * @param stmt the PreparedStatement for inserting the section
     * @throws SQLException if a database error occurs
     */
    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, this.sectionID);
        stmt.setString(2, this.sectionName);
        stmt.setInt(3, this.sectionCapacity);
        stmt.setInt(4, this.venueID);
    }
    /**
     * Creates a Section object from a database result set.
     *
     * @param rs the ResultSet containing section data
     * @return the deserialized Section object
     * @throws SQLException if a database error occurs
     */
    public static Section fromDatabase(ResultSet rs) throws SQLException {
        int sectionID = rs.getInt("sectionID");
        String sectionName = rs.getString("sectionName");
        int sectionCapacity = rs.getInt("sectionCapacity");
        int venueID = rs.getInt("venueID");
        return new Section(sectionID, sectionName, sectionCapacity, venueID);
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
    public int getVenue() {
        return venueID;
    }

    /**
     * Sets the venue for this section.
     * @param venueID the venue ID to set
     */
    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    /**
     * Gets the name of the section.
     *
     * @return the name of the section
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the name of the section.
     *
     * @param sectionName the new name of the section
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * Gets the seating capacity of the section.
     *
     * @return the seating capacity of the section
     */
    public int getSectionCapacity() {
        return sectionCapacity;
    }

    /**
     * Sets the seating capacity of the section.
     *
     * @param sectionCapacity the new seating capacity of the section
     */
    public void setSectionCapacity(int sectionCapacity) {
        this.sectionCapacity = sectionCapacity;
    }

}
