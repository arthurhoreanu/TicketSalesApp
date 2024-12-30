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
     *
     * @param rowId      The ID of the Row.
     * @param seatNumber The number of the Seat.
     * @return The created Seat.
     */
    public Seat createSeat(int rowId, int seatNumber) {
        try {
            return seatService.createSeat(rowId, seatNumber);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create seat: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a Seat by its ID.
     *
     * @param seatId The ID of the Seat to delete.
     */
    public void deleteSeat(int seatId) {
        boolean deleted = seatService.deleteSeat(seatId);
        System.out.println(deleted ? "Seat deleted successfully." : "Failed to delete seat. Seat not found.");
    }

    /**
     * Deletes all Seats in a specific Row.
     *
     * @param rowId The ID of the Row.
     */
    public void deleteSeatsByRow(int rowId) {
        seatService.deleteSeatsByRow(rowId);
        System.out.println("All seats in Row ID " + rowId + " deleted successfully.");
    }

    /**
     * Retrieves a Seat by its ID.
     *
     * @param seatId The ID of the Seat.
     * @return The Seat object if found, null otherwise.
     */
    public Seat findSeatByID(int seatId) {
        Seat seat = seatService.findSeatByID(seatId);
        if (seat != null) {
            System.out.println("Seat found: " + seat);
        } else {
            System.out.println("Seat with ID " + seatId + " not found.");
        }
        return seat;
    }

    /**
     * Retrieves all Seats.
     *
     * @return A list of all Seats.
     */
    public List<Seat> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        if (!seats.isEmpty()) {
            seats.forEach(System.out::println);
        } else {
            System.out.println("No seats available.");
        }
        return seats;
    }

    /**
     * Reserves a Seat.
     *
     * @param seatId     The ID of the Seat to reserve.
     * @param event      The Event associated with the reservation.
     * @param customer   The Customer making the reservation.
     * @param price      The price of the reservation.
     * @param ticketType The type of Ticket.
     */
    public void reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        boolean reserved = seatService.reserveSeat(seatId, event, customer, price, ticketType);
        System.out.println(reserved ? "Seat reserved successfully." : "Failed to reserve seat.");
    }

    /**
     * Unreserves a Seat.
     *
     * @param seatId The ID of the Seat to unreserve.
     */
    public void unreserveSeat(int seatId) {
        seatService.unreserveSeat(seatId);
        System.out.println("Seat unreserved successfully.");
    }

    /**
     * Checks if a Seat is reserved for a specific Event.
     *
     * @param seatId  The ID of the Seat.
     * @param eventId The ID of the Event.
     * @return True if the Seat is reserved for the Event, false otherwise.
     */
    public boolean isSeatReservedForEvent(int seatId, int eventId) {
        boolean isReserved = seatService.isSeatReservedForEvent(seatId, eventId);
        System.out.println(isReserved ? "The seat is reserved for the event." : "The seat is not reserved for the event.");
        return isReserved;
    }
}
