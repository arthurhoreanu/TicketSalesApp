package presentation;

import controller.Controller;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Provides a menu for customers to manage their interactions within the application,
 * including viewing suggested events, managing favourites, and handling their shopping cart.
 */
public class CustomerMenu {

    /**
     * Displays the customer menu and processes the selected options.
     * @param scanner the scanner to read user input
     * @param controller the controller to handle customer-related actions
     * @return {@code true} to keep displaying the menu; {@code false} to exit the application
     */
    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Customer Menu ====");
        System.out.println("1. Logout");
        System.out.println("2. Search for Artists/Athletes");
        System.out.println("3. Search for Locations/Venues");
        System.out.println("4. View Suggested Events");
        System.out.println("5. Manage Shopping Cart");
        System.out.println("6. Checkout Shopping Cart");
        System.out.println("7. View Previous Orders");
        System.out.println("8. Manage Favourites");
        System.out.println("9. View recommended Seats");
        System.out.println("0. Exit");
        System.out.println("=======================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                controller.logout();
                System.out.println("Logged out successfully.");
                break;
            case "2":
                handleSearchArtistsAndAthletes(scanner, controller);
                break;
            case "3":
                handleViewEventsByLocation(scanner, controller);
                break;
            case "4":
                handleViewSuggestedEvents(scanner, controller);
                break;
            case "5":
                handleManageShoppingCart(scanner, controller);
                break;
            case "6":
                handleCheckout(scanner, controller);
                break;
            case "7":
                handleViewPreviousOrders(controller);
                break;
            case "8":
                handleManageFavourites(scanner, controller);
                break;
            case "9":
                recommendSeat(scanner, controller);
                break;
            case "0":
                System.out.println("Exiting the application. Goodbye!");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }

