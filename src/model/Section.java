package model;
import java.util.List;


public class Section implements Identifiable {
    private int sectionID;
    private String sectionName;
    private int sectionCapacity;
    private Venue venue;
    private List<Seat> seats;   //added for the return available seat

    public Section(int sectionID, String sectionName, int sectionCapacity, Venue venue, List<Seat> seats) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionCapacity = sectionCapacity;
        this.venue = venue;
        this.seats =  seats;
    }

    @Override
    public Integer getID() {
        return this.sectionID;
    }

    public Venue venue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<Seat> getSeats(){
        return seats;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSectionCapacity() {
        return sectionCapacity;
    }

    public void setSectionCapacity(int sectionCapacity) {
        this.sectionCapacity = sectionCapacity;
    }

    @Override
    public String toString() {
        return "Section{" + "sectionID=" + sectionID + ", sectionName='" + sectionName + '\'' + ", sectionCapacity=" + sectionCapacity + ", venue=" + venue + '}';
    }

    // TODO method to return the list of available seats
    // public List<Seat> getAvailableSeats() {}
}
