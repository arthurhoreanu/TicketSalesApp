package model;

import controller.Controller;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the association between a SportsEvent and an Athlete.
 */
@Entity
@Table(name = "sports_event_lineup")
public class SportsEventLineUp implements Identifiable {

    @Id
    @ManyToOne
    @JoinColumn(name = "sports_event_id", nullable = false)
    private SportsEvent sportsEvent;

    @Id
    @ManyToOne
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;

    static Controller controller = ControllerProvider.getController();

    public SportsEventLineUp() {}

    public SportsEventLineUp(SportsEvent sportsEvent, Athlete athlete) {
        this.sportsEvent = sportsEvent;
        this.athlete = athlete;
    }

    public SportsEvent getSportsEvent() {
        return sportsEvent;
    }

    public void setSportsEvent(SportsEvent sportsEvent) {
        this.sportsEvent = sportsEvent;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    @Override
    public String toString() {
        return "SportsEventLineUp{" +
                "sportsEvent=" + sportsEvent +
                ", athlete=" + athlete +
                '}';
    }

    @Override
    public String toCsv() {
        return getSportsEvent().getID() + "," + getAthlete().getID();
    }

    public static SportsEventLineUp fromCsv(String csv) {
        String[] fields = csv.split(",");
        int sportsEventID = Integer.parseInt(fields[0]);
        int athleteID = Integer.parseInt(fields[1]);
        return new SportsEventLineUp(controller.findSportsEventByID(sportsEventID), controller.findAthleteByID(athleteID));
    }

    @Override
    public Integer getID() {
        return 0;
    }

    @Override
    public void setID(int Int) {
    }
}