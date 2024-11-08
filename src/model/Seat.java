package model;

public class Seat implements Identifiable {
    private int seatID;
    private Section section;
    private int rowNumber;
    private int sitNumber;
    private Event reservedForEvent;


    public Seat(int seatID, int rowNumber, Section section, int sitNumber, Event reservedForEvent) {
        this.seatID = seatID;
        this.rowNumber = rowNumber;
        this.section = section;
        this.sitNumber = sitNumber;
        this.reservedForEvent = null; //init, the seat is not reserved
    }

    @Override
    public Integer getID() {
        return this.seatID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getRowNumber() {
        return this.rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSitNumber() {
        return this.sitNumber;
    }

    public void setSitNumber(int sitNumber) {
        this.sitNumber = sitNumber;
    }

    public Event getReservedForEvent() {
        return this.reservedForEvent;
    }

    public void setReservedForEvent(Event reservedForEvent) {
        this.reservedForEvent = reservedForEvent;
    }

     //verifies if the seat is reserved for a specific event
     public boolean isReservedForEvent(Event event) {
         return reservedForEvent != null && reservedForEvent.equals(event);
     }

     //mark the seat as reserved for a specific event
     public void reserveForEvent(Event event) {
         if (reservedForEvent == null || !reservedForEvent.equals(event)) {
             reservedForEvent = event;
         }
     }

     //clear the reservation for a specific event
     public void clearReservation(Event event) {
         if (reservedForEvent != null && reservedForEvent.equals(event)) {
             this.reservedForEvent = null;
         }
     }

    @Override
    public String toString() {
        return "Seat{" + "seatID=" + seatID + ", section=" + section + ", rowNumber=" + rowNumber + ", sitNumber=" + sitNumber + ", reservedForEvent=" + reservedForEvent + '}';
    }
}

