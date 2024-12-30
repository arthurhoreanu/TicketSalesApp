package service;

import controller.Controller;
import model.*;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing row-related operations.
 */
public class RowService {

    private final IRepository<Row> rowRepository;
    private final SeatService seatService;
    static Controller controller = ControllerProvider.getController();

    public RowService(RepositoryFactory repositoryFactory, SeatService seatService) {
        this.rowRepository = repositoryFactory.createRowRepository();
        this.seatService = seatService;
    }

    public Row createRow(Section section, int rowCapacity) {
        if (section == null) {
            throw new IllegalArgumentException("Section cannot be null");
        }
        Row row = new Row(0, rowCapacity, section);
        rowRepository.create(row);
        section.addRow(row);
        return row;
    }

    public Row updateRow(int rowId, int rowCapacity) {
        Row row = findRowByID(rowId);
        if (row == null) {
            return null;
        }
        row.setRowCapacity(rowCapacity);
        rowRepository.update(row);
        return row;
    }

    public void deleteRowsBySection(int sectionID) {
        List<Row> rows = rowRepository.getAll().stream().
                filter(row -> row.getSection().getID()==sectionID)
                .toList();
        for (Row row : rows) {
            seatService.deleteSeatsByRow(row.getID());
            rowRepository.delete(row.getID());
        }
    }

    public void deleteRow(int rowID) {
        Row row = findRowByID(rowID);
        if (row == null) {
            throw new IllegalArgumentException("Row not found");
        }
        seatService.deleteSeatsByRow(rowID);
        rowRepository.delete(rowID);
    }

    public Row findRowByID(int rowId) {
        return rowRepository.read(rowId);
    }

    public List<Row> getAllRows() {
        return rowRepository.getAll();
    }

    public void addSeatsToRow(int rowId, int numberOfSeats) {
        Row row = findRowByID(rowId);
        if (row == null) {
            throw new IllegalArgumentException("Row not found");
        }
        for (int i = 1; i <= numberOfSeats; i++) {
            seatService.createSeat(rowId, i);
        }
        rowRepository.update(row);
    }

    public List<Row> findRowsBySection(int sectionId) {
        Section section = controller.findSectionByID(sectionId);
        return section != null ? section.getRows() : new ArrayList<>();
    }

    public List<Seat> getAvailableSeatsInRow(int rowId, int eventId) {
        Row row = findRowByID(rowId);
        if (row == null) {
            return new ArrayList<>();
        }
        return row.getSeats().stream()
                .filter(seat -> !seat.isReserved() && seatService.isSeatForEvent(seat, eventId))
                .collect(Collectors.toList());
    }

    public Seat recommendClosestSeat(int rowId, int seatNumber) {
        Row row = findRowByID(rowId);
        if (row == null) {
            return null;
        }
        return row.getSeats().stream()
                .filter(seat -> !seat.isReserved())
                .min((seat1, seat2) -> Integer.compare(
                        Math.abs(seat1.getNumber() - seatNumber),
                        Math.abs(seat2.getNumber() - seatNumber)
                ))
                .orElse(null);
    }

    public List<Seat> getSeatsByRow(int rowId) {
        Row row = findRowByID(rowId);
        return (row != null) ? row.getSeats() : new ArrayList<>();
    }
}