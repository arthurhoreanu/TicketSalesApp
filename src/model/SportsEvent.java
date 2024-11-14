package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a sports event, extending the general Event class with a list of participating athletes.
 */
public class SportsEvent extends Event {
    private List<Athlete> athletes;

    @Override
    public String toCsvFormat() {
        return "";
    }

    /**
     * Constructs a SportsEvent with the specified attributes.
     * @param eventID         the unique ID of the event
     * @param eventName       the name of the event
     * @param eventDescription a description of the event
     * @param startDateTime   the start date and time of the event
     * @param endDateTime     the end date and time of the event
     * @param venue           the venue where the event is held
     * @param eventStatus     the current status of the event (SCHEDULED, CANCELLED, COMPLETED)
     * @param athletes        the list of athletes participating in the event
     */
    public SportsEvent(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Athlete> athletes) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus);
        this.athletes = athletes;
    }

    /**
     * Gets the list of athletes participating in the sports event.
     * @return the list of participating athletes
     */
    public List<Athlete> getAthletes() {
        return athletes;
    }

    /**
     * Returns a string representation of the sports event, including its athletes.
     * @return a string representing the sports event details
     */
    @Override
    public String toString() {
        return "SportsEvent{" +
                "athletes=" + athletes +
                ", " + super.toString() +
                '}';
    }
}
