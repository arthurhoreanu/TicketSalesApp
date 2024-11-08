package model;

import java.util.List;

public class Venue implements Identifiable {
    private int venueID;
    private String venueName;
    private String location;
    private int venueCapacity;
    public List<Section> sections; //added fot the get available seats

    public Venue(int venueID, String venueName, String location, int venueCapacity, List<Section> sections) {
        this.venueID = venueID;
        this.venueName = venueName;
        this.location = location;
        this.venueCapacity = venueCapacity;
        this.sections = sections;
    }

    @Override
    public Integer getID() {
        return this.venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public List<Section> getSections(){
        return this.sections;
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
