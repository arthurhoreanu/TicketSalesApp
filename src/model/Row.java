package model;

import java.util.ArrayList;
import java.util.List;
import controller.Controller;

public class Row implements Identifiable{
    private int rowID;
    private static int rowCounter = 1; // Counter for generating unique row IDs
    private Section section;
    private List<Seat> seats;
    static Controller controller = ControllerProvider.getController();

    public Row(int rowID, Section section) {
        this.rowID = rowID;
        this.section = section;
        this.seats = new ArrayList<Seat>();
        this.rowID= rowCounter++;
    }

    //TODO CSV to and from CSV methods

    @Override
    public Integer getID(){return this.rowID;}

    @Override
    public String toCsvFormat() {
        return "";
    }

    public static Row fromCsvFormat(String row) {
        return null;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    //todo should it be directly in service??
    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }

    @Override
    public String toString(){
        return "RowID: " + rowID + ", Section: " + section;
    }

}
