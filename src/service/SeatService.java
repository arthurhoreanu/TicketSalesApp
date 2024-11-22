package service;

import model.Event;
import model.Order;
import model.Seat;
import model.Section;
import repository.FileRepository;
import repository.IRepository;

import java.util.List;

//todo nothing related to seats doesn t work, especialy recommSeat, doesn t shit anything in the Seat.csv check the to/from csv s methods
//todo from the seat, ticket doesn t work either so ofc DOMINO FUCKING EFFECT

/**
 * Service class for managing seat-related operations.
 */
public class SeatService {
    private final IRepository<Seat> seatRepository;
    private final FileRepository<Seat> seatFileRepository;

    /**
     * Constructs a SeatService with the specified seat repository.
     *
     * @param seatRepository the repository for storing and managing seat data.
     */
    public SeatService(IRepository<Seat> seatRepository) {
        this.seatRepository = seatRepository;
        this.seatFileRepository = new FileRepository<>("src/repository/data/seats.csv", Seat::fromCsvFormat);
        List<Seat> seatsfromFile = seatFileRepository.getAll();
        for (Seat seat : seatsfromFile) {
            seatRepository.create(seat);
        }
    }

    /**
     * Creates and adds a new seat to the repository if a seat with the given ID does not already exist.
     *
     * @param seatID          the unique ID of the seat.
     * @param section         the section to which the seat belongs.
     * @param rowNumber       the row number where the seat is located.
     * @param seatNumber      the number assigned to the seat within its row.
     * @param reservedForEvent the event for which the seat is reserved, or null if it's not reserved.
     * @return true if the seat was successfully created; false if a seat with the specified ID already exists.
     */
    public boolean createSeat(int seatID, Section section, int rowNumber, int seatNumber, Event reservedForEvent) {
        if (findSeatByID(seatID) == null) {
            Seat seat = new Seat(seatID, rowNumber, section, seatNumber, reservedForEvent);
            seatRepository.create(seat);
            return true;
        }
        return false; // Seat with this ID already exists
    }

    /**
     * Retrieves a seat by its unique ID.
     *
     * @param seatID the unique ID of the seat.
     * @return the Seat object if found; otherwise, null.
     */
    public Seat findSeatByID(int seatID) {
        return seatRepository.getAll().stream()
                .filter(seat -> seat.getID() == seatID)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a seat is reserved for a specific event.
     *
     * @param seat  the seat to check.
     * @param event the event for which to check the reservation.
     * @return true if the seat is reserved for the specified event; false otherwise.
     */
    public boolean isSeatReservedForEvent(Seat seat, Event event) {
        return seat.getReservedForEvent() != null && seat.getReservedForEvent().equals(event);
    }

    /**
     * Reserves a seat for a specific event if it is not already reserved.
     *
     * @param seat  the seat to reserve.
     * @param event the event for which the seat is to be reserved.
     * @return true if the seat was successfully reserved; false if it was already reserved for the event.
     */
    public boolean reserveSeatForEvent(Seat seat, Event event) {
        if (!isSeatReservedForEvent(seat, event)) {
            seat.setReservedForEvent(event);
            seatRepository.update(seat);
            return true; // Seat reserved successfully
        }
        return false; // Seat is already reserved for the event
    }

    /**
     * Clears the reservation of a seat for a specific event if it is currently reserved for that event.
     *
     * @param seat  the seat to clear the reservation.
     * @param event the event for which the reservation is to be cleared.
     * @return true if the reservation was successfully cleared; false if the seat was not reserved for this event.
     */
    public boolean clearSeatReservationForEvent(Seat seat, Event event) {
        if (isSeatReservedForEvent(seat, event)) {
            seat.setReservedForEvent(null);
            seatRepository.update(seat);
            return true; // Reservation cleared successfully
        }
        return false; // Seat was not reserved for this event
    }

    /**
     * Recommends a front-row seat from a list of available seats.
     * If multiple seats are in the front row, the first one in the list is returned.
     *
     * @param availableSeats the list of available seats to consider.
     * @return the seat in the front-most row; null if the list of available seats is empty.
     */
    public Seat recommendFrontRowSeat(List<Seat> availableSeats) {
        return availableSeats.stream()
                .min((s1, s2) -> Integer.compare(s1.getRowNumber(), s2.getRowNumber()))
                .orElse(null);
    }

}
