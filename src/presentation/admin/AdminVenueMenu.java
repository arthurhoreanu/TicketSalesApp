package presentation.admin;

import controller.Controller;
import exception.EntityNotFoundException;
import exception.ValidationException;
import model.*;
import java.util.List;
import java.util.Scanner;

/**
 * Provides an admin interface for managing venues, sections, rows, and seats through a console menu.
 */
public class AdminVenueMenu {

    /**
     * Displays the Venue Management menu to the admin and handles menu selection.
     */
    public static void display(Scanner scanner, Controller controller) {
        boolean inVenueMenu = true;
        while (inVenueMenu) {
            try {
                System.out.println("==== Venue Management ====");
                System.out.println("1. Add Venue");
                System.out.println("2. View Venues");
                System.out.println("3. Add Section to Venue");
                System.out.println("4. Add Rows and Seats to Section");
                System.out.println("5. View Full Venue Structure");
                System.out.println("6. Delete Venue");
                System.out.println("0. Back to Admin Menu");
                System.out.println("==========================");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        handleAddVenue(scanner, controller);
                        break;
                    case "2":
                        handleViewVenues(controller);
                        break;
                    case "3":
                        handleAddSectionToVenue(scanner, controller);
                        break;
                    case "4":
                        handleAddRowsAndSeatsToSection(scanner, controller);
                        break;
                    case "5":
                        handleViewFullVenueStructure(scanner, controller);
                        break;
                    case "6":
                        handleDeleteVenue(scanner, controller);
                        break;
                    case "0":
                        inVenueMenu = false;
                        break;
                    default:
                        throw new ValidationException("Invalid option. Please select a valid number between 0 and 6.");
                }
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }

    /**
     * Handles adding a new venue.
     */
    private static void handleAddVenue(Scanner scanner, Controller controller) {
        try {
            System.out.println("=== Add Venue ===");
            System.out.print("Enter venue name: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                throw new ValidationException("Venue name cannot be empty.");
            }
            System.out.print("Enter location: ");
            String location = scanner.nextLine();
            if (location.trim().isEmpty()) {
                throw new ValidationException("Location cannot be empty.");
            }
            System.out.print("Enter total capacity: ");
            int capacity = Integer.parseInt(scanner.nextLine());
            if (capacity <= 0) {
                throw new ValidationException("Capacity cannot be zero or negative.");
            }
            System.out.print("Does the venue have seats (true/false)? ");
            boolean hasSeats = Boolean.parseBoolean(scanner.nextLine());
            controller.createVenue(name, location, capacity, hasSeats);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all venues.
     */
    private static void handleViewVenues(Controller controller) {
        System.out.println("=== View Venues ===");
        List<Venue> venues = controller.getAllVenues();
        if (venues.isEmpty()) {
            System.out.println("No venues available.");
        } else {
            venues.forEach(venue -> System.out.println(
                    "ID: " + venue.getID() +
                            ", Name: " + venue.getVenueName() +
                            ", Location: " + venue.getLocation() +
                            ", Capacity: " + venue.getVenueCapacity()
            ));
        }
    }

    /**
     * Handles adding a section to a venue.
     */
    private static void handleAddSectionToVenue(Scanner scanner, Controller controller) {
        try {
            System.out.println("=== Add Section to Venue ===");
            handleViewVenues(controller);
            System.out.print("Enter Venue ID: ");
            int venueID = Integer.parseInt(scanner.nextLine());
            if (venueID <= 0) {
                throw new ValidationException("Venue ID cannot be zero or negative.");
            }
            Venue venue = controller.findVenueByID(venueID);
            if (venue == null) {
                throw new EntityNotFoundException("Venue with ID " + venueID + " not found.");
            }
            System.out.print("Enter the number of sections to add: ");
            int numberOfSections = Integer.parseInt(scanner.nextLine());
            if (numberOfSections <= 0) {
                throw new ValidationException("Number of sections cannot be zero or negative.");
            }
            System.out.print("Enter section capacity: ");
            int sectionCapacity = Integer.parseInt(scanner.nextLine());
            if (sectionCapacity <= 0) {
                throw new ValidationException("Section capacity cannot be zero or negative.");
            }
            System.out.print("Enter default section name prefix: ");
            String sectionNamePrefix = scanner.nextLine();
            if (sectionNamePrefix.trim().isEmpty()) {
                throw new ValidationException("Section name prefix cannot be empty.");
            }
            controller.addSectionToVenue(venueID, numberOfSections, sectionCapacity, sectionNamePrefix);
            System.out.println("Sections added successfully.");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles adding rows and seats to a section.
     */
    private static void handleAddRowsAndSeatsToSection(Scanner scanner, Controller controller) {
        try {
            System.out.println("=== Add Rows and Seats ===");
            handleViewVenues(controller);
            System.out.print("Enter Venue ID: ");
            int venueID = Integer.parseInt(scanner.nextLine());
            if (venueID <= 0) {
                throw new ValidationException("Venue ID cannot be zero or negative.");
            }
            Venue venue = controller.findVenueByID(venueID);
            if (venue == null) {
                throw new EntityNotFoundException("Venue not found.");
            }
            List<Section> sections = controller.getSectionsByVenueID(venueID);
            if (sections.isEmpty()) {
                return;
            }
            System.out.print("Enter Section ID: ");
            int sectionID = Integer.parseInt(scanner.nextLine());
            if (sectionID <= 0) {
                throw new ValidationException("Section ID cannot be zero or negative.");
            }
            Section section = controller.findSectionByID(sectionID);
            if (section == null) {
                throw new EntityNotFoundException("Section not found.");
            }
            System.out.print("Enter number of rows: ");
            int numberOfRows = Integer.parseInt(scanner.nextLine());
            if (numberOfRows <= 0) {
                throw new ValidationException("Number of rows cannot be zero or negative.");
            }
            System.out.print("Enter number of seats per row: ");
            int seatsPerRow = Integer.parseInt(scanner.nextLine());
            if (seatsPerRow <= 0) {
                throw new ValidationException("Number of seats per row cannot be zero or negative.");
            }
            for (int i = 0; i < numberOfRows; i++) {
                Row row = controller.createRow(section, seatsPerRow);
                controller.addSeatsToRow(row.getID(), seatsPerRow);
            }
            System.out.println("Rows and seats added successfully to section: " + section.getSectionName());
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays the full structure of a venue.
     */
    private static void handleViewFullVenueStructure(Scanner scanner, Controller controller) {
        try {
            System.out.println("=== View Full Venue Structure ===");
            handleViewVenues(controller);
            System.out.print("Enter Venue ID: ");
            int venueId = Integer.parseInt(scanner.nextLine());
            Venue venue = controller.findVenueByID(venueId);
            if (venue == null) {
                throw new EntityNotFoundException("Venue not found.");
            }
            System.out.println("Venue: " + venue.getVenueName());
            List<Section> sections = controller.getSectionsByVenueID(venueId);
            for (Section section : sections) {
                System.out.println("  Section: " + section.getSectionName());
                List<Row> rows = controller.findRowsBySection(section.getID());
                for (Row row : rows) {
                    System.out.println("    Row: " + row.getID());
                    List<Seat> seats = controller.getSeatsByRow(row.getID());
                    System.out.println("      Seats: " + seats.size());
                }
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the deletion of a venue and all its associated components.
     */
    private static void handleDeleteVenue(Scanner scanner, Controller controller) {
        try {
            System.out.println("=== Delete Venue ===");
            handleViewVenues(controller);
            System.out.print("Enter Venue ID to delete: ");
            int venueId = Integer.parseInt(scanner.nextLine());
            if (venueId <= 0) {
                throw new ValidationException("Venue ID cannot be zero or negative.");
            }
            Venue venue = controller.findVenueByID(venueId);
            if (venue == null) {
                throw new EntityNotFoundException("Venue not found.");
            }
            System.out.print("Are you sure you want to delete venue '" + venue.getVenueName() +
                    "' and all its sections, rows, and seats? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                boolean deleted = controller.deleteVenue(venueId);
                if (deleted) {
                    System.out.println("Venue and all its components were successfully deleted.");
                } else {
                    System.out.println("Failed to delete venue.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}