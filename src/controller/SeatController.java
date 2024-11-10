package controller;

import model.Event;
import model.Seat;
import model.Venue;
import service.SeatService;

import java.util.List;

public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Adds a new seat
    public boolean addSeat(Seat seat) {
        // TODO fix the error I get here
        //seatService.createSeat(seat);
        return true;
    }

    // Retrieves all seats
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    // Updates an existing seat
    public void updateSeat(Seat updatedSeat) {
        // TODO fix the error I get here
        //return seatService.updateSeat(updatedSeat);
    }

    // Deletes a seat by ID
    public boolean deleteSeat(int seatID) {
        return seatService.deleteSeat(seatID);
    }

    // Checks if a seat is reserved for a specific event
    public boolean isSeatReservedForEvent(Seat seat, Event event) {
        return seatService.isSeatReservedForEvent(seat, event);
    }

    // Reserves a seat for a specific event
    public void reserveSeatForEvent(Seat seat, Event event) {
        seatService.reserveSeatForEvent(seat, event);
    }

    // Clears the reservation for a specific event
    public void clearSeatReservationForEvent(Seat seat, Event event) {
        seatService.clearSeatReservationForEvent(seat, event);
    }

    // Clears any reservation on the seat
    public void clearAllReservations(Seat seat) {
        seatService.clearAllReservations(seat);
    }

    // Retrieves the list of available seats for a specific event in a given venue
    public List<Seat> getAvailableSeats(Venue venue, Event event) {
        return seatService.getAvailableSeats(venue, event);
    }
}
