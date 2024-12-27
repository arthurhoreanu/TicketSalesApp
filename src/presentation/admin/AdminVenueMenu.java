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
        int venueId = Integer.parseInt(scanner.nextLine());
        Venue venue = controller.findVenueByID(venueId);

        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        System.out.print("Enter section name: ");
        String sectionName = scanner.nextLine();
        System.out.print("Enter section capacity: ");
        int sectionCapacity = Integer.parseInt(scanner.nextLine());

        controller.addSectionToVenue(venueId, sectionName, sectionCapacity);
        System.out.println("Section added successfully.");
    }

    /**
     * Handles adding rows and seats to a section.
     */
    private static void handleAddRowsAndSeatsToSection(Scanner scanner, Controller controller) {
        System.out.println("=== Add Rows and Seats to Section ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID: ");
        int venueId = Integer.parseInt(scanner.nextLine());
        Venue venue = controller.findVenueByID(venueId);

        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        controller.getSectionsByVenueID(venueId);
        System.out.print("Enter Section ID: ");
        int sectionId = Integer.parseInt(scanner.nextLine());
        Section section = controller.findSectionByID(sectionId);

        if (section == null) {
            System.out.println("Section not found.");
            return;
        }

        System.out.print("Enter number of rows: ");
        int numberOfRows = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter seats per row: ");
        int seatsPerRow = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= numberOfRows; i++) {
            Row row = controller.createRow(sectionId, seatsPerRow);
            controller.addSeatsToRow(row.getID(), seatsPerRow);
        }

        System.out.println("Rows and seats added successfully to section: " + section.getSectionName());
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
                List<Seat> seats = controller.getSeatsByRowID(row.getID());

                System.out.println("      Seats: " + seats.size());
            }
        }
    }
}