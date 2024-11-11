package model;

import java.time.LocalDateTime;
import java.util.List;

public class SportsEvent extends Event {
    private List<Athlete> athletes;
    private String sportName;

    public SportsEvent(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Athlete> athletes) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets);
        this.athletes = athletes;
    }

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    @Override
    public String toString() {
        return "SportsEvent{" + "athletes=" + athletes + '\'' + '}';
    }
}
