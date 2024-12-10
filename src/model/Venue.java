package model;

import controller.Controller;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a venue for events, containing information about its ID, name, location, capacity, and sections.
 */
public class Venue implements Identifiable {
    private static int venueCounter = 1;
    private int venueID;
    private String venueName;
    private String location;
    private int venueCapacity;
    public List<Section> sections;


    /**
     * Constructs a Venue with the specified ID, name, location, capacity, and sections.
     * @param venueID       the unique ID of the venue
     * @param venueName     the name of the venue
     * @param location      the location of the venue
     * @param venueCapacity the capacity of the venue
     * @param sections      the list of sections within the venue
     */
    public Venue(int venueID, String venueName, String location, int venueCapacity, List<Section> sections) {
        this.venueID = venueCounter++;
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
        this.sections = new ArrayList<>();
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

    static Controller controller = ControllerProvider.getController();

    /**
     * Creates a Venue object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string.
     * @return the deserialized Venue object.
     */
    public static Venue fromCsvFormat(String csvLine) {
        String[] fields = csvLine.split(",");
        int venueID = Integer.parseInt(fields[0].trim());
        String venueName = fields[1].trim();
        String location = fields[2].trim();
        int capacity = Integer.parseInt(fields[3].trim());
        String sectionsDetails = fields[4].trim();

        List<Section> sections = new ArrayList<>();
        if (!sectionsDetails.equals("null")) {
            String[] sectionIDs = sectionsDetails.split(";");
            for (String sectionID : sectionIDs) {
                // Delegate to the SectionController through the ControllerProvider
                Section section = controller.findSectionByID(Integer.parseInt(sectionID.trim()));
                if (section != null) {
                    sections.add(section);
                }
            }
        }

        return new Venue(venueID, venueName, location, capacity, sections);
    }

    /**
     * Converts the Venue object into a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the Venue.
     */
    @Override
    public String toCsvFormat() {
        String sectionIds = sections.isEmpty() ? "null" : sections.stream()
                .map(section -> String.valueOf(section.getID()))
                .reduce((a, b) -> a + ";" + b)
                .orElse("null");
        return String.join(",",
                String.valueOf(venueID),
                venueName,
                location,
                String.valueOf(venueCapacity),
                sectionIds
        );
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

    //todo talk about those methods in service directly
    public void addSections(Section section){
        this.sections.add(section);
    }

}
