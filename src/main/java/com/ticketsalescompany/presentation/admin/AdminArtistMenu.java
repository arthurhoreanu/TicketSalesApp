package com.ticketsalescompany.presentation.admin;

import com.ticketsalescompany.controller.Controller;
import com.ticketsalescompany.model.Artist;

import java.util.List;
import java.util.Scanner;

/**
 * Provides a menu for admins to manage artists, including creating, viewing, updating, and deleting artists.
 */
public class AdminArtistMenu {

    /**
     * Displays the artist management menu and processes the selected options.
     * @param scanner the scanner to read user input
     * @param controller the controller to handle artist management actions
     */
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

    /**
     * Handles the creation of a new artist.
     * @param scanner the scanner to read user input
     * @param controller the controller to manage artist creation
     */
    public static void handleCreateArtist(Scanner scanner, Controller controller) {
        System.out.println("=== Create Artist ===");
        System.out.print("Enter artist name: ");
        String artistName = scanner.nextLine();
        System.out.print("Enter artist genre: ");
        String genre = scanner.nextLine();
        controller.createArtist(artistName, genre);
    }

    /**
     * Displays a list of all artists.
     * @param controller the controller to retrieve artists from
     */
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

    /**
     * Handles updating an existing artist with new information.
     * @param scanner the scanner to read user input
     * @param controller the controller to manage artist updates
     */
    public static void handleUpdateArtist(Scanner scanner, Controller controller) {
        System.out.println("=== Update Artist ===");

        List<Artist> artists = controller.getAllArtists();
        if (artists.isEmpty()) {
            System.out.println("No artists available.");
            return;
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

        System.out.print("Enter new artist name (or press Enter to keep current name): ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) {
            newName = artist.getArtistName();
        }

        System.out.print("Enter new genre (or press Enter to keep current genre): ");
        String newGenre = scanner.nextLine().trim();
        if (newGenre.isEmpty()) {
            newGenre = artist.getGenre();
        }

        controller.updateArtist(artistID, newName, newGenre);
        System.out.println("Artist updated successfully.");
    }

    /**
     * Handles deleting an artist by their ID.
     * @param scanner the scanner to read user input
     * @param controller the controller to manage artist deletion
     */
    public static void handleDeleteArtist(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Artist ===");

        List<Artist> artists = controller.getAllArtists();
        if (artists.isEmpty()) {
            System.out.println("No artists available.");
            return;
        } else {
            for (Artist artist : artists) {
                System.out.println(artist);
            }
        }

        System.out.print("Enter artist ID to delete: ");
        int artistID;
        try {
            artistID = Integer.parseInt(scanner.nextLine());
            controller.deleteArtist(artistID);
            System.out.println("Artist deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid artist ID. Please enter a valid number.");
        }
    }
}
