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
    public void createSeat(int seatID, Section section, int rowNumber, int seatNumber, Event reservedForEvent) {
        boolean isCreated = seatService.createSeat(seatID, section, rowNumber, seatNumber, reservedForEvent);
        if (isCreated) {
            System.out.println("Seat created successfully with ID: " + seatID + " in section: " + section.getSectionName());
        } else {
            System.out.println("Seat with ID: " + seatID + " already exists.");
        }
    }

    // Retrieves a seat by its ID
    public void findSeatByID(int seatID) {
        Seat seat = seatService.findSeatByID(seatID);
        if (seat != null) {
            System.out.println("Found seat with ID: " + seatID + " - Row: " + seat.getRowNumber() + ", Seat: " + seat.getSeatNumber());
        } else {
            System.out.println("Seat with ID: " + seatID + " does not exist.");
        }
    }

    // Checks if a seat is reserved for a specific event
    public void checkSeatReservation(Seat seat, Event event) {
        boolean isReserved = seatService.isSeatReservedForEvent(seat, event);
        if (isReserved) {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " is reserved for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " is not reserved for event: " + event.getEventName());
        }
    }

    // Reserves a seat for a specific event
    public void reserveSeatForEvent(Seat seat, Event event) {
        boolean isReserved = seatService.reserveSeatForEvent(seat, event);
        if (isReserved) {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " has been successfully reserved for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " is already reserved for event: " + event.getEventName());
        }
    }

    // Clears the reservation for a specific event
    public void clearSeatReservationForEvent(Seat seat, Event event) {
        boolean isCleared = seatService.clearSeatReservationForEvent(seat, event);
        if (isCleared) {
            System.out.println("Reservation cleared for seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " was not reserved for event: " + event.getEventName());
        }
    }

    // Recommends a front-row seat from a list of available seats
    public void recommendFrontRowSeat(List<Seat> availableSeats) {
        Seat recommendedSeat = seatService.recommendFrontRowSeat(availableSeats);
        if (recommendedSeat != null) {
            System.out.println("Recommended front-row seat: Row " + recommendedSeat.getRowNumber() + ", Seat " + recommendedSeat.getSeatNumber());
        } else {
            System.out.println("No available seats to recommend.");
        }
    }
}
