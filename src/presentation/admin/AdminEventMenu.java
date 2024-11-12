package presentation.admin;
import controller.Controller;
import model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminEventMenu {
    public static void display(Scanner scanner, Controller controller) {
        boolean inEventMenu = true;
        while (inEventMenu) {
            System.out.println("==== Event Management ====");
            System.out.println("1. Create Event");
            System.out.println("2. View Events");
            System.out.println("3. Update Event");
            System.out.println("4. Delete Event");
            System.out.println("0. Back to Admin Menu");
            System.out.println("==========================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleCreateEvent(scanner, controller);
                    break;
                case "2":
                    handleViewEvents(controller);
                    break;
                case "3":
                    handleUpdateEvent(scanner, controller);
                    break;
                case "4":
                    handleDeleteEvent(scanner, controller);
                    break;
                case "0":
                    inEventMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }

    public static void handleCreateEvent(Scanner scanner, Controller controller) {
        System.out.println("=== Create Event ===");
        System.out.print("Enter Event Type (Concert/Sports Event): ");
        String eventType = scanner.nextLine();
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();
        System.out.print("Enter event description: ");
        String eventDescription = scanner.nextLine();
        System.out.print("Enter start date and time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime startDateTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter end date and time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime endDateTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter venue name: ");
        String venueName = scanner.nextLine();
        Venue venue = controller.findVenueByName(venueName);
        if (venue == null) {
            venue = controller.findVenueByName(venueName);
        }
        EventStatus eventStatus = EventStatus.SCHEDULED;
        List<Ticket> tickets = new ArrayList<>();

        List<Artist> artists = new ArrayList<>();
        String genre = null;

        List<Athlete> athletes = new ArrayList<>();
        String sportName = null;

        if ("Concert".equalsIgnoreCase(eventType)) {
            while (true) {
                System.out.print("Enter artist name (or type 'done' to finish): ");
                String artistName = scanner.nextLine();

                if ("done".equalsIgnoreCase(artistName)) {
                    break;
                }

                Artist artist = controller.findArtistByName(artistName);
                if (artist == null) {
                    controller.createArtist(artistName, "No genre set.");
                    System.out.println("New artist added to the repository: " + artistName);
                    artist = controller.findArtistByName(artistName);
                }
                artists.add(artist);
            }
        } else if ("Sports Event".equalsIgnoreCase(eventType)) {
            while (true) {
                System.out.print("Enter athlete name (or type 'done' to finish): ");
                String athleteName = scanner.nextLine();

                if ("done".equalsIgnoreCase(athleteName)) {
                    break;
                }

                Athlete athlete = controller.findAthleteByName(athleteName);
                if (athlete == null) {
                    controller.createAthlete(athleteName, "No sport set.");
                    System.out.println("New athlete added to the repository: " + athleteName);
                    athlete = controller.findAthleteByName(athleteName);
                }
                athletes.add(athlete);
            }
        } else {
            System.out.println("Invalid event type. Please enter 'Concert' or 'Sports Event'.");
            return;
        }

        if ("Concert".equalsIgnoreCase(eventType)) {
            controller.createConcert(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artists);
        } else if ("Sports Event".equalsIgnoreCase(eventType)) {
            controller.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes);
        }
    }

    // Method to handle viewing all events
    public static void handleViewEvents(Controller controller) {
        System.out.println("=== View Events ===");
        List<Event> events = controller.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }
    }

    // Method to handle updating an event
    public static void handleUpdateEvent(Scanner scanner, Controller controller) {
        System.out.println("=== Update Event ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
            return;
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }

        System.out.print("Enter Event ID to update: ");
        int eventId;
        try {
            eventId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Event ID.");
            return;
        }

        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        // Update name
        System.out.print("Enter new event name (or press Enter to keep current name): ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) {
            newName = event.getEventName(); // Keep current name if input is empty
        }

        // Update description
        System.out.print("Enter new event description (or press Enter to keep current description): ");
        String newDescription = scanner.nextLine().trim();
        if (newDescription.isEmpty()) {
            newDescription = event.getEventDescription(); // Keep current description if input is empty
        }

        // Update start date and time
        LocalDateTime newStartDateTime = event.getStartDateTime(); // Default to current start time
        System.out.print("Enter new start date and time (YYYY-MM-DDTHH:MM) (or press Enter to keep current start time): ");
        String startDateTimeInput = scanner.nextLine().trim();
        if (!startDateTimeInput.isEmpty()) {
            try {
                newStartDateTime = LocalDateTime.parse(startDateTimeInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Start time remains unchanged.");
            }
        }

        // Update end date and time
        LocalDateTime newEndDateTime = event.getEndDateTime(); // Default to current end time
        System.out.print("Enter new end date and time (YYYY-MM-DDTHH:MM) (or press Enter to keep current end time): ");
        String endDateTimeInput = scanner.nextLine().trim();
        if (!endDateTimeInput.isEmpty()) {
            try {
                newEndDateTime = LocalDateTime.parse(endDateTimeInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. End time remains unchanged.");
            }
        }

        // Update event status
        System.out.print("Enter new event status (SCHEDULED, CANCELLED, COMPLETED) (or press Enter to keep current status): ");
        String statusInput = scanner.nextLine().trim().toUpperCase();
        EventStatus newStatus = event.getEventStatus(); // Default to current status
        if (!statusInput.isEmpty()) {
            try {
                newStatus = EventStatus.valueOf(statusInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Status remains unchanged.");
            }
        }

        // Update the event through the controller
        controller.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);
        System.out.println("Event updated successfully.");
    }


    // Method to handle deleting an event
    public static void handleDeleteEvent(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Event ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }

        System.out.print("Enter Event ID to delete: ");
        int eventID = Integer.parseInt(scanner.nextLine());

    }
}