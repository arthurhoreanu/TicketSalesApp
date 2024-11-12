package service;

import model.Event;
import model.Seat;
import model.Section;
import repository.IRepository;

import java.util.List;

public class SeatService {
    private final IRepository<Seat> seatRepository;

    public SeatService(IRepository<Seat> seatRepository) {
        this.seatRepository = seatRepository;
    }

    // Adds a new seat
    public boolean createSeat(int seatID, Section section, int rowNumber, int seatNumber, Event reservedForEvent) {
        if (findSeatByID(seatID) == null) {
            Seat seat = new Seat(seatID, rowNumber, section, seatNumber, reservedForEvent);
            seatRepository.create(seat);
            return true;
        }
        return false; // Seat with this ID already exists
    }

    // Retrieves a seat by ID
    public Seat findSeatByID(int seatID) {
        return seatRepository.getAll().stream()
                .filter(seat -> seat.getID() == seatID)
                .findFirst()
                .orElse(null);
    }

    // Checks if a seat is reserved for a specific event
    public boolean isSeatReservedForEvent(Seat seat, Event event) {
        return seat.getReservedForEvent() != null && seat.getReservedForEvent().equals(event);
    }

    // Reserves a seat for a specific event
    public boolean reserveSeatForEvent(Seat seat, Event event) {
        if (!isSeatReservedForEvent(seat, event)) {
            seat.setReservedForEvent(event);
            seatRepository.update(seat);
            return true; // Seat reserved successfully
        }
        return false; // Seat is already reserved for the event
    }

    // Clears the reservation for a specific event
    public boolean clearSeatReservationForEvent(Seat seat, Event event) {
        if (isSeatReservedForEvent(seat, event)) {
            seat.setReservedForEvent(null);
            seatRepository.update(seat);
            return true; // Reservation cleared successfully
        }
        return false; // Seat was not reserved for this event
    }

    // Helper method to recommend a front-row seat
    public Seat recommendFrontRowSeat(List<Seat> availableSeats) {
        if (availableSeats.isEmpty()) {
            return null;
        }
        Seat recommendedSeat = availableSeats.get(0);
        for (Seat seat : availableSeats) {
            if (seat.getRowNumber() < recommendedSeat.getRowNumber()) {
                recommendedSeat = seat;
            }
        }
        return recommendedSeat;
    }
}
