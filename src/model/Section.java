package model;

public class Section {
    private int sectionID;
    private String sectionName;
    private int capacity;

    public Section(int sectionID, String sectionName, int capacity) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.capacity = capacity;
    }

    public int sectionID() {
        return sectionID;
    }

    public Section setSectionID(int sectionID) {
        this.sectionID = sectionID;
        return this;
    }

    public String sectionName() {
        return sectionName;
    }

    public Section setSectionName(String sectionName) {
        this.sectionName = sectionName;
        return this;
    }

    public int capacity() {
        return capacity;
    }

    public Section setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

   // public int getAvailableSeat()
   //TODO talk about the fucking Seat-Section-Venue-Event connection
}
