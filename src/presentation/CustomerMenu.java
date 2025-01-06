package presentation;

import controller.Controller;
import exception.EntityNotFoundException;
import exception.ValidationException;
import model.*;

import java.util.*;

public class CustomerMenu {

    public static boolean display(Scanner scanner, Controller controller) {
        while (true) {
            try {
                System.out.println("==== Customer Menu ====");
                System.out.println("1. Logout");
                System.out.println("2. Search Events by Artists/Athletes");
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
                        throw new ValidationException("Invalid option. Please select a valid menu number.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleSearchArtistsAndAthletes(Scanner scanner, Controller controller) {
        try {
            System.out.print("Enter the name of an artist or athlete: ");
            String name = scanner.nextLine();
            if(name.isEmpty()) {
                throw new ValidationException("Artist name cannot be empty.");
            }
            Artist artist = controller.findArtistByName(name);
            if (artist != null) {
                handleEventSelectionForPerformer(scanner, controller, artist);
                return;
            }
            Athlete athlete = controller.findAthleteByName(name);
            if (athlete != null) {
                handleEventSelectionForPerformer(scanner, controller, athlete);
            } else {
                throw new EntityNotFoundException("No artist or athlete found.");
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleSearchEventsByLocation(Scanner scanner, Controller controller) {
        try {
            System.out.print("Enter location or venue name: ");
            String location = scanner.nextLine();
            if(location.isEmpty()) {
                throw new ValidationException("Venue name cannot be empty.");
            }
            List<Event> events = controller.getEventsByLocation(location);
            handleEventSelectionFromList(scanner, controller, events);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
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
        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            return;
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
            System.out.println("Error: Invalid input.");
        }
    }

    private static void handleEventSelectionForPerformer(Scanner scanner, Controller controller, Object performer) {
        try {
            List<Event> events;
            if (performer instanceof Artist artist) {
                events = controller.getUpcomingEventsForArtist(artist.getID());
            } else if (performer instanceof Athlete athlete) {
                events = controller.getUpcomingEventsForAthlete(athlete.getID());
            } else {
                return;
            }
            if (events.isEmpty()) {
                throw new EntityNotFoundException("No events found.");
            }
            System.out.print("Enter Event ID to view sections and tickets: ");
            int eventId = Integer.parseInt(scanner.nextLine());
            Event event = controller.findEventByID(eventId);
            if (event != null) {
                handleSectionAndTicketSelection(scanner, controller, event);
            } else {
                throw new ValidationException("Invalid Event ID.");
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleEventSelectionFromList(Scanner scanner, Controller controller, List<Event> events) {
        try {
            if (events.isEmpty()) {
                throw new EntityNotFoundException("No events found.");
            }
            events.forEach(System.out::println);
            System.out.print("Enter Event ID to view sections and tickets: ");
            int eventId = Integer.parseInt(scanner.nextLine());
            Event event = controller.findEventByID(eventId);
            if (event != null) {
                handleSectionAndTicketSelection(scanner, controller, event);
            } else {
                throw new ValidationException("Invalid Event ID.");
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    private static void handleSectionAndTicketSelection(Scanner scanner, Controller controller, Event event) {
        try {
            List<String> ticketAvailability = controller.getTicketAvailabilityByType(event);
            if (ticketAvailability.isEmpty()) {
                System.out.println("No tickets available for this event.");
            }
            System.out.println("Ticket availability:");
            ticketAvailability.forEach(System.out::println);
            Venue venue = controller.findVenueByID(event.getVenueID());
            if (venue == null) {
                throw new ValidationException("Venue not found.");
            }
            List<Section> sections = controller.getSectionsByVenueID(venue.getID());
            if (sections.isEmpty() || (sections.size() == 1 && !venue.isHasSeats())) {
                handleSimpleTicketSelection(scanner, controller, event, venue);
            } else {
                handleSeatSelection(scanner, controller, sections, event);
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error " + e.getMessage());
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
            List<Ticket> selectedTickets = new ArrayList<>();

            for (String seatId : seatIds) {
                Seat seat = controller.findSeatByID(Integer.parseInt(seatId.trim()));
                if (seat != null) {
                    // Fetch the ticket associated with the seat
                    Ticket ticket = seat.getTicket();
                    if (ticket != null && !ticket.isSold()) {
                        selectedTickets.add(ticket);
                    } else {
                        System.out.println("Selected seat does not have an available ticket or is already sold.");
                    }
                }
            }

            if (selectedTickets.isEmpty()) {
                System.out.println("No valid tickets selected.");
                return;
            }

            System.out.print("Do you want seat recommendations? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes") && !selectedTickets.isEmpty()) {
                Seat closestSeat = controller.recommendClosestSeat(sectionID, selectedTickets.get(0).getSeat().getNumber());
                if (closestSeat != null && closestSeat.getTicket() != null && !closestSeat.getTicket().isSold()) {
                    selectedTickets.add(closestSeat.getTicket());
                    System.out.println("Recommended seat added: " + closestSeat.getTicket());
                }
            }

            handleCheckout(scanner, controller, event, selectedTickets, selectedTickets.size());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid input.");
        }
    }


    private static void handleSimpleTicketSelection(Scanner scanner, Controller controller, Event event, Venue venue) {
        List<Ticket> selectedTickets = new ArrayList<>();

        while (true) {
            System.out.println("Select ticket type:");
            System.out.println("1. Early Bird");
            System.out.println("2. VIP");
            System.out.println("3. Standard");
            System.out.println("4. Proceed to Checkout");
            System.out.println("0. Cancel and Exit");
            System.out.print("Choose an option: ");
            String ticketTypeChoice = scanner.nextLine();

            TicketType selectedType = null;
            switch (ticketTypeChoice) {
                case "1":
                    selectedType = TicketType.EARLY_BIRD;
                    break;
                case "2":
                    selectedType = TicketType.VIP;
                    break;
                case "3":
                    selectedType = TicketType.STANDARD;
                    break;
                case "4":
                    // Proceed to checkout
                    if (!selectedTickets.isEmpty()) {
                        handleCheckout(scanner, controller, event, selectedTickets, selectedTickets.size());
                    } else {
                        System.out.println("No tickets selected. Returning to menu.");
                    }
                    return; // Exit this method after checkout
                case "0":
                    System.out.println("Exiting ticket selection.");
                    return; // Exit without proceeding to checkout
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }

            // Validate and add tickets for the selected type
            if (selectedType != null) {
                List<Ticket> availableTickets = controller.getAvailableTicketsByType(event, selectedType);
                if (availableTickets.isEmpty()) {
                    System.out.println(selectedType + " tickets are sold out.");
                    continue;
                }

                if (selectedType == TicketType.STANDARD) {
                    List<Ticket> earlyBirdTickets = controller.getAvailableTicketsByType(event, TicketType.EARLY_BIRD);
                    if (!earlyBirdTickets.isEmpty()) {
                        System.out.println("You cannot purchase standard tickets until early bird tickets are sold out.");
                        continue;
                    }
                }

                System.out.print("Enter number of tickets to purchase (max " + availableTickets.size() + "): ");
                try {
                    int ticketCount = Integer.parseInt(scanner.nextLine());
                    if (ticketCount > availableTickets.size() || ticketCount <= 0) {
                        System.out.println("Invalid ticket count. Try again.");
                        continue;
                    }
                    List<Ticket> ticketsToBuy = availableTickets.subList(0, ticketCount);
                    selectedTickets.addAll(ticketsToBuy);
                    System.out.println(ticketCount + " " + selectedType + " tickets added to your selection.");
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid input.");
                }
            }
        }
    }

    private static void handleCheckout(Scanner scanner, Controller controller, Event event, List<Ticket> tickets, int ticketCount) {
        System.out.println("Proceeding to checkout...");

        Customer customer = (Customer) controller.getCurrentUser();
        Cart cart = controller.createCart(customer, event);

        if (tickets != null && !tickets.isEmpty()) {
            for (Ticket ticket : tickets) {
                boolean added = controller.addTicketToCart(cart, ticket);
                if (!added) {
                    System.out.println("Failed to add ticket to cart: " + ticket);
                    return;
                }
            }
        }

        // Retrieve tickets and calculate total price using CartService
        List<Ticket> ticketsInCart = controller.getTicketsInCart(cart);
        double totalPrice = cart.calculateTotalPrice(); // Using Cart's calculate method
        System.out.println("Cart Summary:");
        ticketsInCart.forEach(System.out::println);
        System.out.println("Total Price: $" + totalPrice);

        // Handle payment details
        System.out.println("Please provide payment details:");
        System.out.print("Card Number (16 digits): ");
        String cardNumber = scanner.nextLine();

        System.out.print("Cardholder Name: ");
        String cardholderName = scanner.nextLine();

        System.out.print("Expiry Date (MM/YY): ");
        String expiryDate = scanner.nextLine();
        int expiryMonth, expiryYear;
        try {
            String[] parts = expiryDate.split("/");
            expiryMonth = Integer.parseInt(parts[0]);
            expiryYear = 2000 + Integer.parseInt(parts[1]); // Convert YY to YYYY
        } catch (Exception e) {
            System.out.println("Error: Invalid expiry date format. Must be MM/YY.");
            return;
        }

        System.out.print("CVV (3 or 4 digits): ");
        String cvv = scanner.nextLine();

        try {
            controller.processPayment(cart, cardNumber, cardholderName,
                    expiryMonth, expiryYear, cvv);
            controller.clearCart(cart);
            System.out.println("Payment successful! Order finalized.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Payment failed: " + e.getMessage());
            controller.clearCart(cart);
        } catch (IllegalStateException e) {
            System.out.println("Error processing order: " + e.getMessage());
            controller.clearCart(cart);
        }
    }

    private static void handleViewPreviousOrders(Controller controller) {
        try {
            Customer currentCustomer = (Customer) controller.getCurrentUser();
            List<Ticket> purchasedTickets = controller.getTicketsByCustomer(currentCustomer);
            if (purchasedTickets.isEmpty()) {
                throw new EntityNotFoundException("No previous orders found.");
            } else {
                System.out.println("Previous Orders:");
                purchasedTickets.forEach(ticket -> {
                    System.out.println("Ticket ID: " + ticket.getID());
                    System.out.println("Event: " + ticket.getEvent().getEventName());
                    System.out.println("Price: $" + ticket.getPrice());
                    System.out.println("Purchase Date: " + ticket.getPurchaseDate());
                    if (ticket.getSeat() != null) {
                        System.out.println("Seat: " + ticket.getSeat().getNumber());
                    } else {
                        System.out.println("General Admission");
                    }
                    System.out.println("-----------------------------");
                });
            }
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    private static void handleManageFavourites(Scanner scanner, Controller controller) {
        while (true) {
            System.out.println("==== Manage Favourites ====");
            System.out.println("1. View Favourites");
            System.out.println("2. Add Favourite (Search by Artist/Athlete)");
            System.out.println("3. Remove Favourite");
            System.out.println("0. Return to Main Menu");
            System.out.println("===========================");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // View favourites
                    Set<FavouriteEntity> favourites = controller.getFavourites();
                    if (favourites.isEmpty()) {
                        System.out.println("You have no favourites.");
                    } else {
                        System.out.println("Your Favourites:");
                        favourites.forEach(fav -> System.out.println("- " + fav.getName()));
                    }
                    break;

                case "2":
                    // Add favourite
                    System.out.print("Enter the name of the artist or athlete to add: ");
                    String nameToAdd = scanner.nextLine();

                    // Caută separat artistul și atletul
                    Artist foundArtist = controller.findArtistByName(nameToAdd);
                    Athlete foundAthlete = controller.findAthleteByName(nameToAdd);

                    // Dacă nu s-a găsit niciun rezultat
                    if (foundArtist == null && foundAthlete == null) {
                        System.out.println("No artist or athlete found with the name: " + nameToAdd);
                        break;
                    }

                    // Creează o listă pentru selecție
                    Map<Integer, FavouriteEntity> selectionMap = new HashMap<>();
                    int index = 1;

                    if (foundArtist != null) {
                        System.out.println(index + ". Artist: " + foundArtist.getName());
                        selectionMap.put(index, foundArtist);
                        index++;
                    }

                    if (foundAthlete != null) {
                        System.out.println(index + ". Athlete: " + foundAthlete.getName());
                        selectionMap.put(index, foundAthlete);
                        index++;
                    }

                    // Solicită utilizatorului să selecteze un favorit
                    System.out.print("Enter the number of the favourite to add (or 0 to cancel): ");
                    try {
                        int selectedOption = Integer.parseInt(scanner.nextLine());
                        if (selectedOption == 0) {
                            System.out.println("Cancelled adding to favourites.");
                            break;
                        }
                        FavouriteEntity selectedEntity = selectionMap.get(selectedOption);
                        if (selectedEntity != null) {
                            controller.addFavourite(selectedEntity);
                            System.out.println(selectedEntity.getName() + " has been added to your favourites.");
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                    break;

                case "3":
                    // Remove favourite
                    Set<FavouriteEntity> currentFavourites = controller.getFavourites();
                    if (currentFavourites.isEmpty()) {
                        System.out.println("You have no favourites to remove.");
                        break;
                    }
                    System.out.println("Your Favourites:");
                    int removeIndex = 1;
                    Map<Integer, FavouriteEntity> removeMap = new HashMap<>();
                    for (FavouriteEntity fav : currentFavourites) {
                        System.out.println(removeIndex + ". " + fav.getName());
                        removeMap.put(removeIndex, fav);
                        removeIndex++;
                    }

                    System.out.print("Enter the number of the favourite to remove (or 0 to cancel): ");
                    try {
                        int selectedRemoveOption = Integer.parseInt(scanner.nextLine());
                        if (selectedRemoveOption == 0) {
                            System.out.println("Cancelled removing favourite.");
                            break;
                        }
                        FavouriteEntity entityToRemove = removeMap.get(selectedRemoveOption);
                        if (entityToRemove != null) {
                            controller.removeFavourite(entityToRemove);
                            System.out.println(entityToRemove.getName() + " removed from favourites.");
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                    break;

                case "0":
                    // Return to main menu
                    System.out.println("Returning to the main menu.");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}