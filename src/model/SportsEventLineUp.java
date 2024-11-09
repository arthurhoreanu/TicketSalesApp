package model;

public class SportsEventLineUp {
    private int eventID;
    private int athleteID;

    public SportsEventLineUp(int eventId, int artistId) {
        this.eventID = eventId;
        this.athleteID = artistId;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getArtistId() {
        return athleteID;
    }

    public void setArtistId(int artistId) {
        this.athleteID = artistId;
    }

    @Override
    public String toString() {
        return "SportsEventLineUp{" + "eventId=" + eventID + ", athleteID=" + athleteID + '}';
    }
}
