package presentation;

import controller.Controller;
import model.*;

import java.util.*;

/**
 * Provides a menu for customers to manage their interactions within the application,
 * including viewing events, managing favourites, shopping carts, and orders.
 */
public class CustomerMenu {

    private static Controller controller;

    /**
     * Displays the customer menu and processes the selected options.
     *
     * @param scanner    the scanner to read user input
     * @param controller the controller to handle customer-related actions
     * @return {@code true} to keep displaying the menu; {@code false} to exit the application
     */
    public static boolean display(Scanner scanner, Controller controller) {
        while (true) {
            System.out.println("==== Customer Menu ====");
            System.out.println("1. Logout");
            System.out.println("2. Search for Artists/Athletes");
            System.out.println("3. Search for Locations/Venues");
            System.out.println("4. View Suggested Events");
            System.out.println("5. Manage Shopping Cart");
            System.out.println("6. Checkout Shopping Cart");
            System.out.println("7. View Previous Orders");
            System.out.println("8. Manage Favourites");
            System.out.println("0. Exit");
            System.out.println("=======================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    controller.logout();
                    System.out.println("Logged out successfully.");
                    return true; // Go back to login menu
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
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return false;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Searches for a performer (artist/athlete) and allows viewing upcoming events and tickets.
     */
    private static void handleSearchArtistsAndAthletes(Scanner scanner, Controller controller) {
        System.out.print("Enter the name of an artist or athlete: ");
        String name = scanner.nextLine();

        Artist artist = controller.findArtistByName(name);
        if (artist != null) {
            viewEventsAndManageTickets(scanner, controller, artist);
            return;
        }

        Athlete athlete = controller.findAthleteByName(name);
        if (athlete != null) {
            viewEventsAndManageTickets(scanner, controller, athlete);
        } else {
            System.out.println("No artist or athlete found with the name: " + name);
        }
    }

    /**
     * Handles searching events by location/venue and allows adding tickets to the cart.
     */
    private static void handleViewEventsByLocation(Scanner scanner, Controller controller) {
        System.out.print("Enter location or venue name: ");
        String location = scanner.nextLine();

        List<Event> events = controller.getEventsByLocation(location);
        if (events.isEmpty()) {
            System.out.println("No events found for the given location.");
            return;
        }

        System.out.println("Events found:");
        events.forEach(System.out::println);

        viewTicketsAndAddToCart(scanner, controller);
    }

    /**
     * Displays suggested events based on the user's favourites.
     */
    private static void handleViewSuggestedEvents(Scanner scanner, Controller controller) {
        System.out.println("==== Suggested Events ====");
        Set<FavouriteEntity> favourites = controller.getFavourites();

        if (favourites.isEmpty()) {
            System.out.println("No favourites to generate suggestions from.");
            return;
        }

        List<Event> suggestedEvents = new ArrayList<>();
        for (FavouriteEntity entity : favourites) {
            if (entity instanceof Artist artist) {
                suggestedEvents.addAll(controller.getUpcomingEventsForArtist(artist.getID()));
            } else if (entity instanceof Athlete athlete) {
                suggestedEvents.addAll(controller.getUpcomingEventsForAthlete(athlete.getID()));
            }
        }

        if (suggestedEvents.isEmpty()) {
            System.out.println("No suggested events found.");
        } else {
            System.out.println("Suggested Events:");
            suggestedEvents.forEach(System.out::println);
        }
    }

    /**
     * Manages shopping cart operations: view cart and remove tickets.
     */
    private static void handleManageShoppingCart(Scanner scanner, Controller controller) {
        while (true) {
            System.out.println("==== Shopping Cart ====");
            System.out.println("1. View Cart");
            System.out.println("2. Remove Ticket from Cart");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    ShoppingCart cart = ((Customer) controller.getCurrentUser()).getShoppingCart();
                    if (cart.getItems().isEmpty()) {
                        System.out.println("Your shopping cart is empty.");
                    } else {
                        cart.getItems().forEach(System.out::println); // todo methoda asta getItems cred ca se leaga de getTicketsByShoppingCart din shopping cart service, aia iti da lista, doar ca e facuta shi metoda
                    }
                    break;
                case "2":
                    System.out.print("Enter Ticket ID to remove: ");
                    try {
                        int ticketId = Integer.parseInt(scanner.nextLine());
                        Ticket ticket = controller.findTicketByID(ticketId);
                        if (ticket != null) {
                            controller.removeTicketFromCart(ticket);
                            System.out.println("Ticket removed from cart.");
                        } else {
                            System.out.println("Ticket not found in cart.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Ticket ID.");
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
     * Handles checkout of shopping cart.
     */
    private static void handleCheckout(Scanner scanner, Controller controller) {
        Customer currentCustomer = (Customer) controller.getCurrentUser();
        ShoppingCart cart = currentCustomer.getShoppingCart();

        if (cart.getItems().isEmpty()) {
            System.out.println("Your shopping cart is empty. Add tickets before checking out.");
            return;
        }

        System.out.println("Proceeding to checkout...");
        System.out.println("Total amount: $" + cart.getTotalPrice());
        System.out.print("Enter payment card number: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Enter CVV: ");
        String cvv = scanner.nextLine();

        System.out.println("Processing payment...");
        controller.orderAllTicketsFromCart(currentCustomer);
        System.out.println("Checkout successful. Order has been placed!");
    }

    /**
     * Displays previous orders for the user.
     */
    private static void handleViewPreviousOrders(Controller controller) {
        Customer currentCustomer = (Customer) controller.getCurrentUser();
        List<Order> orders = controller.getOrderHistory(currentCustomer);

        if (orders.isEmpty()) {
            System.out.println("No previous orders found.");
        } else {
            System.out.println("Previous Orders:");
            orders.forEach(System.out::println);
        }
    }
    /**
     * Handles managing user favourites.
     */
    private static void handleManageFavourites(Scanner scanner, Controller controller) {
        System.out.println("==== Favourites ====");
        Set<FavouriteEntity> favourites = controller.getFavourites();

        if (favourites.isEmpty()) {
            System.out.println("You have no favourites.");
        } else {
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

    /**
     * Common method to view tickets for an event and add them to the cart.
     */
    private static void viewTicketsAndAddToCart(Scanner scanner, Controller controller) {
        System.out.print("Enter Event ID to view tickets: ");
        try {
            int eventId = Integer.parseInt(scanner.nextLine());
            Event event = controller.findEventByID(eventId);

            if (event != null) {
                List<Ticket> tickets = controller.getTicketsByEvent(event);
                if (tickets.isEmpty()) {
                    System.out.println("No tickets available for this event.");
                } else {
                    System.out.println("Available Tickets:");
                    tickets.forEach(System.out::println);

                    System.out.print("Enter Ticket ID to add to cart: ");
                    int ticketId = Integer.parseInt(scanner.nextLine());
                    Ticket ticket = controller.findTicketByID(ticketId);

                    if (ticket != null) {
                        controller.addTicketToCart(event, ticket);
                        System.out.println("Ticket added to cart.");
                    } else {
                        System.out.println("Invalid Ticket ID.");
                    }
                }
            } else {
                System.out.println("Event not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void viewEventsAndManageTickets(Scanner scanner, Controller controller, Object performer) {
        if (performer instanceof Artist artist) {
            viewPerformerEvents(scanner, controller.getUpcomingEventsForArtist(artist.getID()));
        } else if (performer instanceof Athlete athlete) {
            viewPerformerEvents(scanner, controller.getUpcomingEventsForAthlete(athlete.getID()));
        }
    }

    private static void viewPerformerEvents(Scanner scanner, List<Event> events) {
        if (events.isEmpty()) {
            System.out.println("No upcoming events.");
            return;
        }
        events.forEach(System.out::println);
        viewTicketsAndAddToCart(scanner, controller); // Replace with valid controller
    }
}   // todo toate errorile tin de metoda aia cu getItems() si de rahatu asta de mai sus, da nu m am uitat atent