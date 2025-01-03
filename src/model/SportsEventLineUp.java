package model;

import controller.Controller;

import javax.persistence.*;

/**
 * Represents the association between a SportsEvent and an Athlete.
 */
@Entity
@Table(name = "sports_event_lineup")
public class SportsEventLineUp implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sports_event_lineup_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sports_event_id", nullable = false)
    private SportsEvent sportsEvent;

    @ManyToOne
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;

    static Controller controller = ControllerProvider.getController();

    public SportsEventLineUp() {}

    public SportsEventLineUp(SportsEvent sportsEvent, Athlete athlete) {
        this.sportsEvent = sportsEvent;
        this.athlete = athlete;
    }

    public Integer getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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
                "id=" + id +
                ", sportsEvent=" + sportsEvent +
                ", athlete=" + athlete +
                '}';
    }

    @Override
    public String toCsv() {
        return id + "," + sportsEvent.getID() + "," + athlete.getID();
    }

    public static SportsEventLineUp fromCsv(String csv) {
        String[] fields = csv.split(",");
        int id = Integer.parseInt(fields[0]);
        int sportsEventID = Integer.parseInt(fields[1]);
        int athleteID = Integer.parseInt(fields[2]);
        SportsEvent sportsEvent = controller.findSportsEventByID(sportsEventID);
        Athlete athlete = controller.findAthleteByID(athleteID);
        SportsEventLineUp sportsEventLineUp = new SportsEventLineUp(sportsEvent, athlete);
        sportsEventLineUp.setID(id);
        return sportsEventLineUp;
    }
}