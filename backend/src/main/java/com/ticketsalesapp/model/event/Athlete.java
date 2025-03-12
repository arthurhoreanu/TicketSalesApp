package com.ticketsalesapp.model.event;

import lombok.Getter;
import lombok.Setter;
import com.ticketsalesapp.model.user.FavouriteEntity;
import com.ticketsalesapp.model.Identifiable;

import javax.persistence.*;
import java.util.List;

/**
 * Represents an athlete with an ID, name, and sport. Implements Identifiable and FavouriteEntity interfaces.
 */
@Entity
@Table(name = "athlete")
public class Athlete implements Identifiable, FavouriteEntity {

    public Athlete() {}

    @Id
    @Column(name = "athlete_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int athleteID;

    /**
     * -- SETTER --
     *  Sets a new name for the athlete.
     * -- GETTER --
     *  Gets the athlete's name.
     *
     */
    @Column(name = "athlete_name", nullable = false)
    private String athleteName;

    public String getAthleteSport() {
        return athleteSport;
    }

    @Column(name = "athlete_sport", nullable = false)
    private String athleteSport;

    @Transient
    @ManyToMany
    @JoinTable(
            name = "sports_event_lineup",
            joinColumns = @JoinColumn(name = "athlete_id"),
            inverseJoinColumns = @JoinColumn(name = "sports_event_id")
    )
    private List<SportsEvent> sportsEvents;

    /**
     * Constructs an Athlete with the specified ID, name, and sport.
     * @param athleteID     the unique ID of the athlete
     * @param athleteName   the name of the athlete
     * @param athleteSport  the sport associated with the athlete
     */
    public Athlete(int athleteID, String athleteName, String athleteSport) {
        this.athleteID = athleteID;
        this.athleteName = athleteName;
        this.athleteSport = athleteSport;
    }

    /**
     * Gets the unique ID of the athlete.
     * @return the athlete's ID
     */
    @Override
    public Integer getId() {
        return this.athleteID;
    }

    public void setId(int id) {
        this.athleteID = id;
    }

    /**
     * Gets the name of the athlete.
     * @return the athlete's name
     */
    @Override
    public String getName() {
        return this.athleteName;
    }

    /**
     * Returns a string representation of the athlete, including athleteID, athleteName, and athleteSport.
     * @return a string representing the athlete's details
     */
    @Override
    public String toString() {
        return "Athlete{" + "athleteID=" + athleteID + ", athleteName='" + athleteName + '\'' + ", athleteSport='" + athleteSport + '\'' + '}';
    }

    public int getAthleteID() {
        return athleteID;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public List<SportsEvent> getSportsEvents() {
        return sportsEvents;
    }

    public void setAthleteID(int athleteID) {
        this.athleteID = athleteID;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public void setAthleteSport(String athleteSport) {
        this.athleteSport = athleteSport;
    }

    public void setSportsEvents(List<SportsEvent> sportsEvents) {
        this.sportsEvents = sportsEvents;
    }
}
