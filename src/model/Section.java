package model;

import controller.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Represents a section within a venue, containing details such as name, capacity, associated venue, and seats.
 */
public class Section implements Identifiable {
    private int sectionID;
    private String sectionName;
    private int sectionCapacity;
    private Venue venue;
    private List<Row>  rows = new ArrayList<>();
    static Controller controller = ControllerProvider.getController();

    /**
     * Creates a Section object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string.
     * @return the deserialized Section object.
     */
    public static Section fromCsvFormat(String csvLine) {
        String[] fields = csvLine.split(",");
        int sectionID = Integer.parseInt(fields[0].trim());
        String sectionName = fields[1].trim();
        int sectionCapacity = Integer.parseInt(fields[2].trim());
        Venue venue = controller.findVenueByName(fields[3].trim());
        List<Map<Integer, Seat>> rows = new ArrayList<>();
        if (!fields[4].trim().equals("null")) {
            String[] rowDetails = fields[4].trim().split(";");
            for (String detail : rowDetails) {
                String[] parts = detail.split("-");
                int rowNumber = Integer.parseInt(parts[0]);
                int seatNumber = Integer.parseInt(parts[1]);
                Seat seat = new Seat(rowNumber * 100 + seatNumber, rowNumber, null, seatNumber, null);
                Map<Integer, Seat> row = new HashMap<>();
                row.put(seatNumber, seat);
                rows.add(row);
            }
        }
        return new Section(sectionID, sectionName, sectionCapacity, venue, rows);
    }


    /**
     * Converts the Section object into a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the Section.
     */

    //TODO repair method taking into consideration Row class dependence
    @Override
    public String toCsvFormat() {
        StringBuilder rowDetails = new StringBuilder();
        for (Map<Integer, Seat> row : rows) {
            for (Seat seat : row.values()) {
                rowDetails.append(seat.getRowNumber()).append("-").append(seat.getSeatNumber()).append(";");
            }
        }
        return String.join(",",
                String.valueOf(sectionID),
                sectionName,
                String.valueOf(sectionCapacity),
                venue.getVenueName(),
                rowDetails.toString()
        );
    }

    /**
     * Constructs a Section with the specified attributes.
     * @param sectionID      the unique ID of the section
     * @param sectionName    the name of the section
     * @param sectionCapacity the seating capacity of the section
     * @param venue          the venue where the section is located
     * @param rows          the list of seats in this section
     */
    public Section(int sectionID, String sectionName, int sectionCapacity, Venue venue, List<Map<Integer, Seat>> rows) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionCapacity = sectionCapacity;
        this.venue = venue;
        this.rows = new ArrayList<>();    }

    /**
     * Gets the unique ID of the section.
     * @return the ID of the section
     */
    @Override
    public Integer getID() {
        return this.sectionID;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
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

    //todo in service?
    public void addRow(Row row) {
        rows.add(row);
    }

}
