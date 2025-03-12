package main.java.com.ticketsalesapp.view;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.EntityNotFoundException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.*;
import main.java.com.ticketsalesapp.model.event.Artist;
import main.java.com.ticketsalesapp.model.event.Athlete;
import main.java.com.ticketsalesapp.model.event.Event;
import main.java.com.ticketsalesapp.model.ticket.Cart;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.ticket.TicketType;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.venue.Seat;
import main.java.com.ticketsalesapp.model.venue.Section;
import main.java.com.ticketsalesapp.model.venue.Venue;
import main.java.com.ticketsalesapp.service.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomerMenu {

    private final CustomerService customerService;
    private final AccountAction accountAction;

    @Autowired
    public CustomerMenu(CustomerService customerService, AccountAction accountAction) {
        this.customerService = customerService;
        this.accountAction = accountAction;
    }

    public boolean display(Scanner scanner, Customer customer) {
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
                        return accountAction.handleLogout(customer);
                    case "2":
                        System.out.println("Search Events by Artists/Athletes - Not implemented yet.");
                        break;
                    case "3":
                        System.out.println("Search Events by Location/Venue - Not implemented yet.");
                        break;
                    case "4":
                        System.out.println("View Suggested Events - Not implemented yet.");
                        break;
                    case "5":
                        System.out.println("View All Events - Not implemented yet.");
                        break;
                    case "6":
                        System.out.println("View Orders History - Not implemented yet.");
                        break;
                    case "7":
                        System.out.println("Manage Favourites - Not implemented yet.");
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

//    /**
//     * Handles the search for events by artists or athletes.
//     *
//     * @param scanner    the scanner for reading user input.
//     * @param applicationController the controller for accessing application functionality.
//     */
//    private static void handleSearchArtistsAndAthletes(Scanner scanner, ApplicationController applicationController) {
//        try {
//            System.out.print("Enter the name of an artist or athlete: ");
//            String name = scanner.nextLine();
//            if(name.isEmpty()) {
//                throw new ValidationException("Artist name cannot be empty.");
//            }
//            Artist artist = applicationController.findArtistByName(name);
//            if (artist != null) {
//                handleEventSelectionForPerformer(scanner, applicationController, artist);
//                return;
//            }
//            Athlete athlete = applicationController.findAthleteByName(name);
//            if (athlete != null) {
//                handleEventSelectionForPerformer(scanner, applicationController, athlete);
//            } else {
//                throw new EntityNotFoundException("No artist or athlete found.");
//            }
//        } catch (ValidationException | EntityNotFoundException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//    /**
//     * Handles the search for events by location or venue.
//     *
//     * @param scanner    the scanner for reading user input.
//     * @param applicationController the controller for accessing application functionality.
//     */
//    private static void handleSearchEventsByLocation(Scanner scanner, ApplicationController applicationController) {
//        try {
//            System.out.print("Enter location or venue name: ");
//            String location = scanner.nextLine();
//            if(location.isEmpty()) {
//                throw new ValidationException("Venue name cannot be empty.");
//            }
//            List<Event> events = applicationController.getEventsByLocation(location);
//            handleEventSelectionFromList(scanner, applicationController, events);
//        } catch (ValidationException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//    /**
//     * Handles the viewing of suggested events based on the user's favourites.
//     *
//     * @param scanner    the scanner for reading user input.
//     * @param applicationController the controller for accessing application functionality.
//     */
//    private static void handleViewSuggestedEvents(Scanner scanner, ApplicationController applicationController) {
//        System.out.println("==== Suggested Events ====");
//        Set<FavouriteEntity> favourites = applicationController.getFavourites();
//        if (favourites.isEmpty()) {
//            System.out.println("No favourites to generate suggestions from.");
//            return;
//        }
//        List<Event> suggestedEvents = new ArrayList<>();
//        for (FavouriteEntity favourite : favourites) {
//            if (favourite instanceof Artist artist) {
//                // Add upcoming events for the favourite artist
//                suggestedEvents.addAll(applicationController.getUpcomingEventsForArtist(artist.getID()));
//                // Add upcoming events for related artists (same genre)
//                List<Artist> relatedArtists = applicationController.findArtistsByGenre(artist.getGenre());
//                for (Artist relatedArtist : relatedArtists) {
//                    if (!relatedArtist.equals(artist)) {
//                        suggestedEvents.addAll(applicationController.getUpcomingEventsForArtist(relatedArtist.getID()));
//                    }
//                }
//            } else if (favourite instanceof Athlete athlete) {
//                // Add upcoming events for the favourite athlete
//                suggestedEvents.addAll(applicationController.getUpcomingEventsForAthlete(athlete.getID()));
//
//                // Add upcoming events for related athletes (same sport)
//                List<Athlete> relatedAthletes = applicationController.findAthletesBySport(athlete.getAthleteSport());
//                for (Athlete relatedAthlete : relatedAthletes) {
//                    if (!relatedAthlete.equals(athlete)) {
//                        suggestedEvents.addAll(applicationController.getUpcomingEventsForAthlete(relatedAthlete.getID()));
//                    }
//                }
//            }
//        }
//        if (suggestedEvents.isEmpty()) {
//            if (favourites.isEmpty()) {
//                throw new BusinessLogicException("Cannot generate suggestions: no favourites available.");
//            }
//        } else {
//            System.out.println("Here are some events you might be interested in:");
//            suggestedEvents.forEach(event -> System.out.println(event.getEventName() + " - " + event.getStartDateTime()));
//        }
//        handleEventSelectionFromList(scanner, applicationController, suggestedEvents);
//    }
//
//    /**
//     * Handles the viewing of all events and allows the user to select an event.
//     *
//     * @param scanner    the scanner for reading user input.
//     * @param applicationController the controller for accessing application functionality.
//     */
//    private static void handleViewAllEvents(Scanner scanner, ApplicationController applicationController) {
//        List<Event> events = applicationController.getAllEvents();
//        if (events.isEmpty()) {
//            return;
//        }
//        System.out.print("Enter Event ID to view sections and tickets: ");
//        try {
//            int eventId = Integer.parseInt(scanner.nextLine());
//            Event event = applicationController.findEventByID(eventId);
//            if (event != null) {
//                handleSectionAndTicketSelection(scanner, applicationController, event);
//            } else {
//                System.out.println("Invalid Event ID.");
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Error: Invalid input.");
//        }
//    }
//
//    /**
//     * Handles the management of favourites, including viewing, adding, and removing favourites.
//     *
//     * @param scanner    the scanner for reading user input.
//     * @param applicationController the controller for accessing application functionality.
//     */
//    private static void handleEventSelectionForPerformer(Scanner scanner, ApplicationController applicationController, Object performer) {
//        try {
//            List<Event> events;
//            if (performer instanceof Artist artist) {
//                events = applicationController.getUpcomingEventsForArtist(artist.getID());
//            } else if (performer instanceof Athlete athlete) {
//                events = applicationController.getUpcomingEventsForAthlete(athlete.getID());
//            } else {
//                return;
//            }
//            if (events.isEmpty()) {
//                throw new EntityNotFoundException("No events found.");
//            }
//            System.out.print("Enter Event ID to view sections and tickets: ");
//            int eventId = Integer.parseInt(scanner.nextLine());
//            Event event = applicationController.findEventByID(eventId);
//            if (event != null) {
//                handleSectionAndTicketSelection(scanner, applicationController, event);
//            } else {
//                throw new ValidationException("Invalid Event ID.");
//            }
//        } catch (ValidationException | EntityNotFoundException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//    /**
//     * Handles the selection of events from a given list and allows users to choose an event to view its sections and tickets.
//     *
//     * @param scanner    the Scanner for reading user input.
//     * @param applicationController the Controller for accessing application functionality.
//     * @param events     the list of events to select from.
//     */
//    private static void handleEventSelectionFromList(Scanner scanner, ApplicationController applicationController, List<Event> events) {
//        try {
//            if (events.isEmpty()) {
//                throw new EntityNotFoundException("No events found.");
//            }
//            // Events are already printed by the Controller, so no need to print here
//            System.out.print("Enter Event ID to view sections and tickets: ");
//            int eventId = Integer.parseInt(scanner.nextLine());
//            Event event = applicationController.findEventByID(eventId);
//            if (event != null) {
//                handleSectionAndTicketSelection(scanner, applicationController, event);
//            } else {
//                throw new ValidationException("Invalid Event ID.");
//            }
//        } catch (ValidationException | EntityNotFoundException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Handles the selection of sections and tickets for a given event.
//     *
//     * @param scanner    the Scanner for reading user input.
//     * @param applicationController the Controller for accessing application functionality.
//     * @param event      the event for which sections and tickets are being selected.
//     */
//    private static void handleSectionAndTicketSelection(Scanner scanner, ApplicationController applicationController, Event event) {
//        try {
//            List<String> ticketAvailability = applicationController.getTicketAvailabilityByType(event);
//            if (ticketAvailability.isEmpty()) {
//                System.out.println("No tickets available for this event.");
//            }
//            System.out.println("Ticket availability:");
//            ticketAvailability.forEach(System.out::println);
//            Venue venue = applicationController.findVenueByID(event.getVenueID());
//            if (venue == null) {
//                throw new ValidationException("Venue not found.");
//            }
//            List<Section> sections = applicationController.getSectionsByVenueID(venue.getID());
//            if (sections.isEmpty() || (sections.size() == 1 && !venue.isHasSeats())) {
//                handleSimpleTicketSelection(scanner, applicationController, event, venue);
//            } else {
//                handleSeatSelection(scanner, applicationController, sections, event);
//            }
//        } catch (ValidationException | EntityNotFoundException e) {
//            System.out.println("Error " + e.getMessage());
//        }
//    }
//
//    /**
//     * Handles the selection of seats for a given event and allows users to select tickets or get seat recommendations.
//     *
//     * @param scanner    the Scanner for reading user input.
//     * @param applicationController the Controller for accessing application functionality.
//     * @param sections   the list of sections available for the event.
//     * @param event      the event for which seats are being selected.
//     */
//    private static void handleSeatSelection(Scanner scanner, ApplicationController applicationController, List<Section> sections, Event event) {
//        System.out.println("Sections available:");
//        sections.forEach(System.out::println);
//        System.out.print("Enter Section ID to view available seats: ");
//        try {
//            int sectionID = Integer.parseInt(scanner.nextLine());
//            List<Seat> availableSeats = applicationController.getAvailableSeatsInSection(sectionID, event.getID());
//            if (availableSeats.isEmpty()) {
//                System.out.println("No seats available.");
//                return;
//            }
//
//            System.out.println("Available Seats:");
//            availableSeats.forEach(System.out::println);
//
//            System.out.print("Enter Seat IDs to select (comma-separated): ");
//            String[] seatIds = scanner.nextLine().split(",");
//            List<Ticket> selectedTickets = new ArrayList<>();
//            List<Integer> selectedSeatNumbers = new ArrayList<>();
//            List<Integer> selectedRowIDs = new ArrayList<>(); // To track rows of selected seats
//
//            for (String seatId : seatIds) {
//                Seat seat = applicationController.findSeatByID(Integer.parseInt(seatId.trim()));
//                if (seat != null) {
//                    // Fetch the ticket associated with the seat
//                    Ticket ticket = seat.getTicket();
//                    if (ticket != null && !ticket.isSold()) {
//                        selectedTickets.add(ticket);
//                        selectedSeatNumbers.add(seat.getNumber());
//                        if (!selectedRowIDs.contains(seat.getRow().getID())) {
//                            selectedRowIDs.add(seat.getRow().getID()); // Track the row ID of the seat
//                        }
//                    } else {
//                        System.out.println("Selected seat does not have an available ticket or is already sold.");
//                    }
//                }
//            }
//
//            if (selectedTickets.isEmpty()) {
//                System.out.println("No valid tickets selected.");
//                return;
//            }
//
//            System.out.print("Do you want seat recommendations? (yes/no): ");
//            if (scanner.nextLine().equalsIgnoreCase("yes") && !selectedSeatNumbers.isEmpty() && !selectedRowIDs.isEmpty()) {
//                Seat closestSeat = null;
//
//                // Find the closest seat from any of the selected rows
//                for (int rowID : selectedRowIDs) {
//                    Seat recommendedSeat = applicationController.recommendClosestSeat(sectionID, rowID, selectedSeatNumbers);
//                    if (recommendedSeat != null) {
//                        // Update closestSeat if it's closer than the previous recommendation
//                        if (closestSeat == null || Math.abs(recommendedSeat.getNumber() - selectedSeatNumbers.get(0)) <
//                                Math.abs(closestSeat.getNumber() - selectedSeatNumbers.get(0))) {
//                            closestSeat = recommendedSeat;
//                        }
//                    }
//                }
//
//                if (closestSeat != null && closestSeat.getTicket() != null && !closestSeat.getTicket().isSold()) {
//                    System.out.print("Recommended seat found: " + closestSeat + ". Do you want to add it to your cart? (yes/no): ");
//                    if (scanner.nextLine().equalsIgnoreCase("yes")) {
//                        selectedTickets.add(closestSeat.getTicket());
//                        System.out.println("Recommended seat added: " + closestSeat.getTicket());
//                    } else {
//                        System.out.println("Recommended seat not added.");
//                    }
//                } else {
//                    System.out.println("No suitable recommended seat found.");
//                }
//            }
//
//            handleCheckout(scanner, applicationController, event, selectedTickets, selectedTickets.size());
//        } catch (NumberFormatException e) {
//            System.out.println("Error: Invalid input.");
//        }
//    }
//
//
//
//    /**
//     * Handles the selection of tickets without specific seats for a given event.
//     *
//     * @param scanner    the Scanner for reading user input.
//     * @param applicationController the Controller for accessing application functionality.
//     * @param event      the event for which tickets are being selected.
//     * @param venue      the venue where the event is taking place.
//     */
//    private static void handleSimpleTicketSelection(Scanner scanner, ApplicationController applicationController, Event event, Venue venue) {
//        List<Ticket> selectedTickets = new ArrayList<>();
//
//        while (true) {
//            System.out.println("Select ticket type:");
//            System.out.println("1. Early Bird");
//            System.out.println("2. VIP");
//            System.out.println("3. Standard");
//            System.out.println("4. Proceed to Checkout");
//            System.out.println("0. Cancel and Exit");
//            System.out.print("Choose an option: ");
//            String ticketTypeChoice = scanner.nextLine();
//
//            TicketType selectedType = null;
//            switch (ticketTypeChoice) {
//                case "1":
//                    selectedType = TicketType.EARLY_BIRD;
//                    break;
//                case "2":
//                    selectedType = TicketType.VIP;
//                    break;
//                case "3":
//                    selectedType = TicketType.STANDARD;
//                    break;
//                case "4":
//                    // Proceed to checkout
//                    if (!selectedTickets.isEmpty()) {
//                        handleCheckout(scanner, applicationController, event, selectedTickets, selectedTickets.size());
//                    } else {
//                        System.out.println("No tickets selected. Returning to menu.");
//                    }
//                    return; // Exit this method after checkout
//                case "0":
//                    System.out.println("Exiting ticket selection.");
//                    return; // Exit without proceeding to checkout
//                default:
//                    System.out.println("Invalid choice.");
//                    continue;
//            }
//
//            // Validate and add tickets for the selected type
//            if (selectedType != null) {
//                List<Ticket> availableTickets = applicationController.getAvailableTicketsByType(event, selectedType);
//                if (availableTickets.isEmpty()) {
//                    System.out.println(selectedType + " tickets are sold out.");
//                    continue;
//                }
//
//                if (selectedType == TicketType.STANDARD) {
//                    List<Ticket> earlyBirdTickets = applicationController.getAvailableTicketsByType(event, TicketType.EARLY_BIRD);
//                    if (!earlyBirdTickets.isEmpty()) {
//                        System.out.println("You cannot purchase standard tickets until early bird tickets are sold out.");
//                        continue;
//                    }
//                }
//
//                System.out.print("Enter number of tickets to purchase (max " + availableTickets.size() + "): ");
//                try {
//                    int ticketCount = Integer.parseInt(scanner.nextLine());
//                    if (ticketCount > availableTickets.size() || ticketCount <= 0) {
//                        System.out.println("Invalid ticket count. Try again.");
//                        continue;
//                    }
//                    List<Ticket> ticketsToBuy = availableTickets.subList(0, ticketCount);
//                    selectedTickets.addAll(ticketsToBuy);
//                    System.out.println(ticketCount + " " + selectedType + " tickets added to your selection.");
//                } catch (NumberFormatException e) {
//                    System.out.println("Error: Invalid input.");
//                }
//            }
//        }
//    }
//
//    /**
//     * Handles the checkout process for the selected tickets and processes payment details.
//     *
//     * @param scanner     the Scanner for reading user input.
//     * @param applicationController  the Controller for accessing application functionality.
//     * @param event       the event for which tickets are being purchased.
//     * @param tickets     the list of selected tickets.
//     * @param ticketCount the number of tickets being purchased.
//     */
//    private static void handleCheckout(Scanner scanner, ApplicationController applicationController, Event event, List<Ticket> tickets, int ticketCount) {
//        System.out.println("Proceeding to checkout...");
//
//        Customer customer = (Customer) applicationController.getCurrentUser();
//        Cart cart = applicationController.createCart(customer, event);
//
//        if (tickets != null && !tickets.isEmpty()) {
//            for (Ticket ticket : tickets) {
//                boolean added = applicationController.addTicketToCart(cart, ticket);
//                if (!added) {
//                    System.out.println("Failed to add ticket to cart: " + ticket);
//                    return;
//                }
//            }
//        }
//
//        // Retrieve tickets and calculate total price using CartService
//        List<Ticket> ticketsInCart = applicationController.getTicketsInCart(cart);
//        double totalPrice = cart.calculateTotalPrice(); // Using Cart's calculate method
//        System.out.println("Cart Summary:");
//        ticketsInCart.forEach(System.out::println);
//        System.out.println("Total Price: $" + totalPrice);
//
//        // Handle payment details
//        System.out.println("Please provide payment details:");
//        System.out.print("Card Number (16 digits): ");
//        String cardNumber = scanner.nextLine();
//
//        System.out.print("Cardholder Name: ");
//        String cardholderName = scanner.nextLine();
//
//        System.out.print("Expiry Date (MM/YY): ");
//        String expiryDate = scanner.nextLine();
//        int expiryMonth, expiryYear;
//        try {
//            String[] parts = expiryDate.split("/");
//            expiryMonth = Integer.parseInt(parts[0]);
//            expiryYear = 2000 + Integer.parseInt(parts[1]); // Convert YY to YYYY
//        } catch (Exception e) {
//            System.out.println("Error: Invalid expiry date format. Must be MM/YY.");
//            return;
//        }
//
//        System.out.print("CVV (3 or 4 digits): ");
//        String cvv = scanner.nextLine();
//
//        try {
//            applicationController.processPayment(cart, cardNumber, cardholderName,
//                    expiryMonth, expiryYear, cvv);
//            applicationController.clearCart(cart);
//            System.out.println("Payment successful! Order finalized.");
//        } catch (IllegalArgumentException e) {
//            System.out.println("Error: Payment failed: " + e.getMessage());
//            applicationController.clearCart(cart);
//        } catch (IllegalStateException e) {
//            System.out.println("Error processing order: " + e.getMessage());
//            applicationController.clearCart(cart);
//        }
//    }
//
//    /**
//     * Handles the display of the current customer's previous orders, including ticket details such as ID,
//     * event name, price, purchase date, and seat information. If no orders are found, an appropriate
//     * message is displayed.
//     *
//     * @param applicationController the Controller instance used to access the customer's data and previous orders.
//     */
//    private static void handleViewPreviousOrders(ApplicationController applicationController) {
//        try {
//            Customer currentCustomer = (Customer) applicationController.getCurrentUser();
//            List<Ticket> purchasedTickets = applicationController.getTicketsByCustomer(currentCustomer);
//            if (purchasedTickets.isEmpty()) {
//                throw new EntityNotFoundException("No previous orders found.");
//            } else {
//                System.out.println("Previous Orders:");
//                purchasedTickets.forEach(ticket -> {
//                    System.out.println("Ticket ID: " + ticket.getID());
//                    System.out.println("Event: " + ticket.getEvent().getEventName());
//                    System.out.println("Price: $" + ticket.getPrice());
//                    System.out.println("Purchase Date: " + ticket.getPurchaseDate());
//                    if (ticket.getSeat() != null) {
//                        System.out.println("Seat: " + ticket.getSeat().getNumber());
//                    } else {
//                        System.out.println("General Admission");
//                    }
//                    System.out.println("-----------------------------");
//                });
//            }
//        } catch (IllegalArgumentException | EntityNotFoundException e) {
//            System.out.println("Error" + e.getMessage());
//        }
//    }
//
//    private static void handleManageFavourites(Scanner scanner, ApplicationController applicationController) {
//        while (true) {
//            System.out.println("==== Manage Favourites ====");
//            System.out.println("1. View Favourites");
//            System.out.println("2. Add Favourite (Search by Artist/Athlete)");
//            System.out.println("3. Remove Favourite");
//            System.out.println("0. Return to Main Menu");
//            System.out.println("===========================");
//            System.out.print("Choose an option: ");
//            String choice = scanner.nextLine();
//
//            switch (choice) {
//                case "1":
//                    // View favourites
//                    Set<FavouriteEntity> favourites = applicationController.getFavourites();
//                    if (favourites.isEmpty()) {
//                        System.out.println("You have no favourites.");
//                    } else {
//                        System.out.println("Your Favourites:");
//                        favourites.forEach(fav -> System.out.println("- " + fav.getName()));
//                    }
//                    break;
//
//                case "2":
//                    // Add favourite
//                    System.out.print("Enter the name of the artist or athlete to add: ");
//                    String nameToAdd = scanner.nextLine();
//
//                    Artist foundArtist = applicationController.findArtistByName(nameToAdd);
//                    Athlete foundAthlete = applicationController.findAthleteByName(nameToAdd);
//
//                    if (foundArtist == null && foundAthlete == null) {
//                        System.out.println("No artist or athlete found with the name: " + nameToAdd);
//                        break;
//                    }
//
//                    Map<Integer, FavouriteEntity> selectionMap = new HashMap<>();
//                    int index = 1;
//
//                    if (foundArtist != null) {
//                        System.out.println(index + ". Artist: " + foundArtist.getName());
//                        selectionMap.put(index, foundArtist);
//                        index++;
//                    }
//
//                    if (foundAthlete != null) {
//                        System.out.println(index + ". Athlete: " + foundAthlete.getName());
//                        selectionMap.put(index, foundAthlete);
//                        index++;
//                    }
//
//                    System.out.print("Enter the number of the favourite to add (or 0 to cancel): ");
//                    try {
//                        int selectedOption = Integer.parseInt(scanner.nextLine());
//                        if (selectedOption == 0) {
//                            System.out.println("Cancelled adding to favourites.");
//                            break;
//                        }
//                        FavouriteEntity selectedEntity = selectionMap.get(selectedOption);
//                        if (selectedEntity != null) {
//                            applicationController.addFavourite(selectedEntity);
//                            System.out.println(selectedEntity.getName() + " has been added to your favourites.");
//                        } else {
//                            System.out.println("Invalid selection.");
//                        }
//                    } catch (NumberFormatException e) {
//                        System.out.println("Invalid input. Please enter a number.");
//                    }
//                    break;
//
//                case "3":
//                    // Remove favourite
//                    Set<FavouriteEntity> currentFavourites = applicationController.getFavourites();
//                    if (currentFavourites.isEmpty()) {
//                        System.out.println("You have no favourites to remove.");
//                        break;
//                    }
//                    System.out.println("Your Favourites:");
//                    int removeIndex = 1;
//                    Map<Integer, FavouriteEntity> removeMap = new HashMap<>();
//                    for (FavouriteEntity fav : currentFavourites) {
//                        System.out.println(removeIndex + ". " + fav.getName());
//                        removeMap.put(removeIndex, fav);
//                        removeIndex++;
//                    }
//
//                    System.out.print("Enter the number of the favourite to remove (or 0 to cancel): ");
//                    try {
//                        int selectedRemoveOption = Integer.parseInt(scanner.nextLine());
//                        if (selectedRemoveOption == 0) {
//                            System.out.println("Cancelled removing favourite.");
//                            break;
//                        }
//                        FavouriteEntity entityToRemove = removeMap.get(selectedRemoveOption);
//                        if (entityToRemove != null) {
//                            applicationController.removeFavourite(entityToRemove);
//                            System.out.println(entityToRemove.getName() + " removed from favourites.");
//                        } else {
//                            System.out.println("Invalid selection.");
//                        }
//                    } catch (NumberFormatException e) {
//                        System.out.println("Invalid input. Please enter a number.");
//                    }
//                    break;
//
//                case "0":
//                    // Return to main menu
//                    System.out.println("Returning to the main menu.");
//                    return;
//
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//    }

}