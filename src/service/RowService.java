package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;
import repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing row-related operations, including creating rows with seats,
 * retrieving seats, and handling row-related updates and deletions.
 */
public class RowService {

    private final IRepository<Row> rowRepository;
    private final IRepository<Seat> seatRepository;
    private final FileRepository<Row> rowFileRepository;
    private final FileRepository<Seat> seatFileRepository;
    private final DBRepository<Row> rowDatabaseRepository;
    private final DBRepository<Seat> seatDatabaseRepository;
    private final SeatService seatService;

    /**
     * Constructs a RowService with dependencies for managing rows and seats.
     *
     * @param seatService    the service for managing seat-related operations
     * @param rowRepository  the repository for managing row data
     * @param seatRepository the repository for managing seat data
     */
    public RowService(SeatService seatService, IRepository<Row> rowRepository, IRepository<Seat> seatRepository) {
        this.seatService = seatService;
        this.rowRepository = rowRepository;
        this.seatRepository = seatRepository;

        // File repositories
        this.rowFileRepository = new FileRepository<>("src/repository/data/rows.csv", Row::fromCsv);
        this.seatFileRepository = new FileRepository<>("src/repository/data/seats.csv", Seat::fromCsv);

        // Database repositories
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.rowDatabaseRepository = new DBRepository<>(entityManagerFactory, Row.class);
        this.seatDatabaseRepository = new DBRepository<>(entityManagerFactory, Seat.class);

        // Sync data from file and database repositories to the primary repository
        syncFromCsv();
        syncFromDatabase();
    }

    /**
     * Synchronizes rows from the CSV file into the main repository.
     */
    private void syncFromCsv() {
        List<Row> rows = rowFileRepository.getAll();
        for (Row row : rows) {
            rowRepository.create(row);
        }

        List<Seat> seats = seatFileRepository.getAll();
        for (Seat seat : seats) {
            seatRepository.create(seat);
        }
    }

    /**
     * Synchronizes rows from the database into the main repository.
     */
    private void syncFromDatabase() {
        List<Row> rows = rowDatabaseRepository.getAll();
        for (Row row : rows) {
            rowRepository.create(row);
        }

        List<Seat> seats = seatDatabaseRepository.getAll();
        for (Seat seat : seats) {
            seatRepository.create(seat);
        }
    }

    /**
     * Creates a new row and initializes seats for the row.
     *
     * @param rowCapacity the capacity of the row (number of seats)
     * @param section     the section to which the row belongs
     * @return the created Row object
     */
    public Row createRowWithSeats(int rowCapacity, Section section) {
        int newRowID = rowRepository.getAll().size() + 1;

        // Create the row
        Row row = new Row(newRowID, rowCapacity, section);

        // Initialize and create seats for the row
        for (int seatNumber = 1; seatNumber <= rowCapacity; seatNumber++) {
            Seat seat = new Seat(seatRepository.getAll().size() + 1, row, false, null);

            // Save seat to repositories
            seatRepository.create(seat);
            seatFileRepository.create(seat);
            seatDatabaseRepository.create(seat);
        }

        // Save the row to repositories
        rowRepository.create(row);
        rowFileRepository.create(row);
        rowDatabaseRepository.create(row);

        return row;
    }

    /**
     * Updates an existing row.
     *
     * @param rowID       the ID of the row to update
     * @param newCapacity the new capacity for the row
     * @return true if the row was updated, false otherwise
     */
    public boolean updateRow(int rowID, int newCapacity) {
        Row row = findRowByID(rowID);
        if (row != null) {
            row.setRowCapacity(newCapacity);

            // Update repositories
            rowRepository.update(row);
            rowFileRepository.update(row);
            rowDatabaseRepository.update(row);
            return true;
        }
        return false;
    }

    /**
     * Deletes a row by its ID and all associated seats.
     *
     * @param rowID the ID of the row to delete
     * @return true if the row was deleted, false otherwise
     */
    public boolean deleteRow(int rowID) {
        Row row = findRowByID(rowID);
        if (row != null) {
            // Delete associated seats
            List<Seat> seats = seatService.getSeatsByRow(row);
            for (Seat seat : seats) {
                seatRepository.delete(seat.getID());
                seatFileRepository.delete(seat.getID());
                seatDatabaseRepository.delete(seat.getID());
            }

            // Delete the row
            rowRepository.delete(rowID);
            rowFileRepository.delete(rowID);
            rowDatabaseRepository.delete(rowID);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all rows.
     *
     * @return a list of all rows
     */
    public List<Row> getAllRows() {
        return rowRepository.getAll();
    }

    /**
     * Finds a row by its ID.
     *
     * @param rowID the ID of the row to find
     * @return the Row object if found, null otherwise
     */
    public Row findRowByID(int rowID) {
        return rowRepository.read(rowID);
    }

    /**
     * Finds rows by section.
     *
     * @param section the section to filter rows by
     * @return a list of rows belonging to the specified section
     */
    public List<Row> findRowsBySection(Section section) {
        return rowRepository.getAll().stream()
                .filter(row -> row.getSection().getID().equals(section.getID()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all seats in a row.
     *
     * @param row the row to retrieve seats from
     * @return a list of Seat objects in the row
     */
    public List<Seat> getSeatsByRow(Row row) {
        return seatRepository.getAll().stream()
                .filter(seat -> seat.getRow().getID().equals(row.getID()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves available seats in a row for a specific event.
     *
     * @param row   the row to check
     * @param event the event for which to check availability
     * @return a list of available seats
     */
    public List<Seat> getAvailableSeatsInRow(Row row, Event event) {
        return getSeatsByRow(row).stream()
                .filter(seat -> !seatService.isSeatReservedForEvent(seat, event))
                .collect(Collectors.toList());
    }

   /* *//**
     * Recommends a seat in a row for a customer based on availability.
     *
     * @param customer the customer for whom the seat is recommended
     * @param row      the row to check
     * @param event    the event for which the seat is recommended
     * @return the recommended Seat object, or null if no seat is found
     *//*
    public Seat recommendSeatInRow(Customer customer, Row row, Event event) {
        List<Seat> availableSeats = getAvailableSeatsInRow(row, event);

        // Use SeatService to recommend a seat
        return seatService.recommendSeatFromList(customer, availableSeats);
    }
*/
}