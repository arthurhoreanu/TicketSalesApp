package controller;

import model.Event;
import model.Seat;
import model.Section;
import service.SeatService;

import java.util.List;

/**
 * The SeatController class provides methods to handle operations related to seat management,
 * including creating seats, checking reservations, reserving seats for events, and seat recommendations.
 */
public class SeatController {
    private final SeatService seatService;

    /**
     * Constructs a SeatController with the specified SeatService.
     *
     * @param seatService the service handling seat-related operations
     */
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    /**
     * Creates a new seat in the specified section.
     *
     * @param seatID         the ID of the seat
     * @param section        the section in which the seat is created
     * @param rowNumber      the row number of the seat
     * @param seatNumber     the seat number in the row
     * @param reservedForEvent the event for which the seat may be reserved
     */
    public void createSeat(int seatID, Section section, int rowNumber, int seatNumber, Event reservedForEvent) {
        boolean isCreated = seatService.createSeat(seatID, section, rowNumber, seatNumber, reservedForEvent);
        if (isCreated) {
            System.out.println("Seat created successfully with ID: " + seatID + " in section: " + section.getSectionName());
        } else {
            System.out.println("Seat with ID: " + seatID + " already exists.");
        }
    }

    /**
     * Retrieves and prints information about a seat by its ID.
     *
     * @param seatID the ID of the seat to retrieve
     */
    public void findSeatByID(int seatID) {
        Seat seat = seatService.findSeatByID(seatID);
        if (seat != null) {
            System.out.println("Found seat with ID: " + seatID + " - Row: " + seat.getRowNumber() + ", Seat: " + seat.getSeatNumber());
        } else {
            System.out.println("Seat with ID: " + seatID + " does not exist.");
        }
    }

    /**
     * Checks and prints whether a specific seat is reserved for a given event.
     *
     * @param seat  the seat to check
     * @param event the event for which reservation status is checked
     */
    public void checkSeatReservation(Seat seat, Event event) {
        boolean isReserved = seatService.isSeatReservedForEvent(seat, event);
        if (isReserved) {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " is reserved for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " is not reserved for event: " + event.getEventName());
        }
    }

    /**
     * Reserves a specific seat for a given event and prints the reservation status.
     *
     * @param seat  the seat to reserve
     * @param event the event for which the seat is reserved
     */
    public void reserveSeatForEvent(Seat seat, Event event) {
        boolean isReserved = seatService.reserveSeatForEvent(seat, event);
        if (isReserved) {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " has been successfully reserved for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " is already reserved for event: " + event.getEventName());
        }
    }

    /**
     * Clears the reservation for a specific seat and event, and prints the result.
     *
     * @param seat  the seat for which the reservation is cleared
     * @param event the event for which the reservation is being cleared
     */
    public void clearSeatReservationForEvent(Seat seat, Event event) {
        boolean isCleared = seatService.clearSeatReservationForEvent(seat, event);
        if (isCleared) {
            System.out.println("Reservation cleared for seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getSeatNumber() + " in row " + seat.getRowNumber() + " was not reserved for event: " + event.getEventName());
        }
    }

    /**
     * Recommends a front-row seat from a list of available seats and prints the recommended seat.
     *
     * @param availableSeats the list of available seats from which a front-row seat is recommended
     */
    public void recommendFrontRowSeat(List<Seat> availableSeats) {
        Seat recommendedSeat = seatService.recommendFrontRowSeat(availableSeats);
        if (recommendedSeat != null) {
            System.out.println("Recommended front-row seat: Row " + recommendedSeat.getRowNumber() + ", Seat " + recommendedSeat.getSeatNumber());
        } else {
            System.out.println("No available seats to recommend.");
        }
    }
}
