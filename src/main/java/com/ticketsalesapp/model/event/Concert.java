package main.java.com.ticketsalesapp.model.event;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a concert event, which is a type of event featuring a list of artists.
 */
@Entity
@Table(name = "concert")
public class Concert extends Event {

    @Id
    @Column(name = "concert_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int eventID;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_description", nullable = false)
    private String eventDescription;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "venue_id", nullable = false)
    private int venueID;

    @Column(name = "event_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @ManyToMany(mappedBy = "concerts", cascade = CascadeType.ALL)
    private List<Artist> artists;

    public Concert() {}

    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                   LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

    @Override
    public String toString() {
        return "Concert{" +
                ", " + super.toString() +
                '}';
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
