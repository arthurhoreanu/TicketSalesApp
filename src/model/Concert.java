package model;
import java.time.LocalDateTime;
import java.util.List;

public class Concert extends Event{
    private Artist artist;
    private String genre;
    private List<String> setlist;

    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                   LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets,
                   Artist artist, String genre, List<String> setlist) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets);
        this.artist = artist;
        this.genre = genre;
        this.setlist = setlist;
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

    public List<String> getSetlist() {
        return setlist;
    }

    public void setSetlist(List<String> setlist) {
        this.setlist = setlist;
    }

}
