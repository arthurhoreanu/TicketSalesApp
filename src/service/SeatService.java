package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;
import repository.DBRepository;

import java.util.ArrayList;

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
    private final RowService rowService;

    /**
     * Constructs a SeatService with the specified repository for managing seat data.
     *
     * @param seatRepository the primary repository for seat data.
     */
    public SeatService(IRepository<Seat> seatRepository, RowService rowService) {
        this.seatRepository = seatRepository;
        this.rowService = rowService;


        // Initialize file and database repositories
        this.seatFileRepository = new FileRepository<>("src/repository/data/seats.csv", Seat::fromCsv);

        // Sync data from file and database repositories to the primary repository
        syncFromCsv();
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
     * Creates a new Seat and saves it to all repositories.
     *
     * @param rowId    the ID of the Row to which the Seat belongs.
     * @param seatNumber the number of the Seat.
     * @return the created Seat object.
     */
    public Seat createSeat(int rowId, int seatNumber) {
        Row row = rowService.findRowByID(rowId);
        if (row == null) {
            return null; // Row not found
        }

        Seat seat = new Seat(0, seatNumber, false, row);
        seatRepository.create(seat);
        seatFileRepository.create(seat);

        row.addSeat(seat); // Update the Row with the new Seat
        return seat;
    }

    /**
     * Deletes a Seat by its ID.
     *
     * @param seatId the ID of the Seat to delete.
     * @return true if the Seat was successfully deleted, false otherwise.
     */
    public boolean deleteSeatById(int seatId) {
        Seat seat = findSeatByID(seatId);
        if (seat == null) {
            return false; // Seat not found
        }

        Row row = seat.getRow();
        if (row != null) {
            row.removeSeat(seat); // Update the Row to remove the Seat
        }

        seatRepository.delete(seatId);
        seatFileRepository.delete(seatId);
        return true;
    }



    /**
     * Retrieves a Seat by its ID.
     *
     * @param seatId the ID of the Seat to retrieve.
     * @return the Seat object, or null if not found.
     */
    public Seat findSeatByID(int seatId) {
        return seatRepository.read(seatId);
    }

    /**
     * Retrieves all Seats from the repository.
     *
     * @return a list of all Seats.
     */
    public List<Seat> getAllSeats() {
        return seatRepository.getAll();
    }

    /**
     * Retrieves all Seats in a specific Row.
     *
     * @param rowId the ID of the Row.
     * @return a list of Seats in the Row.
     */
    public List<Seat> getSeatsByRow(int rowId) {
        Row row = rowService.findRowByID(rowId);
        return (row != null) ? row.getSeats() : new ArrayList<>();
    }

    /**
     * Retrieves all available Seats in a Section.
     *
     * @param sectionId the ID of the Section.
     * @return a list of available Seats in the Section.
     */
    public List<Seat> getAvailableSeatsInSection(int sectionId) {
        List<Row> rows = rowService.findRowsBySection(sectionId);
        List<Seat> availableSeats = new ArrayList<>();
        for (Row row : rows) {
            availableSeats.addAll(
                    row.getSeats().stream()
                            .filter(seat -> !seat.isReserved())
                            .collect(Collectors.toList())
            );
        }
        return availableSeats;
    }

    /**
     * Retrieves all available Seats in a Venue.
     *
     * @param venueId the ID of the Venue.
     * @return a list of available Seats in the Venue.
     */
    public List<Seat> getAvailableSeatsInVenue(int venueId) {
        List<Row> rows = rowService.getAllRows();
        return rows.stream()
                .filter(row -> row.getSection() != null && row.getSection().getVenue().getID() == venueId)
                .flatMap(row -> row.getSeats().stream())
                .filter(seat -> !seat.isReserved())
                .collect(Collectors.toList());
    }

    /**
     * Checks if a Seat is reserved for a specific Event.
     *
     * @param seat  the Seat to check.
     * @param event the Event for which to check the reservation.
     * @return true if the Seat is reserved for the specified Event; false otherwise.
     */
    public boolean isSeatReservedForEvent(Seat seat, Event event) {
        if (seat == null || !seat.isReserved() || seat.getTicket() == null) {
            return false; // Seat is not reserved or has no associated ticket
        }

        return seat.getTicket().getEvent().equals(event); // Check if the Ticket is for the given Event
    }


    /**
     * Recommends the closest Seat to a given seat number in a Row.
     *
     * @param rowId       the ID of the Row.
     * @param seatNumber  the desired seat number.
     * @return the recommended Seat, or null if no suitable Seat is found.
     */
    public Seat recommendClosestSeat(int rowId, int seatNumber) {
        Row row = rowService.findRowByID(rowId);
        if (row == null) {
            return null; // Row not found
        }

        Seat closestSeat = null;
        int closestDistance = Integer.MAX_VALUE;

        for (Seat seat : row.getSeats()) {
            if (!seat.isReserved()) {
                int distance = Math.abs(seat.getNumber() - seatNumber);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestSeat = seat;
                }
            }
        }

        return closestSeat;
    }


    /**
     * Reserves a Seat for a specific Event.
     *
     * @param seatId  the ID of the Seat to reserve.
     * @param event   the Event for which the Seat is reserved.
     * @return true if the Seat was successfully reserved, false otherwise.
     */
    public boolean reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        Seat seat = findSeatByID(seatId);
        if (seat == null || seat.isReserved() || event == null || customer == null) {
            return false; // Seat not found, already reserved, or invalid input
        }

        // Create a Ticket for the reservation
        Ticket ticket = new Ticket(0, event, seat, customer, price, ticketType);
        seat.setReserved(true);
        seat.setTicket(ticket);

        // Update the seat in the repositories
        seatRepository.update(seat);
        seatFileRepository.update(seat);

        return true;
    }

    /**
     * Checks if a Seat is reserved for a specific Event.
     *
     * @param seatId  the ID of the Seat.
     * @param eventId the ID of the Event.
     * @return true if the Seat is reserved for the Event, false otherwise.
     */
    public boolean isSeatReservedForEvent(int seatId, int eventId) {
        Seat seat = findSeatByID(seatId);
        return seat != null && seat.isReserved()
                && seat.getTicket() != null
                && seat.getTicket().getEvent().getID() == eventId;
    }

    /**
     * Unreserves a Seat, removing its association with any Event.
     *
     * @param seatId the ID of the Seat to unreserve.
     */
    public void unreserveSeat(int seatId) {
        Seat seat = findSeatByID(seatId);
        if (seat == null || !seat.isReserved()) {
            return; // Seat not found or not reserved
        }

        seat.setReserved(false);
        seat.setTicket(null); // Remove the Ticket association

        seatRepository.update(seat);
        seatFileRepository.update(seat);
    }
}