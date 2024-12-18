package presentation.admin;

import controller.Controller;
import model.Row;
import model.Section;
import model.Seat;
import model.Venue;
import model.Event;

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
            System.out.println("3. Update Venue");
            System.out.println("4. Delete Venue");
            System.out.println("5. Add Section to Venue");
            System.out.println("6. View Sections in Venue");
            System.out.println("7. Add Row to Section");
            System.out.println("8. View Rows in Section");
            System.out.println("9. Add Seats to Row");
            System.out.println("10. View Seats in Row");
            System.out.println("11. View Available Seats in Venue for Event");
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
                    handleAddRowToSection(scanner, controller);
                    break;
                case "8":
                    handleViewRowsInSection(scanner, controller);
                    break;
                case "9":
                    handleAddSeatsToRow(scanner, controller);
                    break;
                case "10":
                    handleViewSeatsInRow(scanner, controller);
                    break;
                case "11":
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

    private static void handleAddVenue(Scanner scanner, Controller controller) {
        System.out.println("=== Add Venue ===");
        System.out.print("Enter venue name: ");
        String name = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());

        controller.createVenue(name, location, capacity);
        System.out.println("Venue added successfully.");
    }

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

    private static void handleUpdateVenue(Scanner scanner, Controller controller) {
        System.out.println("=== Update Venue ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID to update: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        Venue venue = controller.findVenueByID(venueId);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        System.out.print("Enter new name (press Enter to keep current): ");
        String newName = scanner.nextLine();
        System.out.print("Enter new location (press Enter to keep current): ");
        String newLocation = scanner.nextLine();
        System.out.print("Enter new capacity (press Enter to keep current): ");
        String capacityInput = scanner.nextLine();

        String finalName = newName.isEmpty() ? venue.getVenueName() : newName;
        String finalLocation = newLocation.isEmpty() ? venue.getLocation() : newLocation;
        int finalCapacity = capacityInput.isEmpty() ? venue.getVenueCapacity() : Integer.parseInt(capacityInput);

        controller.updateVenue(venueId, finalName, finalLocation, finalCapacity);
        System.out.println("Venue updated successfully.");
    }

    private static void handleDeleteVenue(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Venue ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID to delete: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        controller.deleteVenue(venueId);
        System.out.println("Venue deleted successfully.");
    }

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

        controller.createSection(sectionName, sectionCapacity, venue);
        System.out.println("Section added successfully.");
    }

    private static void handleViewSectionsInVenue(Scanner scanner, Controller controller) {
        System.out.println("=== View Sections in Venue ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        Venue venue = controller.findVenueByID(venueId);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        System.out.println("Sections in venue '" + venue.getVenueName() + "':");
        controller.getAllSections().stream()
                .filter(section -> section.getVenue().getID() == venue.getID()) //todo eroarea de aici trebuie rezolvata, cred ca e singura naspa
                .forEach(section -> System.out.println(
                        "ID: " + section.getID() +
                                ", Name: " + section.getSectionName() +
                                ", Capacity: " + section.getSectionCapacity()
                ));
    }

    private static void handleAddRowToSection(Scanner scanner, Controller controller) {
        System.out.println("=== Add Row to Section ===");
        handleViewSectionsInVenue(scanner, controller);

        System.out.print("Enter Section ID: ");
        int sectionId = Integer.parseInt(scanner.nextLine());

        Section section = controller.findSectionByID(sectionId);
        if (section == null) {
            System.out.println("Section not found.");
            return;
        }

        System.out.print("Enter Row Capacity: ");
        int rowCapacity = Integer.parseInt(scanner.nextLine());

        controller.createRowWithSeats(rowCapacity, section);
        System.out.println("Row added successfully.");
    }

    private static void handleViewRowsInSection(Scanner scanner, Controller controller) {
        System.out.println("=== View Rows in Section ===");
        handleViewSectionsInVenue(scanner, controller);

        System.out.print("Enter Section ID: ");
        int sectionId = Integer.parseInt(scanner.nextLine());

        Section section = controller.findSectionByID(sectionId);
        if (section == null) {
            System.out.println("Section not found.");
            return;
        }

        System.out.println("Rows in Section:");
        controller.findRowsBySection(section);
    }

    private static void handleAddSeatsToRow(Scanner scanner, Controller controller) {
        System.out.println("=== Add Seats to Row ===");
        handleViewRowsInSection(scanner, controller);

        System.out.print("Enter Row ID: ");
        int rowId = Integer.parseInt(scanner.nextLine());

        Row row = controller.findRowByID(rowId);
        if (row == null) {
            System.out.println("Row not found.");
            return;
        }

        System.out.print("Enter number of seats to add: ");
        int seatCount = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= seatCount; i++) {
            controller.createSeat(row.getID() * 100 + i, row, false, null);
        }
        System.out.println(seatCount + " seats added successfully.");
    }

    private static void handleViewSeatsInRow(Scanner scanner, Controller controller) {
        System.out.println("=== View Seats in Row ===");
        handleViewRowsInSection(scanner, controller);

        System.out.print("Enter Row ID: ");
        int rowId = Integer.parseInt(scanner.nextLine());

        Row row = controller.findRowByID(rowId);
        if (row == null) {
            System.out.println("Row not found.");
            return;
        }

        System.out.println("Seats in Row:");
        controller.getSeatsByRow(row);
    }

    private static void handleViewAvailableSeats(Scanner scanner, Controller controller) {
        System.out.println("=== View Available Seats in Venue ===");
        handleViewVenues(controller);

        System.out.print("Enter Venue ID: ");
        int venueId = Integer.parseInt(scanner.nextLine());

        Venue venue = controller.findVenueByID(venueId);
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
