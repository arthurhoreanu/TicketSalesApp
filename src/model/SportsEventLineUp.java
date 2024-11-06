package model;

public class SportsEventLineUp {
    private int eventId;
    private int athleteID;

    public SportsEventLineUp(int eventId, int artistId) {
        this.eventId = eventId;
        this.athleteID = artistId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getArtistId() {
        return athleteID;
    }

    public void setArtistId(int artistId) {
        this.athleteID = artistId;
    }

    @Override
    public String toString() {
        return "SportsEventLineUp{" +
                "eventId=" + eventId +
                ", athleteID=" + athleteID +
                '}';
    }
}
