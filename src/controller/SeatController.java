package controller;

import model.*;
import service.SeatService;

import java.util.List;

/**
 * Controller for managing seat-related operations.
 */
// TODO de rescris pentru noul Service
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

    public Seat createSeat(int rowId, int seatNumber) {
        try {
            Seat seat = seatService.createSeat(rowId, seatNumber);
            System.out.println("Seat created successfully: " + seat);
            return seat;
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create seat: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteSeatById(int seatId) {
        boolean deleted = seatService.deleteSeat(seatId);
        System.out.println(deleted ? "Seat deleted successfully." : "Failed to delete seat. Seat not found.");
        return deleted;
    }

    public Seat findSeatByID(int seatId) {
        Seat seat = seatService.findSeatByID(seatId);
        if (seat != null) {
            System.out.println("Seat found: " + seat);
        } else {
            System.out.println("Seat with ID " + seatId + " not found.");
        }
        return seat;
    }

    public List<Seat> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        if (seats.isEmpty()) {
            System.out.println("No seats available.");
        } else {
            seats.forEach(System.out::println);
        }
        return seats;
    }

    public List<Seat> getSeatsByRow(int rowId) {
        List<Seat> seats = seatService.getSeatsByRow(rowId);
        if (seats.isEmpty()) {
            System.out.println("No seats available in row " + rowId + ".");
        } else {
            seats.forEach(System.out::println);
        }
        return seats;
    }

    public List<Seat> getAvailableSeatsInSection(int sectionId) {
        List<Seat> availableSeats = seatService.getAvailableSeatsInSection(sectionId);
        if (availableSeats.isEmpty()) {
            System.out.println("No available seats in section " + sectionId + ".");
        } else {
            availableSeats.forEach(System.out::println);
        }
        return availableSeats;
    }

    public List<Seat> getAvailableSeatsInVenue(int venueId) {
        List<Seat> availableSeats = seatService.getAvailableSeatsInVenue(venueId);
        if (availableSeats.isEmpty()) {
            System.out.println("No available seats in venue " + venueId + ".");
        } else {
            availableSeats.forEach(System.out::println);
        }
        return availableSeats;
    }

    public boolean reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        boolean reserved = seatService.reserveSeat(seatId, event, customer, price, ticketType);
        System.out.println(reserved ? "Seat reserved successfully." : "Failed to reserve seat.");
        return reserved;
    }

    public void unreserveSeat(int seatId) {
        seatService.unreserveSeat(seatId);
        System.out.println("Seat unreserved successfully.");
    }

    public Seat recommendClosestSeat(int rowId, int seatNumber) {
        Seat recommendedSeat = seatService.recommendClosestSeat(rowId, seatNumber);
        if (recommendedSeat != null) {
            System.out.println("Recommended seat: " + recommendedSeat);
        } else {
            System.out.println("No suitable seat found.");
        }
        return recommendedSeat;
    }

    public boolean isSeatReservedForEvent(int seatId, int eventId) {
        boolean isReserved = seatService.isSeatReservedForEvent(seatId, eventId);
        System.out.println(isReserved ? "The seat is reserved for the event." : "The seat is not reserved for the event.");
        return isReserved;
    }
}
