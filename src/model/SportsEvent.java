package model;

import java.time.LocalDateTime;
import java.util.List;

public class SportsEvent extends Event {
    private List<Athlete> athletes;
    private String sportName;

    public SportsEvent(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Athlete> athletes, String sportName) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets);
        this.athletes = athletes;
        this.sportName = sportName;
    }

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    @Override
    public String toString() {
        return "SportsEvent{" + "athletes=" + athletes + ", sportName='" + sportName + '\'' + '}';
    }

    @Override
    public String getName() {
        return this.getEventName();
    }
}
