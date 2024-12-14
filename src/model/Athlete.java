package model;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents an athlete with an ID, name, and sport. Implements Identifiable and FavouriteEntity interfaces.
 */
@Entity
@Table(name = "athlete")
public class Athlete implements Identifiable, FavouriteEntity {

    public Athlete() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int athleteID;

    @Column(name = "athleteName", nullable = false)
    private String athleteName;

    @Column(name = "athleteSport", nullable = false)
    private String athleteSport;

    @ManyToMany(mappedBy = "athletes")
    private List<SportsEvent> sportsEvents;

    public List<SportsEvent> getSportsEvents() {
        return sportsEvents;
    }

    public void setSportsEvents(List<SportsEvent> sportsEvents) {
        this.sportsEvents = sportsEvents;
    }

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
    public Integer getID() {
        return this.athleteID;
    }

    public void setID(int id) {
        this.athleteID = id; // Pentru Athlete
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
     * Gets the athlete's name.
     * @return the athlete's name
     */
    public String getAthleteName() {
        return athleteName;
    }

    /**
     * Sets a new name for the athlete.
     * @param athleteName the new name of the athlete
     */
    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    /**
     * Gets the sport associated with the athlete.
     * @return the sport of the athlete
     */
    public String getAthleteSport() {
        return athleteSport;
    }

    /**
     * Sets a new sport for the athlete.
     * @param athleteSport the new sport of the athlete
     */
    public void setAthleteSport(String athleteSport) {
        this.athleteSport = athleteSport;
    }

    /**
     * Converts the current Athlete object into a PreparedStatement for database operations.
     * @param stmt the PreparedStatement to populate with Artist data.
     * @throws SQLException if an SQL error occurs.
     */
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, this.athleteID);
        stmt.setString(2, this.athleteName);
        stmt.setString(3, this.athleteSport);
    }

    /**
     * Creates an Athlete object from a ResultSet.
     * @param rs the ResultSet containing Athlete data.
     * @return an Athlete object populated with data from the ResultSet.
     * @throws SQLException if an SQL error occurs.
     */
    public static Athlete fromDatabase(ResultSet rs) throws SQLException {
        int athleteID = rs.getInt("athleteID");
        String artistName = rs.getString("athleteName");
        String genre = rs.getString("athleteSport");
        return new Athlete(athleteID, artistName, genre);
    }

    /**
     * Returns a string representation of the athlete, including athleteID, athleteName, and athleteSport.
     * @return a string representing the athlete's details
     */
    @Override
    public String toString() {
        return "Athlete{" + "athleteID=" + athleteID + ", athleteName='" + athleteName + '\'' + ", athleteSport='" + athleteSport + '\'' + '}';
    }

    public static Athlete fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int athleteID = Integer.parseInt(fields[0]);
        String athleteName = fields[1];
        String athleteSport = fields[2];
        return new Athlete(athleteID, athleteName, athleteSport);
    }

    @Override
    public String toCsv() {
        return getID() + "," + getName() + "," + getAthleteSport();
    }
}
