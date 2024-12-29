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
 * Service class for managing row-related operations, including creating rows with seats,
 * retrieving seats, and handling row-related updates and deletions.
 */
public class RowService {

    private final IRepository<Row> rowRepository;
    private final IRepository<Seat> seatRepository;
    private final FileRepository<Row> rowFileRepository;
    private final FileRepository<Seat> seatFileRepository;
    private final SectionService sectionService;

    /**
     * Constructs a RowService with dependencies for managing rows and seats.
     *
     * @param rowRepository  the repository for managing row data
     * @param seatRepository the repository for managing seat data
     */
    public RowService(IRepository<Row> rowRepository, IRepository<Seat> seatRepository, SectionService sectionService) {
        this.rowRepository = rowRepository;
        this.seatRepository = seatRepository;
        this.sectionService = sectionService;
        // File repositories
        this.rowFileRepository = new FileRepository<>("src/repository/data/rows.csv", Row::fromCsv);
        this.seatFileRepository = new FileRepository<>("src/repository/data/seats.csv", Seat::fromCsv);

        // Sync data from file and database repositories to the primary repository
        syncFromCsv();
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
     * Creates a new Row and saves it to all repositories.
     *
     * @param sectionId  the ID of the Section to which the Row belongs.
     * @param rowCapacity the capacity of the Row.
     * @return the created Row object.
     */
    public Row createRow(int sectionId, int rowCapacity) {
        Section section = sectionService.findSectionByID(sectionId); // Assuming SectionService is accessible
        if (section == null) {
            return null; // Section not found
        }

        int newId = rowRepository.getAll().size() + 1; // Generate a new ID dynamically
        Row row = new Row(newId, rowCapacity, section);
        rowRepository.create(row);
        rowFileRepository.create(row);

        return row;
    }

    /**
     * Updates an existing Row.
     *
     * @param rowId       the ID of the Row to update.
     * @param rowCapacity the new capacity for the Row.
     * @return the updated Row object, or null if not found.
     */
    public Row updateRow(int rowId, int rowCapacity) {
        Row row = findRowByID(rowId);
        if (row == null) {
            return null; // Row not found
        }

        row.setRowCapacity(rowCapacity);
        rowRepository.update(row);
        rowFileRepository.update(row);

        return row;
    }

    /**
     * Deletes a Row by its ID.
     *
     * @param rowId the ID of the Row to delete.
     * @return true if the Row was successfully deleted, false otherwise.
     */
    public boolean deleteRow(int rowId) {
        Row row = findRowByID(rowId);
        if (row == null) {
            return false; // Row not found
        }

        // Remove associated seats
        for (Seat seat : row.getSeats()) {
            seatRepository.delete(seat.getID());
            seatFileRepository.delete(seat.getID());
        }

        rowRepository.delete(rowId);
        rowFileRepository.delete(rowId);

        return true;
    }

    /**
     * Retrieves a Row by its ID.
     *
     * @param rowId the ID of the Row to retrieve.
     * @return the Row object, or null if not found.
     */
    public Row findRowByID(int rowId) {
        return rowRepository.read(rowId);
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
     * Adds seats to a Row.
     *
     * @param rowId         the ID of the Row.
     * @param numberOfSeats the number of seats to add.
     */
    public void addSeatsToRow(int rowId, int numberOfSeats) {
        Row row = findRowByID(rowId);
        if (row == null) {
            return; // Row not found
        }

        for (int i = 1; i <= numberOfSeats; i++) {
            Seat seat = new Seat(0, i, false, row); // Create a new Seat
            row.addSeat(seat); // Add the Seat to the Row
            seatRepository.create(seat);
            seatFileRepository.create(seat);
        }

        rowRepository.update(row); // Update the Row in the repository
        rowFileRepository.update(row);
    }

    /**
     * Retrieves all Seats in a Row by its ID.
     *
     * @param rowId the ID of the Row.
     * @return a list of Seats in the Row.
     */
    public List<Seat> getSeatsByRowID(int rowId) {
        Row row = findRowByID(rowId);
        return (row != null) ? row.getSeats() : new ArrayList<>();
    }

    /**
     * Finds Rows by their associated Section.
     *
     * @param sectionId the ID of the Section.
     * @return a list of Rows belonging to the Section.
     */
    public List<Row> findRowsBySection(int sectionId) {
        return rowRepository.getAll().stream()
                .filter(row -> row.getSection() != null && row.getSection().getID() == sectionId)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all available Seats in a Row for a specific Event.
     *
     * @param rowId   the ID of the Row.
     * @param eventId the ID of the Event.
     * @return a list of available Seats in the Row.
     */
    public List<Seat> getAvailableSeatsInRow(int rowId, int eventId) {
        Row row = findRowByID(rowId);
        if (row == null) {
            return new ArrayList<>();
        }

        return row.getSeats().stream()
                .filter(seat -> !seat.isReserved() && seat.getTicket() != null
                        && seat.getTicket().getEvent().getID() == eventId)
                .collect(Collectors.toList());
    }

}