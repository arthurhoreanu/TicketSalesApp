package presentation.admin;

import controller.Controller;
import model.*;
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
     * @param controller The {@code Controller} object used to perform operations on tickets and events.
     */
    public static void display(Scanner scanner, Controller controller) {
        boolean inTicketMenu = true;

        while (inTicketMenu) {
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
                    handleGenerateTickets(scanner, controller);
                    break;
                case "2":
                    handleViewTicketsForEvent(scanner, controller);
                    break;
                case "3":
                    handleDeleteTicket(scanner, controller);
                    break;
                case "4":
                    handleViewTicketDetails(scanner, controller);
                    break;
                case "0":
                    inTicketMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }

    /**
     * Allows the admin to generate tickets for a specific event.
     */
    private static void handleGenerateTickets(Scanner scanner, Controller controller) {
        System.out.println("=== Generate Tickets for Event ===");

        Event event = selectEvent(scanner, controller);
        if (event == null) return;

        System.out.print("Enter Base Price for EARLY_BIRD Tickets: ");
        double basePrice = Double.parseDouble(scanner.nextLine());

        List<Ticket> generatedTickets = controller.generateTicketsForEvent(event, basePrice);
        System.out.println(generatedTickets.size() + " tickets generated successfully.");
    }

    /**
     * Displays all tickets for a specific event.
     */
    private static void handleViewTicketsForEvent(Scanner scanner, Controller controller) {
        System.out.println("=== View Tickets for Event ===");

        Event event = selectEvent(scanner, controller);
        if (event == null) return;

        List<Ticket> tickets = controller.getTicketsByEvent(event);
        if (tickets.isEmpty()) {
            System.out.println("No tickets available for this event.");
        } else {
            System.out.println("Tickets for Event: " + event.getEventName());
            tickets.forEach(ticket -> System.out.println(
                    "ID: " + ticket.getID() +
                            ", Type: " + ticket.getTicketType() +
                            ", Price: $" + ticket.getPrice() +
                            ", Sold: " + (ticket.isSold() ? "Yes" : "No")
            ));
        }
    }

    /**
     * Allows the admin to delete a specific ticket by its ID.
     */
    private static void handleDeleteTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Ticket by ID ===");

        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        controller.deleteTicket(ticketId);
        System.out.println("Ticket deleted successfully.");
    }

    /**
     * Allows the admin to view the details of a specific ticket by its ID.
     */
    private static void handleViewTicketDetails(Scanner scanner, Controller controller) {
        System.out.println("=== View Ticket Details by ID ===");

        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        Ticket ticket = controller.findTicketByID(ticketId);
        if (ticket != null) {
            System.out.println("Ticket Details: ");
            System.out.println(ticket);
        } else {
            System.out.println("No ticket found with the given ID.");
        }
    }

    /**
     * Helper method to select an event from the available events.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to retrieve event data.
     * @return The selected Event, or null if invalid.
     */
    private static Event selectEvent(Scanner scanner, Controller controller) {
        List<Event> events = controller.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events available.");
            return null;
        }

        System.out.println("Available Events:");
        for (Event event : events) {
            System.out.println("ID: " + event.getID() + ", Name: " + event.getEventName());
        }

        System.out.print("Enter Event ID: ");
        int eventId;
        try {
            eventId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return null;
        }

        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
        }
        return event;
    }
}