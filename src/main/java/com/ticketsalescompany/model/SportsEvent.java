package com.ticketsalescompany.model;

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
@Table(name = "sport_event")
public class SportsEvent extends Event {

    @ManyToMany
    @JoinTable(
            name = "sport_event_lineup", // Tabelul de legătură specific pentru SportsEvent și Athlete
            joinColumns = @JoinColumn(name = "event_id"), // Coloana care leagă SportsEvent
            inverseJoinColumns = @JoinColumn(name = "athlete_id") // Coloana care leagă Athlete
    )
    private List<Athlete> athletes;

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    public SportsEvent() {}

    public SportsEvent(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                       LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

    @Override
    public String toString() {
        return "SportsEvent{" +
                "athletes=" + athletes +
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
