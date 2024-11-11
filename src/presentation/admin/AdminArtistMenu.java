package presentation.admin;

import controller.Controller;
import model.Artist;

import java.util.List;
import java.util.Scanner;

public class AdminArtistMenu {
    public static void display(Scanner scanner, Controller controller) {
        boolean inArtistMenu = true;
        while (inArtistMenu) {
            System.out.println("==== Artist Management ====");
            System.out.println("1. Create Artist");
            System.out.println("2. View Artists");
            System.out.println("3. Update Artist");
            System.out.println("4. Delete Artist");
            System.out.println("0. Back to Admin Menu");
            System.out.println("==========================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleCreateArtist(scanner, controller);
                    break;
                case "2":
                    handleViewArtists(controller);
                    break;
                case "3":
                    handleUpdateArtist(scanner, controller);
                    break;
                case "4":
                    handleDeleteArtist(scanner, controller);
                    break;
                case "0":
                    inArtistMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }

    public static void handleCreateArtist(Scanner scanner, Controller controller) {
        System.out.println("=== Create Artist ===");
        System.out.print("Enter artist name: ");
        String artistName = scanner.nextLine();
        System.out.print("Enter artist genre: ");
        String genre = scanner.nextLine();
        controller.createArtist(artistName, genre);
    }

    public static void handleViewArtists(Controller controller) {
        System.out.println("=== View Artists ===");
        List<Artist> artists = controller.getAllArtists();

        if (artists.isEmpty()) {
            System.out.println("No artists found.");
        } else {
            for (Artist artist : artists) {
                System.out.println(artist);
            }
        }
    }

    public static void handleUpdateArtist(Scanner scanner, Controller controller) {
        System.out.println("=== Update Artist ===");

        List<Artist> artists = controller.getAllArtists();
        if (artists.isEmpty()) {
            System.out.println("No artists available.");
            return; // Exit if there are no artists to update
        } else {
            for (Artist artist : artists) {
                System.out.println(artist);
            }
        }

        System.out.print("Enter artist ID to update: ");
        int artistID;
        try {
            artistID = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid artist ID.");
            return;
        }

        Artist artist = controller.findArtistByID(artistID);
        if (artist == null) {
            System.out.println("Artist not found.");
            return;
        }

        // Prompt for new values and keep current ones if input is empty
        System.out.print("Enter new artist name (or press Enter to keep current name): ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) {
            newName = artist.getArtistName(); // Keep current name if input is empty
        }

        System.out.print("Enter new genre (or press Enter to keep current genre): ");
        String newGenre = scanner.nextLine().trim();
        if (newGenre.isEmpty()) {
            newGenre = artist.getGenre(); // Keep current genre if input is empty
        }

        // Update artist through controller
        controller.updateArtist(artistID, newName, newGenre);
        System.out.println("Artist updated successfully.");
    }


    public static void handleDeleteArtist(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Artist ===");

        List<Artist> artists = controller.getAllArtists();
        if (artists.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Artist artist : artists) {
                System.out.println(artist);
            }
        }

        System.out.print("Enter artist ID to delete: ");
        int artistID = Integer.parseInt(scanner.nextLine());

        controller.deleteArtist(artistID);
    }
}
