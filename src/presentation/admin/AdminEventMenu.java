package presentation.admin;

import controller.Controller;
import model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides a menu for admins to manage events, including creating, viewing, updating, and deleting events.
 */
public class AdminEventMenu {

    /**
     * Displays the event management menu and handles the selected options.
     * @param scanner the scanner to read user input
     * @param controller the controller to handle event management actions
     */
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

    /**
     * Handles the creation of a new event, either a concert or sports event.
     * @param scanner the scanner to read user input
     * @param controller the controller to manage event creation
     */
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
            System.out.println("Venue not found. Please create the venue first.");
            return;
        }
        int venueID = venue.getID();
        EventStatus eventStatus = EventStatus.SCHEDULED;
        if ("Concert".equalsIgnoreCase(eventType)) {
            Concert concert = controller.createConcert(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
            if (concert != null) {
                manageArtistsForConcert(scanner, controller, concert);
            }
        } else if ("Sports Event".equalsIgnoreCase(eventType)) {
            SportsEvent sportsEvent = controller.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
            if (sportsEvent != null) {
                manageAthletesForSportsEvent(scanner, controller, sportsEvent);
            }
        } else {
            System.out.println("Invalid event type. Please enter 'Concert' or 'Sports Event'.");
        }
    }

    private static void manageArtistsForConcert(Scanner scanner, Controller controller, Concert concert) {
        System.out.println("Manage Artists for Concert:");
        while (true) {
            System.out.println("1. Add Artist");
            System.out.println("2. Remove Artist");
            System.out.println("3. View Artists");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Enter Artist Name: ");
                    String artistName = scanner.nextLine();
                    Artist artist = controller.findArtistByName(artistName);
                    if (artist == null) {
                        System.out.println("Artist not found. Creating new artist.");
                        controller.createArtist(artistName, "No genre set.");
                        artist = controller.findArtistByName(artistName);
                    }
                    controller.addArtistToConcert(concert.getID(), artist.getID());
                    System.out.println("Artist added to the concert.");
                    break;
                case "2":
                    List<Artist> artists = controller.getArtistsByConcert(concert.getID());
                    if (artists.isEmpty()) {
                        System.out.println("No artists to remove.");
                        break;
                    }
                    System.out.println("Artists in this concert:");
                    for (Artist a : artists) {
                        System.out.println("- " + a.getArtistName());
                    }
                    System.out.print("Enter Artist Name to remove: ");
                    String removeArtistName = scanner.nextLine();
                    Artist artistToRemove = controller.findArtistByName(removeArtistName);
                    if (artistToRemove != null) {
                        controller.removeArtistFromConcert(concert.getID(), artistToRemove.getID());
                        System.out.println("Artist removed from the concert.");
                    } else {
                        System.out.println("Artist not found in the concert.");
                    }
                    break;
                case "3":
                    List<Artist> currentArtists = controller.getArtistsByConcert(concert.getID());
                    if (currentArtists.isEmpty()) {
                        System.out.println("No artists associated with this concert.");
                    } else {
                        System.out.println("Artists in this concert:");
                        for (Artist a : currentArtists) {
                            System.out.println("- " + a.getArtistName());
                        }
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageAthletesForSportsEvent(Scanner scanner, Controller controller, SportsEvent sportsEvent) {
        System.out.println("Manage Athletes for Sports Event:");
        while (true) {
            System.out.println("1. Add Athlete");
            System.out.println("2. Remove Athlete");
            System.out.println("3. View Athletes");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Enter Athlete Name: ");
                    String athleteName = scanner.nextLine();
                    Athlete athlete = controller.findAthleteByName(athleteName);
                    if (athlete == null) {
                        System.out.println("Athlete not found. Creating new athlete.");
                        controller.createAthlete(athleteName, "No sport set.");
                        athlete = controller.findAthleteByName(athleteName);
                    }
                    controller.addAthleteToSportsEvent(sportsEvent.getID(), athlete.getID());
                    System.out.println("Athlete added to the sports event.");
                    break;
                case "2":
                    List<Athlete> athletes = controller.getAthletesBySportsEvent(sportsEvent.getID());
                    if (athletes.isEmpty()) {
                        System.out.println("No athletes to remove.");
                        break;
                    }
                    System.out.println("Athletes in this sports event:");
                    for (Athlete a : athletes) {
                        System.out.println("- " + a.getName());
                    }
                    System.out.print("Enter Athlete Name to remove: ");
                    String removeAthleteName = scanner.nextLine();
                    Athlete athleteToRemove = controller.findAthleteByName(removeAthleteName);
                    if (athleteToRemove != null) {
                        controller.removeAthleteFromSportsEvent(sportsEvent.getID(), athleteToRemove.getID());
                        System.out.println("Athlete removed from the sports event.");
                    } else {
                        System.out.println("Athlete not found in the sports event.");
                    }
                    break;
                case "3":
                    List<Athlete> currentAthletes = controller.getAthletesBySportsEvent(sportsEvent.getID());
                    if (currentAthletes.isEmpty()) {
                        System.out.println("No athletes associated with this sports event.");
                    } else {
                        System.out.println("Athletes in this sports event:");
                        for (Athlete a : currentAthletes) {
                            System.out.println("- " + a.getName());
                        }
                    }
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Displays a list of all events.
     * @param controller the controller to retrieve events from
     */
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

    /**
     * Handles updating an existing event with new information.
     * @param scanner the scanner to read user input
     * @param controller the controller to manage event updates
     */
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
        System.out.print("Enter new event name (or press Enter to keep current name): ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) {
            newName = event.getEventName();
        }
        System.out.print("Enter new event description (or press Enter to keep current description): ");
        String newDescription = scanner.nextLine().trim();
        if (newDescription.isEmpty()) {
            newDescription = event.getEventDescription();
        }
        LocalDateTime newStartDateTime = event.getStartDateTime();
        System.out.print("Enter new start date and time (YYYY-MM-DDTHH:MM) (or press Enter to keep current start time): ");
        String startDateTimeInput = scanner.nextLine().trim();
        if (!startDateTimeInput.isEmpty()) {
            try {
                newStartDateTime = LocalDateTime.parse(startDateTimeInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Start time remains unchanged.");
            }
        }
        LocalDateTime newEndDateTime = event.getEndDateTime();
        System.out.print("Enter new end date and time (YYYY-MM-DDTHH:MM) (or press Enter to keep current end time): ");
        String endDateTimeInput = scanner.nextLine().trim();
        if (!endDateTimeInput.isEmpty()) {
            try {
                newEndDateTime = LocalDateTime.parse(endDateTimeInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. End time remains unchanged.");
            }
        }
        System.out.print("Enter new event status (SCHEDULED, CANCELLED, COMPLETED) (or press Enter to keep current status): ");
        String statusInput = scanner.nextLine().trim().toUpperCase();
        EventStatus newStatus = event.getEventStatus();
        if (!statusInput.isEmpty()) {
            try {
                newStatus = EventStatus.valueOf(statusInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Status remains unchanged.");
            }
        }
        if (event instanceof Concert) {
            manageArtistsForConcert(scanner, controller, (Concert) event);
        } else if (event instanceof SportsEvent) {
            manageAthletesForSportsEvent(scanner, controller, (SportsEvent) event);
        }
        controller.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);
        System.out.println("Event updated successfully.");
    }

    /**
     * Handles deleting an event by its ID.
     * @param scanner the scanner to read user input
     * @param controller the controller to manage event deletion
     */
    public static void handleDeleteEvent(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Event ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
            return;
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }

        System.out.print("Enter Event ID to delete: ");
        int eventID;
        try {
            eventID = Integer.parseInt(scanner.nextLine());
            controller.deleteEvent(eventID);
            System.out.println("Event deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Event ID. Please enter a valid number.");
        }
    }
}
