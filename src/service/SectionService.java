package service;

import model.Event;
import model.Seat;
import model.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionService {
    private final SeatService seatService;

    public SectionService(SeatService seatService) {
        this.seatService = seatService;
    }

    // Retrieves the list of available seats in a section for a specific event
    public List<Seat> getAvailableSeats(Section section, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        List<Seat> seats = section.getSeats();

        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (!seatService.isSeatReservedForEvent(seat, event)) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    // Counts the number of available seats in a section for a specific event
    public int countAvailableSeats(Section section, Event event) {
        return getAvailableSeats(section, event).size();
    }

    // Adds a new seat to the section
    public void addSeat(Section section, Seat seat) {
        section.getSeats().add(seat);
    }

    // Removes a seat from the section
    public boolean removeSeat(Section section, Seat seat) {
        return section.getSeats().remove(seat);
    }

    // Checks if a section is at full capacity
    public boolean isSectionFull(Section section, Event event) {
        int availableSeats = countAvailableSeats(section, event);
        return availableSeats == 0;
    }

    // Reserves a list of seats in the section for a specific event
    public void reserveSeats(Section section, List<Seat> seatsToReserve, Event event) {
        List<Seat> seats = section.getSeats();
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (seatsToReserve.contains(seat)) {
                seatService.reserveSeatForEvent(seat, event);
            }
        }
    }

    // Releases all reserved seats in the section for a specific event
    public void releaseAllSeats(Section section, Event event) {
        List<Seat> seats = section.getSeats();
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (seatService.isSeatReservedForEvent(seat, event)) {
                seatService.clearSeatReservationForEvent(seat, event);
            }
        }
    }

    // Retrieves a list of seats that are reserved for a specific event
    public List<Seat> getReservedSeats(Section section, Event event) {
        List<Seat> reservedSeats = new ArrayList<>();
        List<Seat> seats = section.getSeats();
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (seatService.isSeatReservedForEvent(seat, event)) {
                reservedSeats.add(seat);
            }
        }
        return reservedSeats;
    }
}
