package service;

import model.Customer;
import model.Event;
import model.Row;
import model.Seat;
import repository.FileRepository;
import repository.IRepository;
import repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing seat-related operations, supporting in-memory, file, and database storage.
 */
public class SeatService {

    private final IRepository<Seat> seatRepository;
    private final FileRepository<Seat> seatFileRepository;
    private final DBRepository<Seat> seatDatabaseRepository;

    /**
     * Constructs a SeatService with the specified repository for managing seat data.
     *
     * @param seatRepository the primary repository for seat data.
     */
    public SeatService(IRepository<Seat> seatRepository) {
        this.seatRepository = seatRepository;

        // Initialize file and database repositories
        this.seatFileRepository = new FileRepository<>("src/repository/data/seats.csv", Seat::fromCsv);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.seatDatabaseRepository = new DBRepository<>(entityManagerFactory, Seat.class);

        // Sync data from file and database repositories to the primary repository
        syncFromCsv();
        syncFromDatabase();
    }

    /**
     * Synchronizes seats from the CSV file into the main repository.
     */
    private void syncFromCsv() {
        List<Seat> seats = seatFileRepository.getAll();
        for (Seat seat : seats) {
            seatRepository.create(seat);
        }
    }

    /**
     * Synchronizes seats from the database into the main repository.
     */
    private void syncFromDatabase() {
        List<Seat> seats = seatDatabaseRepository.getAll();
        for (Seat seat : seats) {
            seatRepository.create(seat);
        }
    }

    /**
     * Creates and adds a new seat to all repositories if a seat with the given ID does not already exist.
     *
     * @param seatID           the unique ID of the seat.
     * @param row              the row to which the seat belongs.
     * @param isReserved       the reserved status of the seat.
     * @param reservedForEvent the event for which the seat is reserved, or null if not reserved.
     * @return true if the seat was successfully created; false if a seat with the specified ID already exists.
     */
    public boolean createSeat(int seatID, Row row, boolean isReserved, Event reservedForEvent) {
        if (findSeatByID(seatID) == null) {
            Seat seat = new Seat(seatID, row, isReserved, reservedForEvent);

            // Save the seat to all repositories
            seatRepository.create(seat);
            seatFileRepository.create(seat);
            seatDatabaseRepository.create(seat);

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
        return seatRepository.read(seatID);
    }

    /**
     * Retrieves all seats in a specific row.
     *
     * @param row the row to filter seats by.
     * @return a list of seats belonging to the specified row.
     */
    public List<Seat> getSeatsByRow(Row row) {
        return seatRepository.getAll().stream()
                .filter(seat -> seat.getRow().getID().equals(row.getID()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all seats in the repository.
     *
     * @return a list of all Seat objects.
     */
    public List<Seat> getAllSeats() {
        return seatRepository.getAll();
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
            seat.setReserved(true);

            // Update all repositories
            seatRepository.update(seat);
            seatFileRepository.update(seat);
            seatDatabaseRepository.update(seat);

            return true;
        }
        return false; // Seat is already reserved for the event
    }

    /**
     * Clears the reservation of a seat for a specific event if it is currently reserved for that event.
     *
     * @param seat  the seat to clear the reservation for.
     * @param event the event for which the reservation is to be cleared.
     * @return true if the reservation was successfully cleared; false if the seat was not reserved for this event.
     */
    public boolean clearSeatReservationForEvent(Seat seat, Event event) {
        if (isSeatReservedForEvent(seat, event)) {
            seat.setReservedForEvent(null);
            seat.setReserved(false);

            // Update all repositories
            seatRepository.update(seat);
            seatFileRepository.update(seat);
            seatDatabaseRepository.update(seat);

            return true;
        }
        return false; // Seat was not reserved for this event
    }


    /**
     * Recommends a seat from a list of available seats based on customer preferences.
     *
     * @param customer       the customer for whom the seat is recommended.
     * @param availableSeats the list of available seats to consider.
     * @return the recommended Seat object, or null if no suitable seat is found.
     *//*
    public Seat recommendSeatFromList(Customer customer, List<Seat> availableSeats) {
        // Sort seats by row preference (if customer preferences exist)
        availableSeats.sort((s1, s2) -> {
            int preference1 = customer.getPreferredRows().getOrDefault(s1.getRow().getID(), 0);
            int preference2 = customer.getPreferredRows().getOrDefault(s2.getRow().getID(), 0);
            return Integer.compare(preference2, preference1); // Higher preference first
        });

        return availableSeats.isEmpty() ? null : availableSeats.get(0);
    }*/

    /**
     * Recommends a front-row seat from a list of available seats.
     * If multiple seats are in the front row, the first one in the list is returned.
     *
     * @param availableSeats the list of available seats to consider.
     * @return the seat in the front-most row; null if the list of available seats is empty.
     */
    public Seat recommendFrontRowSeat(List<Seat> availableSeats) {
        return availableSeats.stream()
                .min((s1, s2) -> Integer.compare(s1.getRow().getRowCapacity(), s2.getRow().getRowCapacity()))
                .orElse(null);
    }

    /**
     * Deletes a seat by its ID.
     *
     * @param seatID the ID of the seat to delete.
     * @return true if the seat was deleted; false otherwise.
     */
    public boolean deleteSeatByID(int seatID) {
        Seat seat = findSeatByID(seatID);
        if (seat != null) {
            // Delete from all repositories
            seatRepository.delete(seatID);
            seatFileRepository.delete(seatID);
            seatDatabaseRepository.delete(seatID);

            return true;
        }
        return false;
    }


}
