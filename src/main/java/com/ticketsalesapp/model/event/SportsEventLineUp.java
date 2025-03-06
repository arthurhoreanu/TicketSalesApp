package main.java.com.ticketsalesapp.model.event;

import lombok.Getter;
import lombok.Setter;
import main.java.com.ticketsalesapp.controller.ApplicationController;
import main.java.com.ticketsalesapp.model.ControllerProvider;
import main.java.com.ticketsalesapp.model.Identifiable;

import javax.persistence.*;
import java.util.Optional;

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

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "sports_event_id", nullable = false)
    private SportsEvent sportsEvent;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;

    static ApplicationController applicationController = ControllerProvider.getController();

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
        Optional<SportsEvent> sportsEvent = applicationController.findSportsEventByID(sportsEventID);
        Athlete athlete = applicationController.findAthleteByID(athleteID);
        SportsEventLineUp sportsEventLineUp = new SportsEventLineUp(sportsEvent.get(), athlete);
        sportsEventLineUp.setID(id);
        return sportsEventLineUp;
    }
}