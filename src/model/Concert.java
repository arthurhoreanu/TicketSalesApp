package model;

import service.TicketService;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a concert event, which is a type of event featuring a list of artists.
 */
@Entity
@Table(name = "concert")
public class Concert extends Event {

    @ManyToMany
    @JoinTable(
            name = "concert_lineup", // Tabelul de legătură specific pentru Concert și Artist
            joinColumns = @JoinColumn(name = "event_id"), // Coloana care leagă Concert
            inverseJoinColumns = @JoinColumn(name = "artist_id") // Coloana care leagă Artist
    )
    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
    @Transient
    private TicketService ticketService;
    @Override
    public int getTicketsSold() {
        return TicketService.getTicketsByEvent(this).size();
    }

    public Concert() {}

    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                   LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        this.ticketService = ControllerProvider.getController().getTicketService();
    }

    @Override
    public String toString() {
        return "Concert{" +
                "artists=" + artists +
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
                getEventStatus();
    }

    public static Concert fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0]);
        String eventName = fields[1];
        String eventDescription = fields[2];
        LocalDateTime startDateTime = LocalDateTime.parse(fields[3]);
        LocalDateTime endDateTime = LocalDateTime.parse(fields[4]);
        int venueID = Integer.parseInt(fields[5]);
        EventStatus eventStatus = EventStatus.valueOf(fields[6]);
        return new Concert(id, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

}
