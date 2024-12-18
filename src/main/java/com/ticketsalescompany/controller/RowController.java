package com.ticketsalescompany.controller;

import com.ticketsalescompany.model.*;
import com.ticketsalescompany.service.RowService;

import java.util.List;

/**
 * The RowController class handles operations related to rows within sections,
 * including creating rows with seats, managing rows, and seat-related actions.
 */
public class RowController {
    private final RowService rowService;

    /**
     * Constructs a RowController with the specified RowService.
     *
     * @param rowService the service handling row-related operations
     */
    public RowController(RowService rowService) {
        this.rowService = rowService;
    }

    /**
     * Finds and retrieves a row by its unique ID.
     *
     * @param rowID the ID of the row to find
     * @return the Row object if found, or null if not
     */
    public Row findRowByID(int rowID) {
        Row row = rowService.findRowByID(rowID);
        if (row != null) {
            System.out.println("Row found: ID " + row.getID() + ", Capacity: " + row.getRowCapacity());
        } else {
            System.out.println("Row with ID " + rowID + " not found.");
        }
        return row;
    }

    /**
     * Creates a new row within a specified section and initializes seats for the row.
     *
     * @param rowCapacity the number of seats in the row
     * @param section     the section to which the row belongs
     */
    public void createRowWithSeats(int rowCapacity, Section section) {
        Row row = rowService.createRowWithSeats(rowCapacity, section);
        System.out.println("Row created with ID " + row.getID() + " in section '" + section.getSectionName() + "' with " + rowCapacity + " seats.");
    }

    /**
     * Updates an existing row's capacity.
     *
     * @param rowID       the ID of the row to update
     * @param newCapacity the new capacity for the row
     */
    public void updateRow(int rowID, int newCapacity) {
        boolean isUpdated = rowService.updateRow(rowID, newCapacity);
        if (isUpdated) {
            System.out.println("Row with ID " + rowID + " updated to new capacity: " + newCapacity);
        } else {
            System.out.println("Row with ID " + rowID + " not found. Update failed.");
        }
    }

    /**
     * Deletes a row by its ID, including all associated seats.
     *
     * @param rowID the ID of the row to delete
     */
    public void deleteRow(int rowID) {
        boolean isDeleted = rowService.deleteRow(rowID);
        if (isDeleted) {
            System.out.println("Row with ID " + rowID + " and its associated seats have been deleted.");
        } else {
            System.out.println("Row with ID " + rowID + " not found. Deletion failed.");
        }
    }

    /**
     * Retrieves and displays all rows in the repository.
     */
    public void getAllRows() {
        List<Row> rows = rowService.getAllRows();
        if (rows.isEmpty()) {
            System.out.println("No rows available.");
        } else {
            System.out.println("All rows:");
            rows.forEach(row -> System.out.println("- ID: " + row.getID() + ", Capacity: " + row.getRowCapacity()));
        }
    }

    /**
     * Retrieves and displays rows by section.
     *
     * @param section the section to filter rows by
     */
    public void findRowsBySection(Section section) {
        List<Row> rows = rowService.findRowsBySection(section);
        if (rows.isEmpty()) {
            System.out.println("No rows found for section '" + section.getSectionName() + "'.");
        } else {
            System.out.println("Rows in section '" + section.getSectionName() + "':");
            rows.forEach(row -> System.out.println("- ID: " + row.getID() + ", Capacity: " + row.getRowCapacity()));
        }
    }

    /**
     * Retrieves and displays available seats in a row for a specific event.
     *
     * @param row   the row to check
     * @param event the event for which seat availability is checked
     */
    public void getAvailableSeatsInRow(Row row, Event event) {
        List<Seat> availableSeats = rowService.getAvailableSeatsInRow(row, event);
        if (availableSeats.isEmpty()) {
            System.out.println("No available seats in row with ID " + row.getID() + " for event '" + event.getEventName() + "'.");
        } else {
            System.out.println("Available seats in row with ID " + row.getID() + " for event '" + event.getEventName() + "':");
            availableSeats.forEach(seat -> System.out.println("- Seat ID: " + seat.getID() + ", Number: " + seat.getRow()));
        }
    }

    /**
     * Retrieves and displays all seats in a specific row.
     *
     * @param row the row to retrieve seats from
     */
    public void getSeatsByRow(Row row) {
        List<Seat> seats = rowService.getSeatsByRow(row);
        if (seats.isEmpty()) {
            System.out.println("No seats found in row with ID " + row.getID() + ".");
        } else {
            System.out.println("Seats in row with ID " + row.getID() + ":");
            seats.forEach(seat -> System.out.println("- Seat ID: " + seat.getID() + ", Number: " + seat.getRow()));
        }
    }

    /* *//**
     * Recommends a seat in a row for a customer based on preferences and availability.
     *
     * @param customer the customer for whom the seat is recommended
     * @param row      the row to check
     * @param event    the event for which the seat is recommended
     *//*
    public void recommendSeatInRow(Customer customer, Row row, Event event) {
        Seat recommendedSeat = rowService.recommendSeatInRow(customer, row, event);
        if (recommendedSeat != null) {
            System.out.println("Recommended seat for customer '" + customer.getUsername() + "' in row with ID " + row.getID() +
                    " for event '" + event.getEventName() + "': Seat ID " + recommendedSeat.getID() +
                    ", Number: " + recommendedSeat.getSeatNumber());
        } else {
            System.out.println("No preferred seat available for customer '" + customer.getUsername() +
                    "' in row with ID " + row.getID() + " for event '" + event.getEventName() + "'.");
        }
    }*/
}
