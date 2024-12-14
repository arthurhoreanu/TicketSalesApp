package model;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a concert event, which is a type of event featuring a list of artists.
 */
@Entity
@Table(name = "concert")
public class Concert extends Event {

    @ManyToMany
    @JoinTable(
            name = "lineup",
            joinColumns = @JoinColumn(name = "eventid"),
            inverseJoinColumns = @JoinColumn(name = "performerid")
    )
    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Concert() {}

    /**
     * Constructs a Concert with the specified details and list of performing artists.
     * @param eventID          the unique ID of the concert
     * @param eventName        the name of the concert
     * @param eventDescription a description of the concert
     * @param startDateTime    the start date and time of the concert
     * @param endDateTime      the end date and time of the concert
     * @param venueID            the venue where the concert takes place
     * @param eventStatus      the current status of the concert (SCHEDULED, CANCELLED, COMPLETED)
     */
    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getConcertID() {
        return super.getID();
    }

    @Override
    @Column(name = "eventname", nullable = false)
    public String getEventName() {
        return super.getEventName();
    }

    @Override
    @Column(name = "eventdescription", nullable = false)
    public String getEventDescription() {
        return super.getEventDescription();
    }

    @Override
    @Column(name = "startdatetime", nullable = false)
    public LocalDateTime getStartDateTime() {
        return super.getStartDateTime();
    }

    @Override
    @Column(name = "enddatetime", nullable = false)
    public LocalDateTime getEndDateTime() {
        return super.getEndDateTime();
    }

    @Override
    @Column(name = "venueid", nullable = false)
    public int getVenueID() {
        return super.getVenueID();
    }

    @Override
    @Column(name = "eventstatus", nullable = false)
    public EventStatus getEventStatus() {
        return super.getEventStatus();
    }

    /**
     * Returns a string representation of the concert, including its details and the list of artists.
     * @return a string representing the concert's details and artists
     */
    @Override
    public String toString() {
        return "Concert" + super.toString() + '}';
    }

    @Override
    public String toCsv() {
        return getID() + "," +
                getEventName() + "," +
                getEventDescription() + "," +
                getStartDateTime() + "," +
                getEndDateTime() + "," +
                getVenueID() + "," +
                getEventStatus();
    }

    public static Concert fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0]);
        String eventName = fields[1];
        String eventDescription = fields[2];
        LocalDateTime startDateTime = LocalDateTime.parse(fields[3]);
        LocalDateTime endDateTime = LocalDateTime.parse(fields[4]);
        int venueID = Integer.parseInt(fields[5]);
        EventStatus eventStatus = EventStatus.valueOf(fields[6]);
        return new Concert(id, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

}
