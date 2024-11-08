package service;

import model.Event;
import model.Seat;
import model.Section;
import model.Venue;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;

public class SeatService {
    private final IRepository<Seat> seatRepository;

    public SeatService(IRepository<Seat> seatRepository) {
        this.seatRepository = seatRepository;
    }

    // Adds a new seat
    public void addSeat(Seat seat) {
        if (!seatExists(seat.getID())) {
            seatRepository.create(seat);
            System.out.println("Seat added successfully.");
        } else {
            System.out.println("Seat with this ID already exists.");
        }
    }

    // Checks if a seat exists by ID
    private boolean seatExists(int seatID) {
        List<Seat> seats = seatRepository.getAll();
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i).getID() == seatID) {
                return true;
            }
        }
        return false;
    }

    // Retrieves all seats
    public List<Seat> getAllSeats() {
        return seatRepository.getAll();
    }

    // Updates an existing seat by ID
    public boolean updateSeat(Seat updatedSeat) {
        if (seatExists(updatedSeat.getID())) {
            seatRepository.update(updatedSeat);
            System.out.println("Seat updated successfully.");
            return true;
        } else {
            System.out.println("Seat not found.");
            return false;
        }
    }

    // Deletes a seat by ID
    public boolean deleteSeat(int seatID) {
        if (seatExists(seatID)) {
            seatRepository.delete(seatID);
            System.out.println("Seat deleted successfully.");
            return true;
        } else {
            System.out.println("Seat not found.");
            return false;
        }
    }

    // Reservation-related methods

    // Checks if a seat is reserved for a specific event
    public boolean isSeatReservedForEvent(Seat seat, Event event) {
        return seat.getReservedForEvent() != null && seat.getReservedForEvent().equals(event);
    }

    // Reserves a seat for a specific event
    public void reserveSeatForEvent(Seat seat, Event event) {
        if (!isSeatReservedForEvent(seat, event)) {
            seat.setReservedForEvent(event);
            seatRepository.update(seat); // Update repository with reservation
            System.out.println("Seat " + seat.getID() + " reserved for event " + event.getID());
        } else {
            System.out.println("Seat " + seat.getID() + " is already reserved for event " + event.getID());
        }
    }

    // Clears the reservation for a specific event
    public void clearSeatReservationForEvent(Seat seat, Event event) {
        if (isSeatReservedForEvent(seat, event)) {
            seat.setReservedForEvent(null);
            seatRepository.update(seat); // Update repository to clear reservation
            System.out.println("Reservation cleared for seat " + seat.getID() + " for event " + event.getID());
        } else {
            System.out.println("Seat " + seat.getID() + " was not reserved for event " + event.getID());
        }
    }

    // Clears any reservation on the seat
    public void clearAllReservations(Seat seat) {
        seat.setReservedForEvent(null);
        seatRepository.update(seat); // Update repository to clear reservation
        System.out.println("All reservations cleared for seat " + seat.getID());
    }

    // Retrieves the list of available seats for a specific event in a given venue
    public List<Seat> getAvailableSeats(Venue venue, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        List<Section> sections = venue.getSections();

        // Iterate through each section in the venue
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            List<Seat> seats = section.getSeats();

            // Check each seat in the section
            for (int j = 0; j < seats.size(); j++) {
                Seat seat = seats.get(j);
                if (!isSeatReservedForEvent(seat, event)) {
                    availableSeats.add(seat);
                }
            }
        }

        return availableSeats;
    }
}
