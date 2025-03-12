package main.java.com.ticketsalesapp.model.event;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a sports event, extending the general Event class with a list of participating athletes.
 */
@Entity
@Table(name = "sports_event")
public class SportsEvent extends Event {

    @Id
    @Column(name = "sports_event_id", nullable = false)
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

    @Transient
    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @Transient
    @ManyToMany(mappedBy = "sportsEvents", cascade = CascadeType.ALL)
    private List<Athlete> athletes;

    public SportsEvent() {}

    public SportsEvent(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                       LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

    @Override
    public String toString() {
        return "SportsEvent{" +
                ", " + super.toString() +
                '}';
    }
}
