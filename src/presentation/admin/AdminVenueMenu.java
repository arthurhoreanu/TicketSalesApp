package presentation.admin;

import controller.Controller;
import model.Artist;
import model.Section;
import model.Venue;
import model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides an admin interface for managing venues through a console menu.
 * Allows the admin to add, view, update, delete venues, add sections to venues,
 * view sections within venues, and check available seats for events within venues.
 */
public class AdminVenueMenu {

    /**
     * Displays the Venue Management menu to the admin and handles menu selection.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to perform operations on venues and sections.
     */
    public static void display(Scanner scanner, Controller controller) {
        boolean inVenueMenu = true;

        while (inVenueMenu) {
            System.out.println("==== Venue Management ====");
            System.out.println("1. Add Venue");
            System.out.println("2. View Venues");
            System.out.println("3. Update Venue");
            System.out.println("4. Delete Venue");
            System.out.println("5. Add Section to Venue");
            System.out.println("6. View Sections in Venue");
            System.out.println("7. View Available Seats in Venue for Event");
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
                    handleUpdateVenue(scanner, controller);
                    break;
                case "4":
                    handleDeleteVenue(scanner, controller);
                    break;
                case "5":
                    handleAddSectionToVenue(scanner, controller);
                    break;
                case "6":
                    handleViewSectionsInVenue(scanner, controller);
                    break;
                case "7":
                    handleViewAvailableSeats(scanner, controller);
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
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to add the venue.
     */
    private static void handleAddVenue(Scanner scanner, Controller controller) {
        System.out.println("=== Add Venue ===");
        System.out.print("Enter venue name: ");
        String name = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());
        controller.addVenue(name, location, capacity, new ArrayList<>());
        System.out.println("Venue added successfully.");
    }

    /**
     * Handles viewing all available venues.
     *
     * @param controller The {@code Controller} object used to retrieve and display venues.
     */
    private static void handleViewVenues(Controller controller) {
        System.out.println("=== View Venues ===");

        List<Venue> venues = controller.getAllVenues();
        if (venues.isEmpty()) {
            System.out.println("No venues available.");
        } else {
            for (Venue venue : venues) {
                System.out.println(venue);
            }
        }
    }

    /**
     * Handles updating an existing venue.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to update the venue.
     */
    private static void handleUpdateVenue(Scanner scanner, Controller controller) {
        System.out.println("=== Update Venue ===");

        handleViewVenues(controller);

        System.out.print("Enter Venue ID to update: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        Venue venue = controller.findVenueById(venueId);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        System.out.print("Enter new name (or press Enter to keep current name): ");
        String newName = scanner.nextLine();
        if (newName.isEmpty()) newName = venue.getVenueName();

        System.out.print("Enter new location (or press Enter to keep current location): ");
        String newLocation = scanner.nextLine();
        if (newLocation.isEmpty()) newLocation = venue.getLocation();

        System.out.print("Enter new capacity (or press Enter to keep current capacity): ");
        String capacityInput = scanner.nextLine();
        int newCapacity = capacityInput.isEmpty() ? venue.getVenueCapacity() : Integer.parseInt(capacityInput);

        controller.updateVenue(venueId, newName, newLocation, newCapacity, venue.getSections());
        System.out.println("Venue updated successfully.");
    }

    /**
     * Handles deleting a venue by its ID.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to delete the venue.
     */
    private static void handleDeleteVenue(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Venue ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID to delete: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        controller.deleteVenue(venueId);
        System.out.println("Venue deleted successfully.");
    }

    /**
     * Handles adding a new section to a specific venue.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to add the section to the venue.
     */
    private static void handleAddSectionToVenue(Scanner scanner, Controller controller) {
        System.out.println("=== Add Section to Venue ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID to add a section: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        Venue venue = controller.findVenueById(venueId);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        System.out.print("Enter section name: ");
        String sectionName = scanner.nextLine();
        System.out.print("Enter section capacity: ");
        int sectionCapacity = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter number of rows: ");
        int rowCount = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter seats per row: ");
        int seatsPerRow = Integer.parseInt(scanner.nextLine());

        Section section = controller.createSectionWithSeats(sectionName, venue.getSections().size() + 1, sectionCapacity, rowCount, seatsPerRow, venue);
        venue.getSections().add(section);
        System.out.println("Section added successfully to venue.");
    }

    /**
     * Handles viewing all sections within a specific venue.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to retrieve and display sections.
     */
    private static void handleViewSectionsInVenue(Scanner scanner, Controller controller) {
        System.out.println("=== View Sections in Venue ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID to view sections: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        Venue venue = controller.findVenueById(venueId);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        for (Section section : venue.getSections()) {
            System.out.println(section);
        }
    }

    /**
     * Handles viewing available seats in a specific venue for an event.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to retrieve and display available seats.
     */
    private static void handleViewAvailableSeats(Scanner scanner, Controller controller) {
        System.out.println("=== View Available Seats in Venue ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID to check available seats: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        Venue venue = controller.findVenueById(venueId);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());
        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        controller.getAvailableSeats(venue, event);
    }
}
