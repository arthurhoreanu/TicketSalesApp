package controller;

import model.Event;
import model.Seat;
import model.Section;
import model.Venue;
import service.SeatService;

import java.util.List;

public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Adds a new seat
    public void createSeat(int seatID, Section section, int rowNumber, int sitNumber, Event reservedForEvent) {
        boolean success = seatService.createSeat(seatID, section, rowNumber, sitNumber, reservedForEvent);
        if (success) {
            System.out.println("Seat has been created");
        }
        else System.out.println("Seat could not be created");
    }

    // Retrieves all seats
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    // Updates an existing seat
    public void updateSeat(int seatID, Section newSection, int newRowNumber, int newSitNumber, Event newReservedForEvent) {
        boolean success = seatService.updateSeat(seatID, newSection, newRowNumber, newSitNumber, newReservedForEvent);
        if (success) {
            System.out.println("Seat has been created");
        }
        else System.out.println("Seat could not be created");
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
