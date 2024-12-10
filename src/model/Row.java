package model;

import java.util.ArrayList;
import java.util.List;
import controller.Controller;

public class Row implements Identifiable{
    private int rowID;
    private static int rowCounter = 1;
    private int rowCapacity;
    private Section section;
    private List<Seat> seats;
    static Controller controller = ControllerProvider.getController();

    public Row(int rowID, Section section, List<Seat> seats, int rowCapacity ) {
        this.rowID = rowCounter++;
        this.section = section;
        this.seats = new ArrayList<Seat>();
    }

    //TODO CSV to and from CSV methods

    @Override
    public Integer getID(){return this.rowID;}

    @Override
    public String toCsv() {
        return "";
    }

    public static Row fromCsv(String row) {
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
