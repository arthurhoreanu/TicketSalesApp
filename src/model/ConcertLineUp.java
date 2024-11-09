package model;

public class ConcertLineUp {
    private int eventID;
    private int artistId;

    public ConcertLineUp(int eventID, int artistId) {
        this.eventID = eventID;
        this.artistId = artistId;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    @Override
    public String toString() {
        return "ConcertLineUp{" + "eventId=" + eventID + ", artistId=" + artistId + '}';
    }
}