    /**
     * Displays upcoming events for a given performer (either an artist or athlete), allowing the user
     * to view available tickets for a selected event and add them to their shopping cart. The user can
     * also choose to mark the performer as a favourite.
     *
     * @param scanner the scanner to read user input
     * @param controller the controller to interact with the application data
     * @param performer the performer (Artist or Athlete) whose events and tickets are to be viewed and managed
     */
    private static void upcomingEventsTicketsToShoppingCartMarkAsFavourite(Scanner scanner, Controller controller, Object performer) {
        String name;
        int id;
        List<Event> upcomingEvents;

        if (performer instanceof Artist artist) {
            name = artist.getArtistName();
            id = artist.getID();
            upcomingEvents = controller.getUpcomingEventsForArtist(id);
        } else if (performer instanceof Athlete athlete) {
            name = athlete.getAthleteName();
            id = athlete.getID();
            upcomingEvents = controller.getUpcomingEventsForAthlete(id);
        } else {
            throw new IllegalArgumentException("Unknown performer type.");
        }

        if (upcomingEvents.isEmpty()) {
            System.out.println("No upcoming events for " + name + ".");
        } else {
            System.out.println("Upcoming events for " + name + ":");
            upcomingEvents.forEach(System.out::println);

            // Prompt for Event ID to view tickets or skip
            System.out.print("Enter Event ID to view tickets (or press Enter to skip): ");
            String eventIdInput = scanner.nextLine().trim();

            if (!eventIdInput.isEmpty()) {
                try {
                    int eventId = Integer.parseInt(eventIdInput);
                    Event selectedEvent = controller.findEventByID(eventId);

                    if (selectedEvent != null) {
                        List<Ticket> tickets = controller.getTicketsByEvent(eventId);

                        if (tickets.isEmpty()) {
                            System.out.println("No tickets available for this event.");
                        } else {
                            System.out.println("Available tickets for event " + selectedEvent.getEventName() + ":");
                            tickets.forEach(System.out::println);

                            // Prompt for Ticket ID to add to shopping cart
                            System.out.print("Enter Ticket ID to add to shopping cart: ");
                            String ticketIdInput = scanner.nextLine().trim();

                            if (!ticketIdInput.isEmpty()) {
                                try {
                                    int ticketId = Integer.parseInt(ticketIdInput);
                                    Ticket ticketToAdd = controller.getTicketByID(ticketId);

                                    if (ticketToAdd != null) {
                                        controller.addTicketToCart(ticketToAdd);
                                        System.out.println("Ticket added to shopping cart.");
                                    } else {
                                        System.out.println("Ticket not found.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid Ticket ID.");
                                }
                            }
                        }
                    } else {
                        System.out.println("Event not found.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Event ID.");
                }
            }

            // Mark performer as favourite
            System.out.print("Would you like to mark " + name + " as a favourite? (yes/no): ");
            String response = scanner.nextLine();

            if ("yes".equalsIgnoreCase(response)) {
                FavouriteEntity favouriteItem = (FavouriteEntity) performer;
                controller.addFavourite(favouriteItem);
                System.out.println(name + " has been added to your favourites.");
            }
        }
    }

    /**
     * Searches for a performer (artist or athlete) by name, and if found, calls the helper method
     * {@link #upcomingEventsTicketsToShoppingCartMarkAsFavourite(Scanner, Controller, Object)} to
     * display their upcoming events, allow the user to view tickets, add them to their shopping cart,
     * and mark the performer as a favourite.
     *
     * @param scanner the scanner to read user input
     * @param controller the controller to interact with application data
     */
    private static void handleSearchArtistsAndAthletes(Scanner scanner, Controller controller) {
        System.out.print("Enter artist or athlete name: ");
        String name = scanner.nextLine();

        Artist artist = controller.findArtistByName(name);
        if (artist != null) {
            upcomingEventsTicketsToShoppingCartMarkAsFavourite(scanner, controller, artist);
            return;
        }

        Athlete athlete = controller.findAthleteByName(name);
        if (athlete != null) {
            upcomingEventsTicketsToShoppingCartMarkAsFavourite(scanner, controller, athlete);
        } else {
            System.out.println("No artist or athlete found with that name.");
        }
    }


    /**
     * Searches for events based on a specified location or venue name. If events are found,
     * displays them with an option to view tickets for a selected event and add them to the shopping cart.
     * @param scanner the scanner to read user input
     * @param controller the controller to search events by location and manage ticket operations
     */
    private static void handleViewEventsByLocation(Scanner scanner, Controller controller) {
        System.out.print("Enter location/venue name: ");
        String location = scanner.nextLine();
        List<Event> events = controller.getEventsByLocation(location);

        if (events.isEmpty()) {
            System.out.println("No events found for this location.");
        } else {
            System.out.println("Events at " + location + ":");
            events.forEach(System.out::println);
        }

        // Prompt for Event ID to view tickets or skip
        System.out.print("Enter Event ID to view tickets (or press Enter to skip): ");
        String eventIdInput = scanner.nextLine().trim();

        if (!eventIdInput.isEmpty()) {
            try {
                int eventId = Integer.parseInt(eventIdInput);
                Event selectedEvent = controller.findEventByID(eventId);

                if (selectedEvent != null) {
                    List<Ticket> tickets = controller.getTicketsByEvent(eventId);

                    if (tickets.isEmpty()) {
                        System.out.println("No tickets available for this event.");
                    } else {
                        System.out.println("Available tickets for event " + selectedEvent.getEventName() + ":");
                        tickets.forEach(System.out::println);

                        // Prompt for Ticket ID to add to shopping cart
                        System.out.print("Enter Ticket ID to add to shopping cart: ");
                        String ticketIdInput = scanner.nextLine().trim();

                        if (!ticketIdInput.isEmpty()) {
                            try {
                                int ticketId = Integer.parseInt(ticketIdInput);
                                Ticket ticketToAdd = controller.getTicketByID(ticketId);

                                if (ticketToAdd != null) {
                                    controller.addTicketToCart(ticketToAdd);
                                    System.out.println("Ticket added to shopping cart.");
                                } else {
                                    System.out.println("Ticket not found.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid Ticket ID.");
                            }
                        }
                    }
                } else {
                    System.out.println("Event not found.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Event ID.");
            }
        }
    }

    /**
     * Displays a list of suggested events to the user based on their favourites.
     * The method checks whether the favourite entities are artists or athletes,
     * and fetches events related to them and their related counterparts (e.g., artists with the same genre).
     *
     * @param scanner The Scanner instance used for capturing user input (not used in this method but passed for consistency).
     * @param controller The Controller instance used to access services for fetching suggested events.
     */

    private static void handleViewSuggestedEvents(Scanner scanner, Controller controller) {
        System.out.println("==== Suggested Events ====");
        Set<FavouriteEntity> favourites = controller.getFavourites();
        List<Event> suggestedEvents = new ArrayList<>();

        for (FavouriteEntity entity : favourites) {
            if (entity instanceof Artist) {
                Artist artist = (Artist) entity;
                suggestedEvents.addAll(controller.getUpcomingEventsForArtist(artist.getID()));
                List<Artist> relatedArtists = controller.findArtistsByGenre(artist.getGenre());
                for (Artist related : relatedArtists) {
                    if (!related.equals(artist)) {
                        suggestedEvents.addAll(controller.getUpcomingEventsForArtist(related.getID()));
                    }
                }
            } else if (entity instanceof Athlete) {
                Athlete athlete = (Athlete) entity;
                suggestedEvents.addAll(controller.getUpcomingEventsForAthlete(athlete.getID()));
                List<Athlete> relatedAthletes = controller.findAthletesBySport(athlete.getAthleteSport());
                for (Athlete related : relatedAthletes) {
                    if (!related.equals(athlete)) {
                        suggestedEvents.addAll(controller.getUpcomingEventsForAthlete(related.getID()));
                    }
                }
            }
        }

        if (suggestedEvents.isEmpty()) {
            System.out.println("No suggested events found.");
        } else {
            System.out.println("Here are some events you might be interested in:");
            suggestedEvents.forEach(System.out::println);
        }
    }

    /**
     * Handles the Shopping Cart management menu, allowing the user to view the contents of their cart
     * and remove items from it. The menu provides the following options:
     * <ul>
     *     <li>View Cart: Displays all tickets currently in the shopping cart.</li>
     *     <li>Remove Ticket from Cart: Allows the user to specify a ticket ID to remove from the cart.</li>
     *     <li>Back to Customer Menu: Exits the shopping cart menu and returns to the main customer menu.</li>
     * </ul>
     *
     * @param scanner    The {@code Scanner} object used to read user input from the console.
     * @param controller The {@code Controller} object, which provides access to methods for viewing and modifying
     *                   the shopping cart. This includes retrieving the current user's shopping cart items,
     *                   as well as removing tickets by ID.
     */
    private static void handleManageShoppingCart(Scanner scanner, Controller controller) {
        boolean inCartMenu = true;
        while (inCartMenu) {
            System.out.println("==== Shopping Cart ====");
            System.out.println("1. View Cart");
            System.out.println("2. Remove Ticket from Cart");
            System.out.println("0. Back to Customer Menu");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<Ticket> cartItems = ((Customer) controller.getCurrentUser()).getShoppingCart().getItems();
                    if (cartItems.isEmpty()) {
                        System.out.println("Your shopping cart is empty.");
                    } else {
                        cartItems.forEach(System.out::println);
                    }
                    break;
                case "2":
                    System.out.print("Enter Ticket ID to remove: ");
                    int ticketToRemoveId = Integer.parseInt(scanner.nextLine());
                    Ticket ticketToRemove = controller.getTicketByID(ticketToRemoveId);
                    if (ticketToRemove != null) {
                        controller.removeTicketFromCart(ticketToRemove);
                    } else {
                        System.out.println("Ticket not found in the cart.");
                    }
                    break;
                case "0":
                    inCartMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Handles the checkout process for the current customer's shopping cart. This method guides the user through:
     * <ul>
     *     <li>Checking if the shopping cart is empty. If so, the checkout is aborted.</li>
     *     <li>Prompting the user for payment details, including card number, CVV, card owner name, and expiration date.</li>
     *     <li>Validating and processing the payment using a {@code PaymentProcessor} implementation.</li>
     *     <li>If payment is successful, creating an order with all items in the shopping cart and displaying a success message.</li>
     * </ul>
     *
     * The checkout process is completed only if the payment details are valid and the payment transaction succeeds.
     *
     * @param scanner    The {@code Scanner} object used to read user input, including payment details.
     * @param controller The {@code Controller} object used to interact with the shopping cart, including calculating
     *                   the total price, checking out the cart, and creating the order upon successful payment.
     */
    private static void handleCheckout(Scanner scanner, Controller controller) {
        // Check if the shopping cart is empty
        Customer currentCustomer = (Customer) controller.getCurrentUser();
        if (currentCustomer.getShoppingCart().getItems().isEmpty()) {
            System.out.println("Your shopping cart is empty. Add items before checking out.");
            return;
        }

        // Prompt for payment details
        System.out.println("Proceeding to checkout...");
        System.out.print("Enter card number: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Enter CVV: ");
        int cvv = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter card owner name: ");
        String cardOwner = scanner.nextLine();
        System.out.print("Enter expiration date (MM/YY): ");
        String expirationDate = scanner.nextLine();

        // Initialize the PaymentProcessor
        PaymentProcessor paymentProcessor = new BasicPaymentProcessor();

        // Validate and process payment
        if (!paymentProcessor.enterPaymentDetails(cardNumber, cvv, cardOwner, expirationDate)) {
            System.out.println("Payment details are invalid. Please try again.");
            return;
        }
        double totalAmount = controller.getTotalPrice(); // Assuming controller has this method to fetch cart's total price
        if (!paymentProcessor.processPayment(totalAmount)) {
            System.out.println("Payment failed. Please check your payment information or try a different card.");
            return;
        }

        // Perform checkout
        Order order = controller.checkout();
        if (order != null) {
            System.out.println("Checkout completed. Order created successfully: " + order);
        } else {
            System.out.println("Checkout failed. Please try again.");
        }
    }

    /**
     * Displays the previous orders for the current user (customer). This method retrieves and displays
     * the order history, including details for each order such as order ID, items, order date, and status.
     * <ul>
     *     <li>If there are previous orders, they are printed to the console.</li>
     *     <li>If there are no previous orders, a message is displayed indicating this.</li>
     * </ul>
     *
     * @param controller The {@code Controller} object used to retrieve the order history of the current customer.
     *                   This includes accessing the list of {@code Order} objects associated with the customer.
     */
    private static void handleViewPreviousOrders(Controller controller) {
        System.out.println("==== Previous Orders ====");
        controller.getOrderHistory((Customer) controller.getCurrentUser());
    }

    /**
     * Displays a menu for managing the user's favourites. The user can view their favourites,
     * delete a specific favourite, or go back to the customer menu. The method handles input
     * validation and interaction with the controller to update the user's favourite list.
     *
     * @param scanner The Scanner instance used for capturing user input.
     * @param controller The Controller instance used to access services for fetching and modifying the user's favourites.
     */
    private static void handleManageFavourites(Scanner scanner, Controller controller) {
        boolean inFavouritesMenu = true;
        while (inFavouritesMenu) {
            System.out.println("==== Manage Favourites ====");
            System.out.println("1. View Favourites");
            System.out.println("2. Delete Favourite");
            System.out.println("0. Back to Customer Menu");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    Set<FavouriteEntity> favourites = controller.getFavourites();
                    if (favourites.isEmpty()) {
                        System.out.println("You have no favourites.");
                    } else {
                        favourites.forEach(fav -> System.out.println("- " + fav.getName()));
                    }
                    break;
                case "2":
                    favourites = controller.getFavourites();
                    if (favourites.isEmpty()) {
                        System.out.println("You have no favourites to delete.");
                        break;
                    }
                    System.out.print("Enter the name of the favourite to delete: ");
                    String favouriteName = scanner.nextLine();
                    FavouriteEntity itemToDelete = favourites.stream()
                            .filter(fav -> fav.getName().equalsIgnoreCase(favouriteName))
                            .findFirst()
                            .orElse(null);
                    if (itemToDelete != null) {
                        controller.removeFavourite(itemToDelete);
                        System.out.println(favouriteName + " has been removed from your favourites.");
                    } else {
                        System.out.println("No favourite found with that name.");
                    }
                    break;
                case "0":
                    inFavouritesMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void recommendSeat(Scanner scanner, Controller controller) {
        // Fetch the current customer
        Customer customer = (Customer) controller.getCurrentUser();

        // Get the venue from the user
        System.out.print("Enter Venue ID: ");
        int venueId = Integer.parseInt(scanner.nextLine());
        Venue venue = controller.findVenueById(venueId);

        if (venue == null) {
            System.out.println("Venue not found. Please try again.");
            return;
        }

        // Get the event from the user
        System.out.print("Enter Event ID: ");
        int eventId = Integer.parseInt(scanner.nextLine());
        Event event = controller.findEventByID(eventId);

        if (event == null) {
            System.out.println("Event not found. Please try again.");
            return;
        }

        // Recommend a seat
        Seat recommendedSeat = controller.recommendSeat(customer, venue, event);

        // Display the result
        if (recommendedSeat != null) {
            System.out.println("Recommended Seat:");
            System.out.println("Row: " + recommendedSeat.getRowNumber() + ", Seat: " + recommendedSeat.getID());
        } else {
            System.out.println("No preferred seat available for this event.");
        }
    }

}