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
@Table(name = "sport_event")
public class SportsEvent extends Event {

    @OneToMany(mappedBy = "sportsEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SportsEventLineUp> sportsEventLineUps;

    public List<SportsEventLineUp> getSportsEventLineUps() {
        return sportsEventLineUps;
    }

    public void setSportsEventLineUps(List<SportsEventLineUp> sportsEventLineUps) {
        this.sportsEventLineUps = sportsEventLineUps;
    }

    public void addAthleteToLineUp(Athlete athlete, Integer lineupOrder) {
        SportsEventLineUp sportsEventLineUp = new SportsEventLineUp(this, athlete);
        sportsEventLineUps.add(sportsEventLineUp);
    }

    public void removeAthleteFromLineUp(Athlete athlete) {
        sportsEventLineUps.removeIf(lineUp -> lineUp.getAthlete().equals(athlete));
    }

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
