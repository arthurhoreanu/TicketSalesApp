package main.java.com.ticketsalesapp.view.admin;

import main.java.com.ticketsalesapp.controller.ApplicationController;
import main.java.com.ticketsalesapp.exception.EntityNotFoundException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.*;
import main.java.com.ticketsalesapp.model.venue.Venue;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Provides a menu for admins to manage events, including creating, viewing, updating, and deleting events.
 */
public class AdminEventMenu {

    /**
     * Displays the event management menu and handles the selected options.
     * @param scanner the scanner to read user input
     * @param applicationController the controller to handle event management actions
     */
    public static void display(Scanner scanner, ApplicationController applicationController) {
        boolean inEventMenu = true;
        while (inEventMenu) {
            try {
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
                        handleCreateEvent(scanner, applicationController);
                        break;
                    case "2":
                        handleViewEvents(applicationController);
                        break;
                    case "3":
                        handleUpdateEvent(scanner, applicationController);
                        break;
                    case "4":
                        handleDeleteEvent(scanner, applicationController);
                        break;
                    case "0":
                        inEventMenu = false;
                        break;
                    default:
                        throw new ValidationException("Invalid option. Please select a valid number between 0 and 4.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    /**
     * Handles the creation of a new event, either a concert or sports event.
     * @param scanner the scanner to read user input
     * @param applicationController the controller to manage event creation
     */
    public static void handleCreateEvent(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("=== Create Event ===");
            System.out.print("Enter Event Type (Concert/Sports Event): ");
            String eventType = scanner.nextLine().trim();
            if (!"Concert".equalsIgnoreCase(eventType) && !"Sports Event".equalsIgnoreCase(eventType)) {
                throw new ValidationException("Invalid event type. Please enter 'Concert' or 'Sports Event'.");
            }
            System.out.print("Enter event name: ");
            String eventName = scanner.nextLine().trim();
            if (eventName.isEmpty()) {
                throw new ValidationException("Event name cannot be empty.");
            }
            System.out.print("Enter event description: ");
            String eventDescription = scanner.nextLine().trim();
            if (eventDescription.isEmpty()) {
                throw new ValidationException("Event description cannot be empty.");
            }
            System.out.print("Enter start date and time (YYYY-MM-DDTHH:MM): ");
            LocalDateTime startDateTime = LocalDateTime.parse(scanner.nextLine());
            if (startDateTime.isBefore(LocalDateTime.now())) {
                throw new ValidationException("Start date cannot be before end date.");
            }
            System.out.print("Enter end date and time (YYYY-MM-DDTHH:MM): ");
            LocalDateTime endDateTime = LocalDateTime.parse(scanner.nextLine());
            if (endDateTime.isBefore(LocalDateTime.now())) {
                throw new ValidationException("End date cannot be before start date.");
            }
            System.out.print("Enter venue name: ");
            String venueName = scanner.nextLine().trim();
            Venue venue = applicationController.findVenueByName(venueName);
            if (venue == null) {
                throw new EntityNotFoundException("Venue with name " + venueName + " not found.");
            }
            if ("Concert".equalsIgnoreCase(eventType)) {
                Concert concert = applicationController.createConcert(eventName, eventDescription, startDateTime, endDateTime, venue.getID(), EventStatus.SCHEDULED);
                manageArtistsForConcert(scanner, applicationController, concert);
            } else {
                SportsEvent sportsEvent = applicationController.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venue.getID(), EventStatus.SCHEDULED);
                manageAthletesForSportsEvent(scanner, applicationController, sportsEvent);
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void manageArtistsForConcert(Scanner scanner, ApplicationController applicationController, Concert concert) {
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
                    try {
                        System.out.print("Enter Artist Name: ");
                        String artistName = scanner.nextLine();
                        if(artistName.isEmpty()) {
                            throw new ValidationException("Artist name cannot be empty.");
                        }
                        Artist artist = applicationController.findArtistByName(artistName);
                        if (artist == null) {
                            System.out.println("Artist not found. Creating new artist.");
                            applicationController.createArtist(artistName, "No genre set.");
                            artist = applicationController.findArtistByName(artistName);
                        }
                        if (artist != null) {
                            boolean added = applicationController.addArtistToConcert(concert.getID(), artist.getID());
                            if (added) {
                                System.out.println("Artist added to the concert.");
                            } else {
                                System.out.println("Failed to add artist to the concert.");
                            }
                        } else {
                            System.out.println("Failed to create or retrieve artist.");
                        }
                        break;
                    } catch (ValidationException e) {
                        System.out.println(e.getMessage());
                    }

                case "2":
                    try {
                        List<Artist> artists = applicationController.getArtistsByConcert(concert.getID());
                        if (artists.isEmpty()) {
                            throw new EntityNotFoundException("No artist found for concert.");
                        }
                        System.out.println("Artists in this concert:");
                        for (Artist a : artists) {
                            System.out.println("- " + a.getArtistName());
                        }
                        System.out.print("Enter Artist Name to remove: ");
                        String removeArtistName = scanner.nextLine();
                        Artist artistToRemove = applicationController.findArtistByName(removeArtistName);
                        if (artistToRemove != null) {
                            applicationController.removeArtistFromConcert(concert.getID(), artistToRemove.getID());
                            System.out.println("Artist removed from the concert.");
                        } else {
                            throw new EntityNotFoundException("Artist not found in the concert.");
                        }
                    } catch (ValidationException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case "3":
                    try {
                        List<Artist> currentArtists = applicationController.getArtistsByConcert(concert.getID());
                        if (currentArtists.isEmpty()) {
                            throw new EntityNotFoundException("No artists associated with this concert.");
                        } else {
                            System.out.println("Artists in this concert:");
                            for (Artist a : currentArtists) {
                                System.out.println("- " + a.getArtistName());
                            }
                        }
                    } catch (ValidationException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageAthletesForSportsEvent(Scanner scanner, ApplicationController applicationController, SportsEvent sportsEvent) {
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
                    Athlete athlete = applicationController.findAthleteByName(athleteName);
                    if (athlete == null) {
                        System.out.println("Athlete not found. Creating new athlete.");
                        applicationController.createAthlete(athleteName, "No sport set.");
                        athlete = applicationController.findAthleteByName(athleteName);
                    }
                    if (athlete != null) {
                        boolean added = applicationController.addAthleteToSportsEvent(sportsEvent.getID(), athlete.getID());
                        if (added) {
                            System.out.println("Athlete added to the sports event.");
                        } else {
                            System.out.println("Failed to add athlete to the sports event.");
                        }
                    } else {
                        System.out.println("Failed to create or retrieve athlete.");
                    }
                    break;
                case "2":
                    try {
                        List<Athlete> athletes = applicationController.getAthletesBySportsEvent(sportsEvent.getID());
                        if (athletes.isEmpty()) {
                            throw new EntityNotFoundException("No athletes associated with this sports event.");
                        }
                        System.out.println("Athletes in this sports event:");
                        for (Athlete a : athletes) {
                            System.out.println("- " + a.getName());
                        }
                        System.out.print("Enter Athlete Name to remove: ");
                        String removeAthleteName = scanner.nextLine();
                        Athlete athleteToRemove = applicationController.findAthleteByName(removeAthleteName);
                        if (athleteToRemove != null) {
                            applicationController.removeAthleteFromSportsEvent(sportsEvent.getID(), athleteToRemove.getID());
                            System.out.println("Athlete removed from the sports event.");
                        } else {
                            System.out.println("Athlete not found in the sports event.");
                        }
                    } catch (ValidationException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "3":
                    try {
                        List<Athlete> currentAthletes = applicationController.getAthletesBySportsEvent(sportsEvent.getID());
                        if (currentAthletes.isEmpty()) {
                            throw new EntityNotFoundException("No athletes associated with this sports event.");
                        } else {
                            System.out.println("Athletes in this sports event:");
                            for (Athlete a : currentAthletes) {
                                System.out.println("- " + a.getName());
                            }
                        }
                    } catch (ValidationException e) {
                        System.out.println("Error: " + e.getMessage());
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
     * @param applicationController the controller to retrieve events from
     */
    public static void handleViewEvents(ApplicationController applicationController) {
        System.out.println("=== View Events ===");
        applicationController.getAllEvents();
    }

    /**
     * Handles updating an existing event with new information.
     * @param scanner the scanner to read user input
     * @param applicationController the controller to manage event updates
     */
    public static void handleUpdateEvent(Scanner scanner, ApplicationController applicationController) {
        System.out.println("=== Update Event ===");
        List<Event> events = applicationController.getAllEvents();
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
        Event event = applicationController.findEventByID(eventId);
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
            manageArtistsForConcert(scanner, applicationController, (Concert) event);
        } else if (event instanceof SportsEvent) {
            manageAthletesForSportsEvent(scanner, applicationController, (SportsEvent) event);
        }
        applicationController.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);
        System.out.println("Event updated successfully.");
    }

    /**
     * Handles deleting an event by its ID.
     * @param scanner the scanner to read user input
     * @param applicationController the controller to manage event deletion
     */
    public static void handleDeleteEvent(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("=== Delete Event ===");
            List<Event> events = applicationController.getAllEvents();
            if (events.isEmpty()) {
                throw new EntityNotFoundException("Events not found.");
            } else {
                for (Event event : events) {
                    System.out.println(event);
                }
            }
            System.out.print("Enter Event ID to delete: ");
            int eventID;
            try {
                eventID = Integer.parseInt(scanner.nextLine());
                applicationController.deleteEvent(eventID);
                System.out.println("Event deleted successfully.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid Event ID. Please enter a valid number.");
            }
        } catch (NumberFormatException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}