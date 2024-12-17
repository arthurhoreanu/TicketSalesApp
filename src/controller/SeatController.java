package controller;

import model.Customer;
import model.Event;
import model.Row;
import model.Seat;
import service.SeatService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The SeatController class provides methods for managing seats, including creation, reservation,
 * checking availability, and seat recommendations.
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
     * Creates a new seat in the specified row.
     *
     * @param seatID         the ID of the seat
     * @param row            the row in which the seat is created
     * @param isReserved     whether the seat is initially reserved
     * @param reservedForEvent the event for which the seat may be reserved
     */
    public void createSeat(int seatID, Row row, boolean isReserved, Event reservedForEvent) {
        boolean isCreated = seatService.createSeat(seatID, row, isReserved, reservedForEvent);
        if (isCreated) {
            System.out.println("Seat created successfully with ID: " + seatID + " in row: " + row.getID());
        } else {
            System.out.println("Seat with ID: " + seatID + " already exists.");
        }
    }

    /**
     * Retrieves a seat by its ID and displays its details.
     *
     * @param seatID the ID of the seat to retrieve
     * @return the Seat object if found, or null otherwise
     */
    public Seat findSeatByID(int seatID) {
        Seat seat = seatService.findSeatByID(seatID);
        if (seat != null) {
            System.out.println("Seat found: ID " + seatID + ", Row " + seat.getRow().getID() + ", Reserved: " + seat.isReserved());
        } else {
            System.out.println("Seat with ID " + seatID + " not found.");
        }
        return seat;
    }

    /**
     * Retrieves and displays all seats in a specific row.
     *
     * @param row the row to retrieve seats from
     */
    public void getSeatsByRow_SEAT(Row row) {
        List<Seat> seats = seatService.getSeatsByRow(row);
        if (seats.isEmpty()) {
            System.out.println("No seats found in row with ID " + row.getID() + ".");
        } else {
            System.out.println("Seats in row with ID " + row.getID() + ":");
            seats.forEach(seat -> System.out.println("- Seat ID: " + seat.getID() + ", Reserved: " + seat.isReserved()));
        }
    }

    /**
     * Checks if a specific seat is reserved for a given event and displays the result.
     *
     * @param seat  the seat to check
     * @param event the event for which reservation status is checked
     */
    public void checkSeatReservation(Seat seat, Event event) {
        boolean isReserved = seatService.isSeatReservedForEvent(seat, event);
        if (isReserved) {
            System.out.println("Seat " + seat.getID() + " in row " + seat.getRow().getID() + " is reserved for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getID() + " in row " + seat.getRow().getID() + " is not reserved for event: " + event.getEventName());
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
            System.out.println("Seat " + seat.getID() + " in row " + seat.getRow().getID() + " has been successfully reserved for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getID() + " in row " + seat.getRow().getID() + " is already reserved for event: " + event.getEventName());
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
            System.out.println("Reservation cleared for seat " + seat.getID() + " in row " + seat.getRow().getID() + " for event: " + event.getEventName());
        } else {
            System.out.println("Seat " + seat.getID() + " in row " + seat.getRow().getID() + " was not reserved for event: " + event.getEventName());
        }
    }

    /**
     * Retrieves and displays all available seats in a row for a specific event.
     *
     * @param row   the row to check
     * @param event the event for which seat availability is checked
     */
    public void getAvailableSeatsInRow_SEAT(Row row, Event event) {
        List<Seat> availableSeats = seatService.getSeatsByRow(row).stream()
                .filter(seat -> !seatService.isSeatReservedForEvent(seat, event))
                .collect(Collectors.toList());

        if (availableSeats.isEmpty()) {
            System.out.println("No available seats in row with ID " + row.getID() + " for event '" + event.getEventName() + "'.");
        } else {
            System.out.println("Available seats in row with ID " + row.getID() + " for event '" + event.getEventName() + "':");
            availableSeats.forEach(seat -> System.out.println("- Seat ID: " + seat.getID() + ", Seat Number: " + seat.getID()));
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
            System.out.println("Recommended front-row seat: Row " + recommendedSeat.getRow().getID() + ", Seat " + recommendedSeat.getID());
        } else {
            System.out.println("No available seats to recommend.");
        }
    }

    /**
     * Deletes a seat by its ID and prints the result.
     *
     * @param seatID the ID of the seat to delete
     */
    public void deleteSeatByID(int seatID) {
        boolean isDeleted = seatService.deleteSeatByID(seatID);
        if (isDeleted) {
            System.out.println("Seat with ID " + seatID + " has been deleted.");
        } else {
            System.out.println("Seat with ID " + seatID + " not found. Deletion failed.");
        }
    }

    /**
     * Displays all seats in the system.
     */
    public void getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        if (seats.isEmpty()) {
            System.out.println("No seats available.");
        } else {
            System.out.println("All seats:");
            seats.forEach(seat -> System.out.println("- Seat ID: " + seat.getID() + ", Reserved: " + seat.isReserved()));
        }
    }
}