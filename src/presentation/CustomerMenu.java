package presentation;

import controller.Controller;
import model.*;

import java.util.*;

public class CustomerMenu {

    public static boolean display(Scanner scanner, Controller controller) {
        while (true) {
            System.out.println("==== Customer Menu ====");
            System.out.println("1. Logout");
            System.out.println("2. Search for Artists/Athletes");
            System.out.println("3. Search Events by Location/Venue");
            System.out.println("4. View Suggested Events");
            System.out.println("5. View All Events");
            System.out.println("6. View Orders History");
            System.out.println("7. Manage Favourites");
            System.out.println("0. Exit");
            System.out.println("=======================");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    controller.logout();
                    System.out.println("Logged out successfully.");
                    return true;
                case "2":
                    handleSearchArtistsAndAthletes(scanner, controller);
                    break;
                case "3":
                    handleSearchEventsByLocation(scanner, controller);
                    break;
                case "4":
                    handleViewSuggestedEvents(scanner, controller);
                    break;
                case "5":
                    handleViewAllEvents(scanner, controller);
                    break;
                case "6":
                    handleViewPreviousOrders(controller);
                    break;
                case "7":
                    handleManageFavourites(scanner, controller);
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return false;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleSearchArtistsAndAthletes(Scanner scanner, Controller controller) {
        System.out.print("Enter the name of an artist or athlete: ");
        String name = scanner.nextLine();

        Artist artist = controller.findArtistByName(name);
        if (artist != null) {
            handleEventSelectionForPerformer(scanner, controller, artist);
            return;
        }

        Athlete athlete = controller.findAthleteByName(name);
        if (athlete != null) {
            handleEventSelectionForPerformer(scanner, controller, athlete);
        } else {
            System.out.println("No artist or athlete found with the name: " + name);
        }
    }


    private static void handleSearchEventsByLocation(Scanner scanner, Controller controller) {
        System.out.print("Enter location or venue name: ");
        String location = scanner.nextLine();
        List<Event> events = controller.getEventsByLocation(location);
        handleEventSelectionFromList(scanner, controller, events);
    }

    private static void handleViewSuggestedEvents(Scanner scanner, Controller controller) {
        Set<FavouriteEntity> favourites = controller.getFavourites();
        if (favourites.isEmpty()) {
            System.out.println("No favourites to generate suggestions from.");
            return;
        }

        List<Event> suggestedEvents = new ArrayList<>();
        for (FavouriteEntity favourite : favourites) {
            if (favourite instanceof Artist artist) {
                suggestedEvents.addAll(controller.getUpcomingEventsForArtist(artist.getID()));
            } else if (favourite instanceof Athlete athlete) {
                suggestedEvents.addAll(controller.getUpcomingEventsForAthlete(athlete.getID()));
            }
        }

        handleEventSelectionFromList(scanner, controller, suggestedEvents);
    }

    private static void handleViewAllEvents(Scanner scanner, Controller controller) {
        List<Event> events = controller.getAllEvents(); // This will handle printing
        if (events.isEmpty()) {
            return; // No further action needed if no events are found
        }
        System.out.print("Enter Event ID to view sections and tickets: ");
        try {
            int eventId = Integer.parseInt(scanner.nextLine());
            Event event = controller.findEventByID(eventId);
            if (event != null) {
                handleSectionAndTicketSelection(scanner, controller, event);
            } else {
                System.out.println("Invalid Event ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void handleEventSelectionForPerformer(Scanner scanner, Controller controller, Object performer) {
        List<Event> events;

        if (performer instanceof Artist artist) {
            events = controller.getUpcomingEventsForArtist(artist.getID()); // Controller handles printing
        } else if (performer instanceof Athlete athlete) {
            events = controller.getUpcomingEventsForAthlete(athlete.getID()); // Controller handles printing
        } else {
            return;
        }

        // If no events are returned, stop further processing
        if (events.isEmpty()) {
            return;
        }

        // Prompt the user for an Event ID
        System.out.print("Enter Event ID to view sections and tickets: ");
        try {
            int eventId = Integer.parseInt(scanner.nextLine());
            Event event = controller.findEventByID(eventId);
            if (event != null) {
                handleSectionAndTicketSelection(scanner, controller, event);
            } else {
                System.out.println("Invalid Event ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }


    private static void handleEventSelectionFromList(Scanner scanner, Controller controller, List<Event> events) {
        if (events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }

        events.forEach(System.out::println);
        System.out.print("Enter Event ID to view sections and tickets: ");
        try {
            int eventId = Integer.parseInt(scanner.nextLine());
            Event event = controller.findEventByID(eventId);
            if (event != null) {
                handleSectionAndTicketSelection(scanner, controller, event);
            } else {
                System.out.println("Invalid Event ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void handleSectionAndTicketSelection(Scanner scanner, Controller controller, Event event) {
        Venue venue = controller.findVenueByID(event.getVenueID());
        List<Section> sections = controller.getSectionsByVenueID(venue.getID());

        if (sections.isEmpty() || (sections.size() == 1 && !venue.isHasSeats())) {
            handleSimpleTicketSelection(scanner, controller, event, venue);
        } else {
            handleSeatSelection(scanner, controller, sections, event);
        }
    }

    private static void handleSeatSelection(Scanner scanner, Controller controller, List<Section> sections, Event event) {
        System.out.println("Sections available:");
        sections.forEach(System.out::println);
        System.out.print("Enter Section ID to view available seats: ");
        try {
            int sectionID = Integer.parseInt(scanner.nextLine());
            List<Seat> availableSeats = controller.getAvailableSeatsInSection(sectionID, event.getID());
            if (availableSeats.isEmpty()) {
                System.out.println("No seats available.");
                return;
            }

            System.out.println("Available Seats:");
            availableSeats.forEach(System.out::println);

            System.out.print("Enter Seat IDs to select (comma-separated): ");
            String[] seatIds = scanner.nextLine().split(",");
            List<Seat> selectedSeats = new ArrayList<>();
            for (String seatId : seatIds) {
                Seat seat = controller.findSeatByID(Integer.parseInt(seatId.trim()));
                if (seat != null) selectedSeats.add(seat);
            }

            System.out.print("Do you want seat recommendations? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes") && !selectedSeats.isEmpty()) {
                Seat closestSeat = controller.recommendClosestSeat(sectionID, selectedSeats.get(0).getNumber());
                if (closestSeat != null) selectedSeats.add(closestSeat);
            }

            // Convert selected seats into tickets
            List<Ticket> selectedTickets = new ArrayList<>();
            double basePrice = controller.getBasePriceForEvent(event.getID()); // Fetch base price
            for (Seat seat : selectedSeats) {
                selectedTickets.add(new Ticket(0, event, seat, null, basePrice, TicketType.STANDARD));
            }

            handleCheckout(scanner, controller, event, selectedTickets, selectedTickets.size());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }


    private static void handleSimpleTicketSelection(Scanner scanner, Controller controller, Event event, Venue venue) {
        System.out.print("Enter the number of tickets to purchase (max " + venue.getVenueCapacity() + "): ");
        try {
            int ticketCount = Integer.parseInt(scanner.nextLine());
            if (ticketCount > venue.getVenueCapacity() || ticketCount <= 0) {
                System.out.println("Invalid ticket count. Try again.");
                return;
            }
            List<Ticket> tickets = new ArrayList<>();
            double basePrice = controller.getBasePriceForEvent(event.getID()); // Fetch base price
            for (int i = 0; i < ticketCount; i++) {
                tickets.add(new Ticket(0, event, null, null, basePrice, TicketType.STANDARD));
            }
            handleCheckout(scanner, controller, event, tickets, tickets.size());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void handleCheckout(Scanner scanner, Controller controller, Event event, List<Ticket> tickets, int ticketCount) {
        System.out.println("Proceeding to checkout...");

        Customer customer = (Customer) controller.getCurrentUser();
        Cart cart = controller.createCart(customer, event);

        if (tickets != null && !tickets.isEmpty()) {
            for (Ticket ticket : tickets) {
                controller.addTicketToCart(cart, ticket); // Add tickets to the cart
            }
        }

        // Retrieve updated tickets and calculate the total price
        List<Ticket> ticketsInCart = controller.getTicketsInCart(cart);
        double totalPrice = controller.calculateTotalPrice(ticketsInCart); // Fetch updated total price
        cart.setTotalPrice(totalPrice); // Update cart's total price
        System.out.println("Cart Summary:");
        ticketsInCart.forEach(System.out::println);
        System.out.println("Total Price: $" + totalPrice);

        // Handle payment details
        System.out.println("Please provide payment details:");
        System.out.print("Card Number: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Cardholder Name: ");
        String cardholderName = scanner.nextLine();
        System.out.print("Expiry Month (MM): ");
        int expiryMonth = Integer.parseInt(scanner.nextLine());
        System.out.print("Expiry Year (YYYY): ");
        int expiryYear = Integer.parseInt(scanner.nextLine());
        System.out.print("CVV: ");
        String cvv = scanner.nextLine();
        System.out.print("Currency (e.g., USD): ");
        String currency = scanner.nextLine();

        try {
            // Process the payment using the controller
            controller.processPayment(cart, cardNumber, cardholderName, expiryMonth, expiryYear, cvv, currency);
            controller.finalizeCart(cart);
            System.out.println("Payment successful! Order finalized.");
        } catch (IllegalArgumentException e) {
            System.out.println("Payment failed: " + e.getMessage());
        }
    }



    private static double getPriceForGeneralAdmission(Controller controller, Event event) {
        return event.getBasePrice();
    }

    private static double getPriceForSeat(Controller controller, Seat seat, Event event) {
        if (seat.getRow().getRowCapacity() <= 5) {
            return event.getBasePrice() * 1.5;
        } else {
            return event.getBasePrice();
        }
    }

    private static void handleViewPreviousOrders(Controller controller) {
        Customer currentCustomer = (Customer) controller.getCurrentUser();
        List<PurchaseHistory> history = controller.getPurchaseHistoryForCustomer(currentCustomer);
        if (history.isEmpty()) {
            System.out.println("No previous orders found.");
        } else {
            System.out.println("Previous Orders:");
            history.forEach(System.out::println);
        }
    }

    private static void handleManageFavourites(Scanner scanner, Controller controller) {
        System.out.println("==== Manage Favourites ====");
        Set<FavouriteEntity> favourites = controller.getFavourites();
        if (favourites.isEmpty()) {
            System.out.println("You have no favourites.");
            return;
        }
        favourites.forEach(fav -> System.out.println("- " + fav.getName()));
        System.out.print("Enter the name of the favourite to remove: ");
        String name = scanner.nextLine();
        favourites.stream()
                .filter(fav -> fav.getName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresentOrElse(
                        fav -> {
                            controller.removeFavourite(fav);
                            System.out.println(name + " removed from favourites.");
                        },
                        () -> System.out.println("Favourite not found.")
                );
    }
}
