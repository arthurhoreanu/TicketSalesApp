package presentation.admin;

import controller.Controller;
import model.Event;
import model.Ticket;
import model.Venue;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminTicketMenu {
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
                    handleViewTicketById(scanner, controller);
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

    private static void handleGenerateTickets(Scanner scanner, Controller controller) {
        System.out.println("=== Generate Tickets for Event ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
            return;
        } else {
            for (Event event : events) {
                System.out.println(events);
            }
        }

        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());

        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        System.out.print("Enter Standard Ticket Price: ");
        double standardPrice = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter VIP Ticket Price: ");
        double vipPrice = Double.parseDouble(scanner.nextLine());

        controller.generateTicketsForEvent(event, standardPrice, vipPrice);
    }

    private static void handleViewAvailableTickets(Scanner scanner, Controller controller) {
        System.out.println("=== View Available Tickets for Event ===");
        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());

        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        controller.getAvailableTicketsForEvent(event);
    }

    private static void handleReserveTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Reserve Ticket ===");
        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        Ticket ticket = controller.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        System.out.print("Enter Purchaser Name: ");
        String purchaserName = scanner.nextLine();

        controller.reserveTicket(ticket, purchaserName);
    }

    private static void handleReleaseTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Release Ticket ===");
        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        Ticket ticket = controller.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        controller.releaseTicket(ticket);
    }

    private static void handleViewTicketById(Scanner scanner, Controller controller) {
        System.out.println("=== View Ticket by ID ===");
        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        Ticket ticket = controller.getTicketById(ticketId); 
        if (ticket != null) {
            System.out.println("Ticket found: " + ticket);
        } else {
            System.out.println("No ticket found with ID: " + ticketId);
        }
    }

    private static void handleDeleteTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Ticket by ID ===");
        System.out.print("Enter Ticket ID: ");
        int ticketId = Integer.parseInt(scanner.nextLine());

        controller.deleteTicket(ticketId);
    }

    private static void handleCalculateTotalPrice(Scanner scanner, Controller controller) {
        System.out.println("=== Calculate Total Price of Selected Tickets ===");
        List<Ticket> tickets = new ArrayList<>();

        while (true) {
            System.out.print("Enter Ticket ID (or type 'done' to finish): ");
            String input = scanner.nextLine();

            if ("done".equalsIgnoreCase(input)) {
                break;
            }

            int ticketId = Integer.parseInt(input);
            Ticket ticket = controller.getTicketById(ticketId);
            if (ticket != null) {
                tickets.add(ticket);
            } else {
                System.out.println("Ticket with ID " + ticketId + " not found.");
            }
        }

        controller.calculateTotalPrice(tickets);
    }
}
