package model;

public class Seat {
    private int seatID;
    private Section section;
    private int rowNumber;
    private int sitNumber;
    //TODO event/boolean type???
    //TODO we have to talk about the implementation of the methods related to this- I have a plan :)
    private Event reservedForEvent;


    public Seat(int seatID, int rowNumber, Section section, int sitNumber, boolean reservedForEvent) {
        this.seatID = seatID;
        this.rowNumber = rowNumber;
        this.section = section;
        this.sitNumber = sitNumber;
    }

    public int getSeatID() {
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

    public boolean getReservedForEvent() {
        return this.reservedForEvent;
    }

    public void setReservedForEvent(boolean reservedForEvent) {
        this.reservedForEvent = reservedForEvent;
    }

    //verifies if the seat is reserved for a specific event
    public boolean isReservedForEvent(Event event) {
        return reservedForEvent != null && reservedForEvent.equals(event);
    }

    //mark the seat as reserved for a specific event
    public void markAsReserved(Event event) {
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
}

