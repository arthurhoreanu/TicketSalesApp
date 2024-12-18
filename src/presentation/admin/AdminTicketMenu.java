package presentation.admin;

import controller.Controller;
import model.Event;
import model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides an admin interface for managing tickets through a console menu.
 * Allows the admin to generate tickets, view available tickets, reserve or release tickets,
 * view ticket details by ID, delete tickets, and calculate the total price of selected tickets.
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
            System.out.println("2. View Available Tickets for Event");
            System.out.println("3. Reserve Ticket");
            System.out.println("4. Release Ticket");
            System.out.println("5. View Ticket by ID");
            System.out.println("6. Delete Ticket by ID");
            System.out.println("7. Calculate Total Price of Selected Tickets");
            System.out.println("0. Back to Admin Menu");
            System.out.println("==========================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleGenerateTickets(scanner, controller);
                    break;
                case "2":
                    handleViewAvailableTickets(scanner, controller);
                    break;
                case "3":
                    handleReserveTicket(scanner, controller);
                    break;
                case "4":
                    handleReleaseTicket(scanner, controller);
                    break;
                case "5":
                    handleViewTicketByID(scanner, controller);
                    break;
                case "6":
                    handleDeleteTicket(scanner, controller);
                    break;
                case "7":
                    handleCalculateTotalPrice(scanner, controller);
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
     * Displays all events and allows the admin to select one.
     *
     * @param scanner    The {@code Scanner} for user input.
     * @param controller The {@code Controller} to access event data.
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
            System.out.println(event);
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

    /**
     * Handles generating tickets for a specific event.
     */
    private static void handleGenerateTickets(Scanner scanner, Controller controller) {
        System.out.println("=== Generate Tickets for Event ===");

        Event event = selectEvent(scanner, controller);
        if (event == null) return;

        System.out.print("Enter Standard Ticket Price: ");
        double standardPrice = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter VIP Ticket Price: ");
        double vipPrice = Double.parseDouble(scanner.nextLine());

        controller.generateTicketsForEvent(event, standardPrice, vipPrice);
        System.out.println("Tickets generated successfully.");
    }

    /**
     * Handles viewing available tickets for a specific event.
     */
    private static void handleViewAvailableTickets(Scanner scanner, Controller controller) {
        System.out.println("=== View Available Tickets for Event ===");

        Event event = selectEvent(scanner, controller);
        if (event == null) return;

        List<Ticket> availableTickets = controller.getAvailableTicketsForEvent(event);
        if (availableTickets.isEmpty()) {
            System.out.println("No available tickets for this event.");
        } else {
            System.out.println("Available Tickets:");
            availableTickets.forEach(System.out::println);
        }
    }

    /**
     * Handles reserving a ticket for an event.
     */
    private static void handleReserveTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Reserve Ticket ===");

        Event event = selectEvent(scanner, controller);
        if (event == null) return;

        List<Ticket> tickets = controller.getAvailableTicketsForEvent(event);
        if (tickets.isEmpty()) {
            System.out.println("No tickets available for this event.");
            return;
        }

        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());
        Ticket ticket = controller.findTicketByID(ticketId);

        if (ticket != null) {
            System.out.print("Enter Purchaser Name: ");
            String purchaserName = scanner.nextLine();
            controller.reserveTicket(ticket, purchaserName);
        } else {
            System.out.println("Ticket not found.");
        }
    }

    /**
     * Handles releasing a reserved ticket.
     */
    private static void handleReleaseTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Release Ticket ===");

        Event event = selectEvent(scanner, controller);
        if (event == null) return;

        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        Ticket ticket = controller.findTicketByID(ticketId);
        if (ticket != null) {
            controller.releaseTicket(ticket);
        } else {
            System.out.println("Ticket not found.");
        }
    }

    /**
     * Handles viewing a ticket's details by ID.
     */
    private static void handleViewTicketByID(Scanner scanner, Controller controller) {
        System.out.println("=== View Ticket by ID ===");

        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        Ticket ticket = controller.findTicketByID(ticketId);
        if (ticket != null) {
            System.out.println("Ticket Details: " + ticket);
        } else {
            System.out.println("No ticket found with the given ID.");
        }
    }

    /**
     * Handles deleting a ticket by its ID.
     */
    private static void handleDeleteTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Ticket by ID ===");

        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        controller.deleteTicket(ticketId);
        System.out.println("Ticket deleted successfully.");
    }

    /**
     * Handles calculating the total price of selected tickets.
     */
    private static void handleCalculateTotalPrice(Scanner scanner, Controller controller) {
        System.out.println("=== Calculate Total Price of Selected Tickets ===");
        List<Ticket> selectedTickets = new ArrayList<>();

        while (true) {
            System.out.print("Enter Ticket ID (or type 'done' to finish): ");
            String input = scanner.nextLine();

            if ("done".equalsIgnoreCase(input)) break;

            try {
                int ticketId = Integer.parseInt(input);
                Ticket ticket = controller.findTicketByID(ticketId);

                if (ticket != null) {
                    selectedTickets.add(ticket);
                } else {
                    System.out.println("Ticket not found. Please enter a valid ID.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid ticket ID.");
            }
        }

        double totalPrice = controller.calculateTotalPrice(selectedTickets);
        System.out.println("Total Price: $" + totalPrice);
    }
}