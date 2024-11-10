package model;

import java.time.LocalDateTime;
import java.util.List;

public class Concert extends Event {
    private List<Artist> artists;
    private String genre;

    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Artist> artists, String genre) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets);
        this.artists = artists;
        this.genre = genre;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtist(List<Artist> artist) {
        this.artists = artists;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Concert" + super.toString() + // Call the Event's toString method
                ", {" + "artists=" + artists + ", genre='" + genre + '\'' + '}';
    }

    @Override
    public String getName() {
        return getName();
    }
}
