package controller;

import model.Event;
import model.Seat;
import model.Section;
import service.SeatService;

import java.util.List;

public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Creates a new seat in a section
    public String createSeat(int seatID, Section section, int rowNumber, int sitNumber, Event reservedForEvent) {
        boolean isCreated = seatService.createSeat(seatID, section, rowNumber, sitNumber, reservedForEvent);
        if (isCreated) {
            return "Seat created successfully with ID: " + seatID + " in section: " + section.getSectionName();
        } else {
            return "Seat with ID: " + seatID + " already exists.";
        }
    }

    // Retrieves a seat by its ID
    public String findSeatById(int seatID) {
        Seat seat = seatService.findSeatById(seatID);
        if (seat != null) {
            return "Found seat with ID: " + seatID + " - Row: " + seat.getRowNumber() + ", Seat: " + seat.getSitNumber();
        } else {
            return "Seat with ID: " + seatID + " does not exist.";
        }
    }

    // Checks if a seat is reserved for a specific event
    public String checkSeatReservation(Seat seat, Event event) {
        boolean isReserved = seatService.isSeatReservedForEvent(seat, event);
        if (isReserved) {
            return "Seat " + seat.getSitNumber() + " in row " + seat.getRowNumber() + " is reserved for event: " + event.getEventName();
        } else {
            return "Seat " + seat.getSitNumber() + " in row " + seat.getRowNumber() + " is not reserved for event: " + event.getEventName();
        }
    }

    // Reserves a seat for a specific event
    public String reserveSeatForEvent(Seat seat, Event event) {
        boolean isReserved = seatService.reserveSeatForEvent(seat, event);
        if (isReserved) {
            return "Seat " + seat.getSitNumber() + " in row " + seat.getRowNumber() + " has been successfully reserved for event: " + event.getEventName();
        } else {
            return "Seat " + seat.getSitNumber() + " in row " + seat.getRowNumber() + " is already reserved for event: " + event.getEventName();
        }
    }

    // Clears the reservation for a specific event
    public String clearSeatReservationForEvent(Seat seat, Event event) {
        boolean isCleared = seatService.clearSeatReservationForEvent(seat, event);
        if (isCleared) {
            return "Reservation cleared for seat " + seat.getSitNumber() + " in row " + seat.getRowNumber() + " for event: " + event.getEventName();
        } else {
            return "Seat " + seat.getSitNumber() + " in row " + seat.getRowNumber() + " was not reserved for event: " + event.getEventName();
        }
    }

    // Recommends a front-row seat from a list of available seats
    public String recommendFrontRowSeat(List<Seat> availableSeats) {
        Seat recommendedSeat = seatService.recommendFrontRowSeat(availableSeats);
        if (recommendedSeat != null) {
            return "Recommended front-row seat: Row " + recommendedSeat.getRowNumber() + ", Seat " + recommendedSeat.getSitNumber();
        } else {
            return "No available seats to recommend.";
        }
    }
}
