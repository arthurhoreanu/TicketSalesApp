package model;

public class Venue {
    private int venueID;
    private String venueName;
    private String location;
    private int venueCapacity;

    public Venue(int venueID, String venueName, String location, int venueCapacity) {
        this.venueID = venueID;
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
    }

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getVenueCapacity() {
        return venueCapacity;
    }

    public void setVenueCapacity(int venueCapacity) {
        this.venueCapacity = venueCapacity;
    }

    @Override
    public String toString() {
        return "Venue{" + "venueID=" + venueID + ", venueName='" + venueName + '\'' + ", location='" + location + '\'' + ", venueCapacity=" + venueCapacity + '}';
    }
}
