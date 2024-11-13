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
     * Handles generating tickets for a specific event.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to generate tickets for the event.
     */
    private static void handleGenerateTickets(Scanner scanner, Controller controller) {
        System.out.println("=== Generate Tickets for Event ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
            return;
        } else {
            for (Event event : events) {
                System.out.println(event);
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

    /**
     * Handles viewing available tickets for a specific event.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to retrieve available tickets for the event.
     */
    private static void handleViewAvailableTickets(Scanner scanner, Controller controller) {
        System.out.println("=== View Available Tickets for Event ===");

        List<Event> events = controller.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }

        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());

        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        controller.getAvailableTicketsForEvent(event);
    }

    /**
     * Handles reserving a ticket for a specific event.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to reserve a ticket.
     */
    private static void handleReserveTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Reserve Ticket ===");
        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }
        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());
        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        if (event != null) {
            List<Ticket> tickets = controller.getTicketsByEvent(eventId);
            if (tickets.isEmpty()) {
                System.out.println("No tickets available for this event.");
            } else {
                System.out.println("Tickets for event " + event.getEventName() + ":");
                tickets.forEach(System.out::println);
                System.out.print("Enter Ticket ID: ");
                int ticketId = Integer.parseInt(scanner.nextLine());
                Ticket ticket = controller.getTicketByID(ticketId);
                if (ticket == null) {
                    System.out.println("Ticket not found.");
                    return;
                }
                System.out.print("Enter Purchaser Name: ");
                String purchaserName = scanner.nextLine();
                controller.reserveTicket(ticket, purchaserName);
            }
        }
    }

    /**
     * Handles releasing a reserved ticket.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to release a ticket.
     */
    private static void handleReleaseTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Release Ticket ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }
        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());
        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        if (event != null) {
            List<Ticket> tickets = controller.getTicketsByEvent(eventId);
            if (tickets.isEmpty()) {
                System.out.println("No tickets available for this event.");
            } else {
                System.out.println("Tickets for event " + event.getEventName() + ":");
                tickets.forEach(System.out::println);

                System.out.print("Enter Ticket ID: ");
                int ticketId = Integer.parseInt(scanner.nextLine());

                Ticket ticket = controller.getTicketByID(ticketId);
                if (ticket == null) {
                    System.out.println("Ticket not found.");
                    return;
                }

                controller.releaseTicket(ticket);
            }
        }
    }

    /**
     * Handles viewing a ticket's details by its ID.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to view ticket details.
     */
    private static void handleViewTicketByID(Scanner scanner, Controller controller) {
        System.out.println("=== View Ticket by ID ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }
        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());
        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        if (event != null) {
            List<Ticket> tickets = controller.getTicketsByEvent(eventId);
            if (tickets.isEmpty()) {
                System.out.println("No tickets available for this event.");
            } else {
                System.out.println("Tickets for event " + event.getEventName() + ":");
                tickets.forEach(System.out::println);

                System.out.print("Enter Ticket ID: ");
                int ticketId = Integer.parseInt(scanner.nextLine());

                Ticket ticket = controller.getTicketByID(ticketId);
                if (ticket != null) {
                    System.out.println("Ticket found: " + ticket);
                } else {
                    System.out.println("No ticket found with ID: " + ticketId);
                }
            }
        }
    }

    /**
     * Handles deleting a ticket by its ID.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to delete a ticket.
     */
    private static void handleDeleteTicket(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Ticket by ID ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }
        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());
        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        if (event != null) {
            List<Ticket> tickets = controller.getTicketsByEvent(eventId);
            if (tickets.isEmpty()) {
                System.out.println("No tickets available for this event.");
            } else {
                System.out.println("Tickets for event " + event.getEventName() + ":");
                tickets.forEach(System.out::println);

                System.out.print("Enter Ticket ID: ");
                int ticketId = Integer.parseInt(scanner.nextLine());

                controller.deleteTicket(ticketId);
            }
        }
    }

    /**
     * Handles calculating the total price of selected tickets.
     *
     * @param scanner    The {@code Scanner} object used to read user input.
     * @param controller The {@code Controller} object used to calculate total price of tickets.
     */
    private static void handleCalculateTotalPrice(Scanner scanner, Controller controller) {
        System.out.println("=== Calculate Total Price of Selected Tickets ===");
        List<Ticket> ticketsToBeCalculated = new ArrayList<>();

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }
        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());
        Event event = controller.findEventByID(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        if (event != null) {
            List<Ticket> tickets = controller.getTicketsByEvent(eventId);
            if (tickets.isEmpty()) {
                System.out.println("No tickets available for this event.");
            } else {
                System.out.println("Tickets for event " + event.getEventName() + ":");
                tickets.forEach(System.out::println);

                while (true) {
                    System.out.print("Enter Ticket ID (or type 'done' to finish): ");
                    String input = scanner.nextLine();

                    if ("done".equalsIgnoreCase(input)) {
                        break;
                    }

                    int ticketId = Integer.parseInt(input);
                    Ticket ticketToBeCalculated = controller.getTicketByID(ticketId);
                    if (ticketToBeCalculated != null) {
                        ticketsToBeCalculated.add(ticketToBeCalculated);
                    } else {
                        System.out.println("Ticket with ID " + ticketId + " not found.");
                    }
                }

                controller.calculateTotalPrice(ticketsToBeCalculated);
            }
        }
    }
}
