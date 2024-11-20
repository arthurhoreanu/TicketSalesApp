package model;

import controller.Controller;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a section within a venue, containing details such as name, capacity, associated venue, and seats.
 */
public class Section implements Identifiable {
    private int sectionID;
    private String sectionName;
    private int sectionCapacity;
    private Venue venue;
    private List<Seat> seats;

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
        String venueDetails = fields[3].trim();
        String seatsDetails = fields[4].trim();

        Venue venue = Venue.fromCsvFormat(venueDetails);
        List<Seat> seats = new ArrayList<>();
        if (!seatsDetails.equals("null")) {
            String[] seatIds = seatsDetails.split(";");
            for (String seatId : seatIds) {
                Seat seat = controller.findSeatByID(Integer.parseInt(seatId.trim()));
                if (seat != null) {
                    seats.add(seat);
                }
            }
        }

        return new Section(sectionID, sectionName, sectionCapacity, venue, seats);
    }

    /**
     * Converts the Section object into a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the Section.
     */
    @Override
    public String toCsvFormat() {
        String seatIds = seats.isEmpty() ? "null" : seats.stream()
                .map(seat -> String.valueOf(seat.getID()))
                .reduce((a, b) -> a + ";" + b)
                .orElse("null");

        return String.join(",",
                String.valueOf(sectionID),
                sectionName,
                String.valueOf(sectionCapacity),
                venue.getVenueName(),
                seatIds
        );
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
