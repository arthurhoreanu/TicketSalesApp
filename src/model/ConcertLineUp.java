package model;

public class ConcertLineUp {
    private int eventId;
    private int artistId;

    public ConcertLineUp(int eventId, int artistId) {
        this.eventId = eventId;
        this.artistId = artistId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    @Override
    public String toString() {
        return "ConcertLineUp{" + "eventId=" + eventId + ", artistId=" + artistId + '}';
    }
}
