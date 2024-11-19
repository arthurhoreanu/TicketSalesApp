package model;

import controller.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract event with a unique ID, name, description, schedule, venue, and status.
 */
public abstract class Event implements Identifiable {
    private int eventID;
    private String eventName;
    private String eventDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Venue venue;
    private EventStatus eventStatus;
    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs an Event with the specified details.
     * @param eventID          the unique ID of the event
     * @param eventName        the name of the event
     * @param eventDescription a description of the event
     * @param startDateTime    the start date and time of the event
     * @param endDateTime      the end date and time of the event
     * @param venue            the venue where the event takes place
     * @param eventStatus      the current status of the event (SCHEDULED, CANCELLED, COMPLETED)
     */
    public Event(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                 LocalDateTime endDateTime, Venue venue, EventStatus eventStatus) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.venue = venue;
        this.eventStatus = eventStatus;
    }

    /**
     * Gets the unique ID of the event.
     * @return the event ID
     */
    @Override
    public Integer getID() {
        return this.eventID;
    }

    /**
     * Gets the name of the event.
     * @return the name of the event
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets a new name for the event.
     * @param eventName the new name of the event
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the description of the event.
     * @return the description of the event
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets a new description for the event.
     * @param eventDescription the new description of the event
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Gets the start date and time of the event.
     * @return the start date and time of the event
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets a new start date and time for the event.
     * @param startDateTime the new start date and time of the event
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the end date and time of the event.
     * @return the end date and time of the event
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets a new end date and time for the event.
     * @param endDateTime the new end date and time of the event
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the venue where the event is held.
     * @return the venue of the event
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * Sets a new venue for the event.
     * @param venue the new venue for the event
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * Gets the current status of the event (SCHEDULED, CANCELLED, COMPLETED).
     * @return the status of the event
     */
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    /**
     * Sets a new status for the event.
     * @param eventStatus the new status of the event
     */
    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * Returns a string representation of the event, including its ID, name, description, schedule, venue, and status.
     * @return a string representing the event's details
     */
    @Override
    public String toString() {
        return "{" + eventID + ", eventName=" + eventName + ", eventDescription=" + eventDescription +
                ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", venue=" + venue +
                ", eventStatus=" + eventStatus + '}';
    }

    public static Event fromCsvFormat(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0].trim());
        String type = fields[1].trim();
        String eventName = fields[2].trim();
        String eventDescription = fields[3].trim();
        LocalDateTime startDateTime = LocalDateTime.parse(fields[4].trim());
        LocalDateTime endDateTime = LocalDateTime.parse(fields[5].trim());
        int venueID = Integer.parseInt(fields[6].trim());
        EventStatus status = EventStatus.valueOf(fields[7].trim());

        Venue venue = controller.findVenueById(venueID);
        if (venue == null) {
            throw new IllegalArgumentException("Venue with ID " + venueID + " not found.");
        }

        switch (type) {
            case "Concert": {
                String[] artistNames = fields[8].trim().split(";");
                List<Artist> artists = new ArrayList<>();
                for (String artistName : artistNames) {
                    Artist artist = controller.findArtistByName(artistName.trim());
                    if (artist != null) {
                        artists.add(artist);
                    } else {
                        System.out.println("Warning: Artist with name " + artistName + " not found. Skipping.");
                    }
                }
                return new Concert(id, eventName, eventDescription, startDateTime, endDateTime, venue, status, artists);
            }
            case "Sports Event": {
                String[] athleteNames = fields[8].trim().split(";");
                List<Athlete> athletes = new ArrayList<>();
                for (String athleteName : athleteNames) {
                    Athlete athlete = controller.findAthleteByName(athleteName.trim());
                    if (athlete != null) {
                        athletes.add(athlete);
                    } else {
                        System.out.println("Warning: Athlete with name " + athleteName + " not found. Skipping.");
                    }
                }
                return new SportsEvent(id, eventName, eventDescription, startDateTime, endDateTime, venue, status, athletes);
            }
            default:
                throw new IllegalArgumentException("Unknown event type: " + type);
        }
    }

}