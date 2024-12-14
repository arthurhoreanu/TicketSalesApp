package model;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a sports event, extending the general Event class with a list of participating athletes.
 */
@Entity
@Table(name = "sportsevent")
public class SportsEvent extends Event {

    @ManyToMany
    @JoinTable(
            name = "lineup",
            joinColumns = @JoinColumn(name = "eventid"),
            inverseJoinColumns = @JoinColumn(name = "performerid")
    )
    private List<Athlete> athletes;

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    protected SportsEvent() {}

    /**
     * Constructs a SportsEvent with the specified attributes.
     * @param eventID         the unique ID of the event
     * @param eventName       the name of the event
     * @param eventDescription a description of the event
     * @param startDateTime   the start date and time of the event
     * @param endDateTime     the end date and time of the event
     * @param venueID           the venue where the event is held
     * @param eventStatus     the current status of the event (SCHEDULED, CANCELLED, COMPLETED)
     */
    public SportsEvent(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getSportsEventID() {
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
     * Returns a string representation of the sports event, including its athletes.
     * @return a string representing the sports event details
     */
    @Override
    public String toString() {
        return "SportsEvent{" +  ", " + super.toString() + '}';
    }

    @Override
    public String toCsv() {
        return getID() + "," +
                getEventName() + "," +
                getEventDescription() + "," +
                getStartDateTime() + "," +
                getEndDateTime() + "," +
                getVenueID() + "," +
                getEventStatus() + ",";
    }

    public static SportsEvent fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0]);
        String eventName = fields[1];
        String eventDescription = fields[2];
        LocalDateTime startDateTime = LocalDateTime.parse(fields[3]);
        LocalDateTime endDateTime = LocalDateTime.parse(fields[4]);
        int venueID = Integer.parseInt(fields[5]);
        EventStatus eventStatus = EventStatus.valueOf(fields[6]);
        return new SportsEvent(id,eventName,eventDescription,startDateTime,endDateTime,venueID,eventStatus);
    }
}
