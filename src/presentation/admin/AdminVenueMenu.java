package presentation.admin;

import controller.Controller;
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
            System.out.println("==== Venue Management ====");
            System.out.println("1. Add Venue");
            System.out.println("2. View Venues");
            System.out.println("3. Add Section to Venue");
            System.out.println("4. Add Rows and Seats to Section");
            System.out.println("5. View Full Venue Structure");
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
                case "0":
                    inVenueMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }

    /**
     * Handles adding a new venue.
     */
    private static void handleAddVenue(Scanner scanner, Controller controller) {
        System.out.println("=== Add Venue ===");
        System.out.print("Enter venue name: ");
        String name = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter total capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());
        System.out.print("Does the venue have seats (true/false)? ");
        boolean hasSeats = Boolean.parseBoolean(scanner.nextLine());
        controller.createVenue(name, location, capacity, hasSeats);
        System.out.println("Venue added successfully.");
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
        System.out.println("=== Add Section to Venue ===");
        handleViewVenues(controller);
        System.out.print("Enter Venue ID: ");
        int venueId;
        try {
            venueId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Venue ID must be a number.");
            return;
        }
        Venue venue = controller.findVenueByID(venueId);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }
        System.out.print("Enter the number of sections to add: ");
        int numberOfSections;
        try {
            numberOfSections = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Number of sections must be a number.");
            return;
        }
        System.out.print("Enter section capacity: ");
        int sectionCapacity;
        try {
            sectionCapacity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Section capacity must be a number.");
            return;
        }
        System.out.print("Enter default section name prefix: ");
        String sectionNamePrefix = scanner.nextLine();
        try {
            controller.addSectionToVenue(venueId, numberOfSections, sectionCapacity, sectionNamePrefix);
            System.out.println("Sections added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles adding rows and seats to a section.
     */
    private static void handleAddRowsAndSeatsToSection(Scanner scanner, Controller controller) {
        System.out.println("=== Add Rows and Seats to Section ===");

        // Display venues and prompt for Venue ID
        handleViewVenues(controller);
        System.out.print("Enter Venue ID: ");
        int venueId;
        try {
            venueId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Venue ID must be a number.");
            return;
        }

        Venue venue = controller.findVenueByID(venueId);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        // Display sections for the selected venue
        System.out.println("Sections in the venue:");
        List<Section> sections = controller.getSectionsByVenueID(venueId);
        if (sections.isEmpty()) {
            System.out.println("No sections found for this venue.");
            return;
        }
        sections.forEach(System.out::println);

        // Prompt for Section ID
        System.out.print("Enter Section ID: ");
        int sectionId;
        try {
            sectionId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Section ID must be a number.");
            return;
        }

        Section section = controller.findSectionByID(sectionId);
        if (section == null) {
            System.out.println("Section not found.");
            return;
        }

        // Prompt for number of rows and seats per row
        System.out.print("Enter number of rows: ");
        int numberOfRows;
        try {
            numberOfRows = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Number of rows must be a number.");
            return;
        }

        System.out.print("Enter seats per row: ");
        int seatsPerRow;
        try {
            seatsPerRow = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Seats per row must be a number.");
            return;
        }

        // Add rows and seats to the section
        try {
            for (int i = 1; i <= numberOfRows; i++) {
                Row row = controller.createRow(section, seatsPerRow); // Create a new row
                controller.addSeatsToRow(row.getID(), seatsPerRow);    // Add seats to the created row
            }
            System.out.println("Rows and seats added successfully to section: " + section.getSectionName());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays the full structure of a venue.
     */
    private static void handleViewFullVenueStructure(Scanner scanner, Controller controller) {
        System.out.println("=== View Full Venue Structure ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID: ");
        int venueId = Integer.parseInt(scanner.nextLine());
        Venue venue = controller.findVenueByID(venueId);

        if (venue == null) {
            System.out.println("Venue not found.");
            return;
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
    }
}