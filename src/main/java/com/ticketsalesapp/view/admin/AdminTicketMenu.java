package main.java.com.ticketsalesapp.view.admin;

import main.java.com.ticketsalesapp.controller.ApplicationController;
import main.java.com.ticketsalesapp.exception.EntityNotFoundException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.Event;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.venue.Venue;

import java.util.List;
import java.util.Scanner;

/**
 * Provides an admin interface for managing tickets through a console menu.
 * Allows the admin to generate tickets and manage them for specific events.
 */
public class AdminTicketMenu {

    /**
     * Displays the Ticket Management menu to the admin and handles menu selection.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param applicationController The {@code Controller} object used to perform operations on tickets and events.
     */
    public static void display(Scanner scanner, ApplicationController applicationController) {
        boolean inTicketMenu = true;
        while (inTicketMenu) {
            try {
                System.out.println("==== Ticket Management ====");
                System.out.println("1. Generate Tickets for Event");
                System.out.println("2. View Tickets for Event");
                System.out.println("3. Delete Ticket by ID");
                System.out.println("4. View Ticket Details by ID");
                System.out.println("0. Back to Admin Menu");
                System.out.println("==========================");

                System.out.print("Choose an option: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        handleGenerateTickets(scanner, applicationController);
                        break;
                    case "2":
                        handleViewTicketsForEvent(scanner, applicationController);
                        break;
                    case "3":
                        handleDeleteTicket(scanner, applicationController);
                        break;
                    case "4":
                        handleViewTicketDetails(scanner, applicationController);
                        break;
                    case "0":
                        inTicketMenu = false;
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
     * Allows the admin to generate tickets for a specific event.
     */
    private static void handleGenerateTickets(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("=== Generate Tickets for Event ===");
            Event event = selectEvent(scanner, applicationController);
            if (event == null) return;
            Venue venue = applicationController.findVenueByID(event.getVenueID());
            if (venue == null) {
                throw new EntityNotFoundException("Venue with ID " + event.getVenueID() + " not found.");
            }
            System.out.print("Enter Base Price for EARLY_BIRD Tickets: ");
            double basePrice = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Number of EARLY_BIRD Tickets to Generate: ");
            int earlyBirdCount = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Number of VIP Tickets to Generate: ");
            int vipCount = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Number of STANDARD Tickets to Generate: ");
            int standardCount = Integer.parseInt(scanner.nextLine());
            try {
                List<Ticket> generatedTickets = applicationController.generateTicketsForEvent(event, basePrice, earlyBirdCount, vipCount, standardCount);
                System.out.println(generatedTickets.size() + " tickets generated successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all tickets for a specific event.
     */
    private static void handleViewTicketsForEvent(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("=== View Tickets for Event ===");
            Event event = selectEvent(scanner, applicationController);
            if (event == null) return;
            List<Ticket> tickets = applicationController.getTicketsByEvent(event);
            if (tickets.isEmpty()) {
                throw new EntityNotFoundException("Ticket not found for this event.");
            } else {
                System.out.println("Tickets for Event: " + event.getEventName());
                tickets.forEach(ticket -> System.out.println(
                        "ID: " + ticket.getID() +
                                ", Type: " + ticket.getTicketType() +
                                ", Price: $" + ticket.getPrice() +
                                ", Sold: " + (ticket.isSold() ? "Yes" : "No")
                ));
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Allows the admin to delete a specific ticket by its ID.
     */
    private static void handleDeleteTicket(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("=== Delete Ticket by ID ===");
            System.out.print("Enter Ticket ID: ");
            int ticketID = Integer.parseInt(scanner.nextLine());
            if (ticketID <= 0)
                throw new ValidationException("Ticket ID must be a positive integer.");
            applicationController.deleteTicket(ticketID);
            System.out.println("Ticket deleted successfully.");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Allows the admin to view the details of a specific ticket by its ID.
     */
    private static void handleViewTicketDetails(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("=== View Ticket Details by ID ===");
            System.out.print("Enter Ticket ID: ");
            int ticketID = Integer.parseInt(scanner.nextLine());
            if (ticketID <= 0)
                throw new ValidationException("Ticket ID must be a positive integer.");
            Ticket ticket = applicationController.findTicketByID(ticketID);
            if (ticket != null) {
                System.out.println("Ticket Details: ");
                System.out.println(ticket);
            } else {
                throw new EntityNotFoundException("Ticket ID " + ticketID + " not found.");
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Helper method to select an event from the available events.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param applicationController The {@code Controller} object used to retrieve event data.
     * @return The selected Event, or null if invalid.
     */
    private static Event selectEvent(Scanner scanner, ApplicationController applicationController) {
        try {
            List<Event> events = applicationController.getAllEvents();
            if (events.isEmpty()) {
                throw new EntityNotFoundException("No events found.");
            }
            System.out.println("Available Events:");
            for (Event event : events) {
                System.out.println("ID: " + event.getID() + ", Name: " + event.getEventName());
            }
            System.out.print("Enter Event ID: ");
            int eventID = Integer.parseInt(scanner.nextLine());
            if (eventID <= 0)
                throw new ValidationException("Event ID must be greater than 0.");
            Event event = applicationController.findEventByID(eventID);
            if (event == null) {
                throw new EntityNotFoundException("No event found with the given ID.");
            }
            return event;
        } catch (EntityNotFoundException | ValidationException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}