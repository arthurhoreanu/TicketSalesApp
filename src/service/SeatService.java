package service;

import model.Event;
import model.Seat;
import model.Section;
import model.Venue;
import repository.IRepository;
import model.Customer;

import java.util.ArrayList;
import java.util.List;

public class SeatService {
    private final IRepository<Seat> seatRepository;

    public SeatService(IRepository<Seat> seatRepository) {
        this.seatRepository = seatRepository;
    }

    // Adds a new seat
    public boolean createSeat(int seatID, Section section, int rowNumber, int sitNumber, Event reservedForEvent) {
        if (findSeatById(seatID) == null) {
            Seat seat = new Seat(seatID, rowNumber, section, sitNumber, reservedForEvent);
            seatRepository.create(seat);
            return true;
        }
        return false; // Seat with this ID already exists
    }

    // Retrieves a seat by ID
    public Seat findSeatById(int seatID) {
        return seatRepository.getAll().stream()
                .filter(seat -> seat.getID() == seatID)
                .findFirst()
                .orElse(null);
    }

    // Updates an existing seat by ID
    public boolean updateSeat(int seatID, Section newSection, int newRowNumber, int newSitNumber, Event newReservedForEvent) {
        Seat seat = findSeatById(seatID);
        if (seat != null) {
            seat.setSection(newSection);
            seat.setRowNumber(newRowNumber);
            seat.setSitNumber(newSitNumber);
            seat.setReservedForEvent(newReservedForEvent);
            seatRepository.update(seat);
            return true;
        }
        return false; // Seat not found
    }

    // Deletes a seat by ID
    public boolean deleteSeat(int seatID) {
        if (findSeatById(seatID) != null) {
            seatRepository.delete(seatID);
            return true;
        }
        return false; // Seat not found
    }

    // Retrieves all seats
    public List<Seat> getAllSeats() {
        return seatRepository.getAll();
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

    // Clears any reservation on the seat
    public void clearAllReservations(Seat seat) {
        seat.setReservedForEvent(null);
        seatRepository.update(seat); // Clear all reservations
    }

    // Retrieves the list of available seats for a specific event in a given venue
    public List<Seat> getAvailableSeats(Venue venue, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        for (Section section : venue.getSections()) {
            availableSeats.addAll(getAvailableSeats(section, event));
        }
        return availableSeats;
    }

    // Retrieves the list of available seats for a specific section and event
    public List<Seat> getAvailableSeats(Section section, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : section.getSeats()) {
            if (!isSeatReservedForEvent(seat, event)) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
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

    // Reserves a seat for a specific event and records the customer's preference
    public boolean reserveSeatForEvent(Customer customer, Seat seat, Event event) {
        if (reserveSeatForEvent(seat, event)) {
            customer.addSeatPreference(seat.getSection());
            return true;
        }
        return false; // Seat is already reserved for the event
    }
}
