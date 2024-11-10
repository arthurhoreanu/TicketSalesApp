package presentation.menus;

import controller.Controller;
import model.*;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Customer Menu ====");
        System.out.println("1. Logout");
        System.out.println("2. View Suggested Events");
        System.out.println("3. Search for Artists/Athletes");
        System.out.println("4. Search for Locations/Venues");
        System.out.println("5. View Previous Orders");
        System.out.println("6. Manage Favorites");
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
                // Arthur's TODO: depends on other methods
                //handleViewSuggestedEvents(scanner, controller);
                break;
            case "3":
                handleSearchArtistsAndAthletes(scanner, controller);
                break;
            case "4":
                //handleViewEventsByLocation(scanner, controller);
                break;
            case "5":
                //handleViewPreviousOrders(controller);
                break;
            case "6":
                //handleManageFavorites(scanner, controller);
                break;
            case "0":
                System.out.println("Exiting the application. Goodbye!");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }

    private static void handleSearchArtistsAndAthletes(Scanner scanner, Controller controller) {
        System.out.print("Enter artist or athlete name: ");
        String name = scanner.nextLine();

        // First, try to find the name as an artist
        Artist artist = controller.findArtistByName(name);
        if (artist != null) {
            upcomingEventsAndMarkAsFavourite(scanner, controller, artist);
            return;
        }

        // If not found as an artist, try to find as an athlete
        Athlete athlete = controller.findAthleteByName(name);
        if (athlete != null) {
            upcomingEventsAndMarkAsFavourite(scanner, controller, athlete);
        } else {
            System.out.println("No artist or athlete found with that name.");
        }
    }

    // Helper method to display events and ask to mark as favorite
    private static void upcomingEventsAndMarkAsFavourite(Scanner scanner, Controller controller, Object performer) {
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

            System.out.print("Would you like to mark " + name + " as a favorite? (yes/no): ");
            String response = scanner.nextLine();

            if ("yes".equalsIgnoreCase(response)) {
                // The `performer` object is already an instance of either Artist, Athlete, or Event
                FavouriteItem favoriteItem = (FavouriteItem) performer;
                controller.addFavorite(favoriteItem); // Pass the actual FavoriteItem object
            }
        }

//    private static void handleViewEventsByLocation(Scanner scanner, Controller controller) {
//        System.out.print("Enter location name: ");
//        String location = scanner.nextLine();
//        List<Event> events = controller.getEventsByLocation(location);
//
//        if (events.isEmpty()) {
//            System.out.println("No events found for this location.");
//        } else {
//            System.out.println("Events at " + location + ":");
//            events.forEach(System.out::println);
//        }
//    }
//
//    private static void handleViewPreviousOrders(Controller controller) {
//        List<Order> previousOrders = controller.getPreviousOrders();
//        if (previousOrders.isEmpty()) {
//            System.out.println("No previous orders found.");
//        } else {
//            System.out.println("Your previous orders:");
//            previousOrders.forEach(System.out::println);
//        }
//    }
//
//    private static void handleManageFavorites(Scanner scanner, Controller controller) {
//        boolean inFavoritesMenu = true;
//        while (inFavoritesMenu) {
//            System.out.println("==== Manage Favorites ====");
//            System.out.println("1. Add Artist/Athlete to Favorites");
//            System.out.println("2. Add Event to Favorites");
//            System.out.println("3. View Favorite Artists/Athletes");
//            System.out.println("4. View Favorite Events");
//            System.out.println("0. Back to Customer Menu");
//            System.out.print("Choose an option: ");
//            String choice = scanner.nextLine();
//
//            switch (choice) {
//                case "1":
//                    System.out.print("Enter artist/athlete name to favorite: ");
//                    String artistName = scanner.nextLine();
//                    controller.addFavoriteArtist(artistName);
//                    System.out.println("Added " + artistName + " to favorites.");
//                    break;
//                case "2":
//                    System.out.print("Enter event name to favorite: ");
//                    String eventName = scanner.nextLine();
//                    controller.addFavoriteEvent(eventName);
//                    System.out.println("Added " + eventName + " to favorites.");
//                    break;
//                case "3":
//                    List<Artist> favoriteArtists = controller.getFavoriteArtists();
//                    System.out.println("Favorite Artists/Athletes:");
//                    favoriteArtists.forEach(System.out::println);
//                    break;
//                case "4":
//                    List<Event> favoriteEvents = controller.getFavoriteEvents();
//                    System.out.println("Favorite Events:");
//                    favoriteEvents.forEach(System.out::println);
//                    break;
//                case "0":
//                    inFavoritesMenu = false;
//                    break;
//                default:
//                    System.out.println("Invalid option. Please try again.");
//            }
//        }
//    }
    }
}
