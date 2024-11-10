package presentation.actions;

import controller.Controller;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class EventAction {
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
            controller.addVenue(venueName, "No location set.", 0, null);
            System.out.println("New venue added to the repository: " + venueName);
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

            System.out.print("Enter genre: ");
            genre = scanner.nextLine();
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
            System.out.print("Enter sport name: ");
            sportName = scanner.nextLine();
        } else {
            System.out.println("Invalid event type. Please enter 'Concert' or 'Sports Event'.");
            return;
        }

        if ("Concert".equalsIgnoreCase(eventType)) {
            controller.createConcert(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artists, genre);
            System.out.println("Concert created successfully.");
        } else if ("Sports Event".equalsIgnoreCase(eventType)) {
            controller.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes, sportName);
            System.out.println("Sports Event created successfully.");
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
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }

        System.out.print("Enter Event ID to update: ");
        int eventId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new event name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new event description: ");
        String newDescription = scanner.nextLine();
        System.out.print("Enter new start date and time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime newStartDateTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter new end date and time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime newEndDateTime = LocalDateTime.parse(scanner.nextLine());

        // Prompting for new event status
        System.out.print("Enter new event status (SCHEDULED, CANCELLED, COMPLETED): ");
        String statusInput = scanner.nextLine().toUpperCase();
        EventStatus newStatus;

        try {
            newStatus = EventStatus.valueOf(statusInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter one of SCHEDULED, CANCELLED, or COMPLETED.");
            return;
        }

        controller.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);
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
