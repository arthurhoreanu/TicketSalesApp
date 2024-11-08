package model;

import java.time.LocalDateTime;
import java.util.List;

public class Concert extends Event {
    private Artist artist;
    private String genre;

    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, Artist artist, String genre) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets);
        this.artist = artist;
        this.genre = genre;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Concert{" + "artist=" + artist + ", genre=" + genre + '}';
    }
}
