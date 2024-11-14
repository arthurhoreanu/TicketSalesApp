package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a concert event, which is a type of event featuring a list of artists.
 */
public class Concert extends Event {
    private List<Artist> artists;

    @Override
    public String toCsvFormat() {
        return "";
    }

    /**
     * Constructs a Concert with the specified details and list of performing artists.
     * @param eventID          the unique ID of the concert
     * @param eventName        the name of the concert
     * @param eventDescription a description of the concert
     * @param startDateTime    the start date and time of the concert
     * @param endDateTime      the end date and time of the concert
     * @param venue            the venue where the concert takes place
     * @param eventStatus      the current status of the concert (SCHEDULED, CANCELLED, COMPLETED)
     * @param artists          the list of artists performing at the concert
     */
    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Artist> artists) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus);
        this.artists = artists;
    }

    /**
     * Gets the list of artists performing at the concert.
     * @return a list of artists performing at the concert
     */
    public List<Artist> getArtists() {
        return artists;
    }

    /**
     * Sets the list of artists performing at the concert.
     * @param artists the new list of artists performing at the concert
     */
    public void setArtist(List<Artist> artists) {
        this.artists = artists;
    }

    /**
     * Returns a string representation of the concert, including its details and the list of artists.
     * @return a string representing the concert's details and artists
     */
    @Override
    public String toString() {
        return "Concert" + super.toString() +
                ", {" + "artists=" + artists + '}';
    }

}
