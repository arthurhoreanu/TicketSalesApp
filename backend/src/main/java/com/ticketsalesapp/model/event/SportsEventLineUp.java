package main.java.com.ticketsalesapp.model.event;

import lombok.Getter;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.Identifiable;

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

    public SportsEventLineUp() {}

    public SportsEventLineUp(SportsEvent sportsEvent, Athlete athlete) {
        this.sportsEvent = sportsEvent;
        this.athlete = athlete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
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
}