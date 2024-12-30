package service;

import controller.Controller;
import model.*;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.util.List;

/**
 * Service class for managing seat-related operations, supporting in-memory, file, and database storage.
 */
public class SeatService {

    private final IRepository<Seat> seatRepository;
    static Controller controller = ControllerProvider.getController();

    public SeatService(RepositoryFactory repositoryFactory) {
        this.seatRepository = repositoryFactory.createSeatRepository();
    }

    /**
     * Creates a new Seat and saves it to all repositories.
     *
     * @param rowId       the ID of the Row to which the Seat belongs.
     * @param seatNumber  the number of the Seat.
     * @return the created Seat object.
     */
    public Seat createSeat(int rowId, int seatNumber) {
        Seat seat = new Seat(0, seatNumber, false, controller.findRowByID(rowId));
        seatRepository.create(seat);
        return seat;
    }

    public void deleteSeatsByRow(int rowId) {
        List<Seat> seats = seatRepository.getAll().stream()
                .filter(seat -> seat.getRow().getID() == rowId)
                .toList();
        for (Seat seat : seats) {
            seatRepository.delete(seat.getID());
        }
    }

    /**
     * Deletes a Seat by its ID.
     *
     * @param seatID the ID of the Seat to delete.
     * @return true if the Seat was successfully deleted, false otherwise.
     */
    public boolean deleteSeat(int seatID) {
        Seat seat = findSeatByID(seatID);
        if (seat == null) {
            return false; // Seat not found
        }
        Row row = seat.getRow();
        if (row != null) {
            row.removeSeat(seat); // Update the Row to remove the Seat
        }
        seatRepository.delete(seatID);
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
     * Reserves a Seat by associating it with a Ticket.
     *
     * @param seatId     the ID of the Seat to reserve.
     * @return true if the Seat was successfully reserved, false otherwise.
     */
    public boolean reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        Seat seat = findSeatByID(seatId);
        if (seat == null || seat.isReserved() || event == null || customer == null) {
            return false; // Seat not found, already reserved, or invalid input
        }
        // Create a Ticket object and associate it with the Seat
        Ticket ticket = new Ticket(0, event, seat, customer, price, ticketType);
        seat.setReserved(true);
        seat.setTicket(ticket);
        // Update the seat in the repositories
        seatRepository.update(seat);
        return true;
    }

    /**
     * Unreserves a Seat, removing its association with any Ticket.
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

    public boolean isSeatForEvent(Seat seat, int eventId) {
        return seat.getTicket() != null && seat.getTicket().getEvent().getID() == eventId;
    }
}