package controller;

import model.*;
import service.RowService;

import java.util.List;

/**
 * Controller for managing row-related operations.
 */
// TODO de rescris pentru noul Service
public class RowController {
    private final RowService rowService;

    /**
     * Constructs a RowController with the specified RowService.
     *
     * @param rowService The service for managing rows.
     */
    public RowController(RowService rowService) {
        this.rowService = rowService;
    }

    /**
     * Creates a new Row in a specific Section.
     */
    public Row createRow(int sectionId, int rowCapacity) {
        Row row = rowService.createRow(sectionId, rowCapacity);
        if (row != null) {
            System.out.println("Row created successfully: " + row);
        } else {
            System.out.println("Failed to create row. Section with ID " + sectionId + " not found.");
        }
        return row;
    }

    /**
     * Updates an existing Row.
     */
    public void updateRow(int rowId, int rowCapacity) {
        Row updatedRow = rowService.updateRow(rowId, rowCapacity);

        if (updatedRow != null) {
            System.out.println("Row updated successfully: " + updatedRow);
        } else {
            System.out.println("Failed to update row. Row with ID " + rowId + " not found.");
        }
    }

    /**
     * Deletes a Row by its ID.
     */
    public void deleteRow(int rowId) {
        boolean deleted = rowService.deleteRow(rowId);
        if (deleted) {
            System.out.println("Row with ID " + rowId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete row. Row with ID " + rowId + " not found.");
        }
    }

    /**
     * Retrieves a Row by its ID.
     */
    public Row findRowByID(int rowId) {
        Row row = rowService.findRowByID(rowId);
        if (row != null) {
            System.out.println("Row found: " + row);
        } else {
            System.out.println("Row with ID " + rowId + " not found.");
        }
        return row;
    }

    /**
     * Retrieves all Rows.
     */
    public List<Row> getAllRows() {
        List<Row> rows = rowService.getAllRows();
        if (!rows.isEmpty()) {
            System.out.println("All rows:");
            rows.forEach(System.out::println);
        } else {
            System.out.println("No rows available.");
        }
        return rows;
    }

    /**
     * Adds Seats to a specific Row.
     */
    public void addSeatsToRow(int rowId, int numberOfSeats) {
        rowService.addSeatsToRow(rowId, numberOfSeats);
        System.out.println("Added " + numberOfSeats + " seats to Row with ID " + rowId + ".");
    }

    /**
     * Retrieves all Seats in a specific Row.
     */
    public List<Seat> getSeatsByRowID(int rowId) {
        List<Seat> seats = rowService.getSeatsByRowID(rowId);
        if (!seats.isEmpty()) {
            System.out.println("Seats in Row ID " + rowId + ":");
            seats.forEach(System.out::println);
        } else {
            System.out.println("No seats found for Row ID " + rowId + ".");
        }
        return seats;
    }

    /**
     * Finds Rows by their associated Section.
     */
    public List<Row> findRowsBySection(int sectionId) {
        List<Row> rows = rowService.findRowsBySection(sectionId);
        if (!rows.isEmpty()) {
            System.out.println("Rows in Section ID " + sectionId + ":");
            rows.forEach(System.out::println);
        } else {
            System.out.println("No rows found for Section ID " + sectionId + ".");
        }
        return rows;
    }

    /**
     * Retrieves all available Seats in a Row for a specific Event.
     */
    public void getAvailableSeatsInRow(int rowId, int eventId) {
        List<Seat> availableSeats = rowService.getAvailableSeatsInRow(rowId, eventId);

        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats in Row ID " + rowId + " for Event ID " + eventId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats in Row ID " + rowId + " for Event ID " + eventId + ".");
        }
    }
}