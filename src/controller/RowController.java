package controller;

import model.*;
import service.RowService;

import java.util.List;

/**
 * Controller for managing row-related operations.
 */
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
     *
     * @param section   The Section to which the row belongs.
     * @param rowCapacity The capacity of the row.
     * @return The created Row.
     */
    public Row createRow(Section section, int rowCapacity) {
        try {
            Row row = rowService.createRow(section, rowCapacity);
            System.out.println("Row created successfully: " + row);
            return row;
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create row: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing Row.
     *
     * @param rowId       The ID of the row.
     * @param rowCapacity The new capacity of the row.
     * @return The updated Row.
     */
    public Row updateRow(int rowId, int rowCapacity) {
        Row updatedRow = rowService.updateRow(rowId, rowCapacity);
        if (updatedRow != null) {
            System.out.println("Row updated successfully: " + updatedRow);
        } else {
            System.out.println("Failed to update row. Row with ID " + rowId + " not found.");
        }
        return updatedRow;
    }

    /**
     * Deletes all Rows in a specific Section.
     *
     * @param sectionId The ID of the Section.
     */
    public void deleteRowsBySection(int sectionId) {
        try {
            rowService.deleteRowsBySection(sectionId);
            System.out.println("All rows in Section ID " + sectionId + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete rows: " + e.getMessage());
        }
    }

    /**
     * Deletes a Row by its ID.
     *
     * @param rowId The ID of the row.
     */
    public void deleteRow(int rowId) {
        try {
            rowService.deleteRow(rowId);
            System.out.println("Row with ID " + rowId + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete row: " + e.getMessage());
        }
    }

    /**
     * Retrieves a Row by its ID.
     *
     * @param rowId The ID of the row.
     * @return The Row object.
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
     *
     * @return A list of all Rows.
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
     *
     * @param rowId         The ID of the row.
     * @param numberOfSeats The number of seats to add.
     */
    public void addSeatsToRow(int rowId, int numberOfSeats) {
        try {
            rowService.addSeatsToRow(rowId, numberOfSeats);
            System.out.println("Added " + numberOfSeats + " seats to Row with ID " + rowId + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add seats: " + e.getMessage());
        }
    }

    /**
     * Retrieves all Seats in a specific Row.
     *
     * @param rowId The ID of the row.
     * @return A list of Seats in the row.
     */
    public List<Seat> getSeatsByRow(int rowId) {
        List<Seat> seats = rowService.getSeatsByRow(rowId);
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
     *
     * @param sectionId The ID of the section.
     * @return A list of Rows in the section.
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
     *
     * @param rowId   The ID of the row.
     * @param eventId The ID of the event.
     * @return A list of available Seats.
     */
    public List<Seat> getAvailableSeatsInRow(int rowId, int eventId) {
        List<Seat> availableSeats = rowService.getAvailableSeatsInRow(rowId, eventId);
        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats in Row ID " + rowId + " for Event ID " + eventId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats in Row ID " + rowId + " for Event ID " + eventId + ".");
        }
        return availableSeats;
    }

    /**
     * Recommends the closest available Seat in a Row.
     *
     * @param rowId      The ID of the row.
     * @param seatNumber The desired seat number.
     * @return The recommended Seat.
     */
    public Seat recommendClosestSeat(int rowId, int seatNumber) {
        Seat recommendedSeat = rowService.recommendClosestSeat(rowId, seatNumber);
        if (recommendedSeat != null) {
            System.out.println("Recommended seat: " + recommendedSeat);
        } else {
            System.out.println("No suitable seat found in Row ID " + rowId + ".");
        }
        return recommendedSeat;
    }
}
