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
        List<Seat> seats = seatRepository.getAll();
        for (Seat seat : seats) {
            if (seat.getID() == seatID) {
                return seat;
            }
        }
        return null;
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
        Seat seat = findSeatById(seatID);
        if (seat != null) {
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
        List<Section> sections = venue.getSections();

        for (Section section : sections) {
            List<Seat> seats = section.getSeats();
            for (Seat seat : seats) {
                if (!isSeatReservedForEvent(seat, event)) {
                    availableSeats.add(seat);
                }
            }
        }
        return availableSeats;
    }
}
