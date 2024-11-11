package presentation.menus;

import controller.Controller;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
                handleViewSuggestedEvents(scanner, controller);
                break;
            case "3":
                handleSearchArtistsAndAthletes(scanner, controller);
                break;
            case "4":
                handleViewEventsByLocation(scanner, controller);
                break;
            case "5":
                // TODO: not a priority right now, order needs to work first=
                //handleViewPreviousOrders(controller);
                break;
            case "6":
                handleManageFavourites(scanner, controller);
                break;
            case "0":
                System.out.println("Exiting the application. Goodbye!");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }

    private static void handleViewSuggestedEvents(Scanner scanner, Controller controller) {
        System.out.println("==== Suggested Events ====");

        // Step 1: Retrieve favorites
        Set<FavouriteEntity> favourites = controller.getFavourites();
        List<Event> suggestedEvents = new ArrayList<>();

        // Step 2: Process each favorite item (either Artist or Athlete)
        for (FavouriteEntity entity : favourites) {
            if (entity instanceof Artist favoriteArtist) {
                // Add upcoming events of the favorite artist
                suggestedEvents.addAll(controller.getUpcomingEventsForArtist(favoriteArtist.getID()));

                // Find other artists in the same genre and add their upcoming events
                List<Artist> relatedArtists = controller.findArtistsByGenre(favoriteArtist.getGenre());
                for (Artist artist : relatedArtists) {
                    if (!artist.equals(favoriteArtist)) { // Avoid duplicates
                        suggestedEvents.addAll(controller.getUpcomingEventsForArtist(artist.getID()));
                    }
                }
            } else if (entity instanceof Athlete favoriteAthlete) {
                // Add upcoming events of the favorite athlete
                suggestedEvents.addAll(controller.getUpcomingEventsForAthlete(favoriteAthlete.getID()));

                // Find other athletes in the same sport and add their upcoming events
                List<Athlete> relatedAthletes = controller.findAthletesBySport(favoriteAthlete.getAthleteSport());
                for (Athlete athlete : relatedAthletes) {
                    if (!athlete.equals(favoriteAthlete)) { // Avoid duplicates
                        suggestedEvents.addAll(controller.getUpcomingEventsForAthlete(athlete.getID()));
                    }
                }
            }
        }

        // Step 3: Display suggested events
        if (suggestedEvents.isEmpty()) {
            System.out.println("No suggested events found.");
        } else {
            System.out.println("Here are some events you might be interested in:");
            for (Event event : suggestedEvents) {
                System.out.println(event);
            }
        }
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
    }

    //    private static void handleViewPreviousOrders(Controller controller) {
//        List<Order> previousOrders = controller.getPreviousOrders();
//        if (previousOrders.isEmpty()) {
//            System.out.println("No previous orders found.");
//        } else {
//            System.out.println("Your previous orders:");
//            previousOrders.forEach(System.out::println);
//        }
//    }

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
                FavouriteEntity favoriteItem = (FavouriteEntity) performer;
                controller.addFavorite(favoriteItem); // Pass the actual FavoriteItem object
            }
        }
    }

    private static void handleManageFavourites(Scanner scanner, Controller controller) {
        boolean inFavoritesMenu = true;
        while (inFavoritesMenu) {
            System.out.println("==== Manage Favorites ====");
            System.out.println("1. View Favorites");
            System.out.println("2. Delete Favorite");
            System.out.println("0. Back to Customer Menu");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": // View Favorites
                    Set<FavouriteEntity> favorites = controller.getFavourites();
                    if (favorites.isEmpty()) {
                        System.out.println("You have no favorites.");
                    } else {
                        System.out.println("Favorite Artists/Athletes:");
                        favorites.forEach(favorite -> System.out.println("- " + favorite.getName()));
                    }
                    break;

                case "2": // Delete Favorite
                    favorites = controller.getFavourites();
                    if (favorites.isEmpty()) {
                        System.out.println("You have no favorites to delete.");
                        break;
                    }

                    // Display all favorites before deletion
                    System.out.println("Favorite Artists/Athletes:");
                    favorites.forEach(favorite -> System.out.println("- " + favorite.getName()));

                    System.out.print("Enter the name of the favorite to delete: ");
                    String favoriteName = scanner.nextLine();

                    // Find the favorite by name
                    FavouriteEntity itemToDelete = favorites.stream()
                            .filter(favorite -> favorite.getName().equalsIgnoreCase(favoriteName))
                            .findFirst()
                            .orElse(null);

                    if (itemToDelete != null) {
                        controller.removeFavorite(itemToDelete);
                        System.out.println(favoriteName + " has been removed from your favorites.");
                    } else {
                        System.out.println("No favorite found with that name.");
                    }
                    break;

                case "0": // Back to Customer Menu
                    inFavoritesMenu = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}