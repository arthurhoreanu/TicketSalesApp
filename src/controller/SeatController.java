package controller;

import model.*;
import service.SeatService;

import java.util.List;

/**
 * Controller for managing seat-related operations.
 */
public class SeatController {
    private final SeatService seatService;

    /**
     * Constructs a SeatController with the specified SeatService.
     *
     * @param seatService The service for managing seats.
     */
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    /**
     * Creates a new Seat in a specific Row.
     */
    public void createSeat(int rowId, int seatNumber) {
        Seat seat = seatService.createSeat(rowId, seatNumber);

        if (seat != null) {
            System.out.println("Seat created successfully: " + seat);
        } else {
            System.out.println("Failed to create seat. Row with ID " + rowId + " not found.");
        }
    }

    /**
     * Deletes a Seat by its ID.
     */
    public void deleteSeatById(int seatId) {
        boolean deleted = seatService.deleteSeatById(seatId);

        if (deleted) {
            System.out.println("Seat with ID " + seatId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete seat. Seat with ID " + seatId + " not found.");
        }
    }

    /**
     * Retrieves a Seat by its ID.
     */
    public Seat findSeatByID(int seatId) {
        Seat seat = seatService.findSeatByID(seatId);
        if (seat != null) {
            System.out.println("Seat found: " + seat);
        } else {
            System.out.println("Seat with ID " + seatId + " not found.");
        }
    }

    /**
     * Retrieves all Seats.
     */
    public void getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();

        if (!seats.isEmpty()) {
            System.out.println("All seats:");
            seats.forEach(System.out::println);
        } else {
            System.out.println("No seats available.");
        }
    }

    /**
     * Retrieves all Seats in a specific Row.
     */
    public void getSeatsByRow(int rowId) {
        List<Seat> seats = seatService.getSeatsByRow(rowId);

        if (!seats.isEmpty()) {
            System.out.println("Seats in Row ID " + rowId + ":");
            seats.forEach(System.out::println);
        } else {
            System.out.println("No seats found for Row ID " + rowId + ".");
        }
    }

    /**
     * Retrieves all available Seats in a specific Section.
     */
    public void getAvailableSeatsInSection(int sectionId) {
        List<Seat> availableSeats = seatService.getAvailableSeatsInSection(sectionId);

        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats in Section ID " + sectionId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats in Section ID " + sectionId + ".");
        }
    }

    /**
     * Retrieves all available Seats in a specific Venue.
     */
    public void getAvailableSeatsInVenue(int venueId) {
        List<Seat> availableSeats = seatService.getAvailableSeatsInVenue(venueId);

        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats in Venue ID " + venueId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats in Venue ID " + venueId + ".");
        }
    }

    /**
     * Reserves a Seat for a specific Event.
     */
    public void reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        boolean reserved = seatService.reserveSeat(seatId, event, customer, price, ticketType);

        if (reserved) {
            System.out.println("Seat with ID " + seatId + " reserved successfully for Event ID " + event.getID() + ".");
        } else {
            System.out.println("Failed to reserve seat. Either seat is already reserved, or invalid data provided.");
        }
    }

    /**
     * Checks if a Seat is reserved for a specific Event.
     */
    public void isSeatReservedForEvent(int seatId, int eventId) {
        boolean isReserved = seatService.isSeatReservedForEvent(seatId, eventId);

        if (isReserved) {
            System.out.println("Seat with ID " + seatId + " is reserved for Event ID " + eventId + ".");
        } else {
            System.out.println("Seat with ID " + seatId + " is not reserved for Event ID " + eventId + ".");
        }
    }

    /**
     * Unreserves a Seat.
     */
    public void unreserveSeat(int seatId) {
        seatService.unreserveSeat(seatId);
        System.out.println("Seat with ID " + seatId + " has been unreserved.");
    }

    /**
     * Recommends the closest available Seat to a specific seat number in a Row.
     */
    public void recommendClosestSeat(int rowId, int seatNumber) {
        Seat recommendedSeat = seatService.recommendClosestSeat(rowId, seatNumber);

        if (recommendedSeat != null) {
            System.out.println("Recommended closest seat: " + recommendedSeat);
        } else {
            System.out.println("No available seats found near seat number " + seatNumber + " in Row ID " + rowId + ".");
        }
    }
}