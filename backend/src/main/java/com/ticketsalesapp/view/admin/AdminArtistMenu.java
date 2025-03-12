package main.java.com.ticketsalesapp.view.admin;

import main.java.com.ticketsalesapp.controller.ArtistController;
import main.java.com.ticketsalesapp.exception.EntityNotFoundException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.Artist;
import main.java.com.ticketsalesapp.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Provides a menu for admins to manage artists, including creating, viewing, updating, and deleting artists.
 */
@Component
public class AdminArtistMenu {

    private final ArtistController artistController;

    public AdminArtistMenu(ArtistController artistController) {
        this.artistController = artistController;
    }

    /**
     * Displays the artist management menu and processes the selected options.
     * @param scanner the scanner to read user input
     */
    public void display(Scanner scanner) {
        boolean inArtistMenu = true;
        while (inArtistMenu) {
            try {
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
                        handleCreateArtist(scanner);
                        break;
                    case "2":
                        handleViewArtists();
                        break;
                    case "3":
                        handleUpdateArtist(scanner);
                        break;
                    case "4":
                        handleDeleteArtist(scanner);
                        break;
                    case "0":
                        inArtistMenu = false;
                        break;
                    default:
                        throw new ValidationException("Invalid option. Please select a number between 0 and 4.");
                }
                System.out.println();
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handles the creation of a new artist.
     * @param scanner the scanner to read user input
     */
    public void handleCreateArtist(Scanner scanner) {
        try {
            System.out.println("=== Create Artist ===");
            System.out.print("Enter artist name: ");
            String artistName = scanner.nextLine();
            if (artistName.isEmpty()) {
                throw new ValidationException("Artist name cannot be empty.");
            }
            System.out.print("Enter artist genre: ");
            String genre = scanner.nextLine();
            if (genre.isEmpty()) {
                throw new ValidationException("Artist genre cannot be empty.");
            }
            artistController.createArtist(artistName, genre);
            System.out.println("Artist created successfully!");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays a list of all artists.
     */
    public void handleViewArtists() {
        System.out.println("=== View Artists ===");
        artistController.displayAllArtists();
    }

    /**
     * Handles updating an existing artist with new information.
     * @param scanner the scanner to read user input
     */
    public  void handleUpdateArtist(Scanner scanner) {
        try {
            System.out.println("=== Update Artist ===");
            artistController.displayAllArtists();
            System.out.print("Enter artist ID to update: ");
            int artistID = Integer.parseInt(scanner.nextLine());
            Artist artist = artistController.findArtistById(artistID);
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
            artistController.updateArtist(artistID, newName, newGenre);
            System.out.println("Artist updated successfully.");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles deleting an artist by their ID.
     * @param scanner the scanner to read user input
     */
    public  void handleDeleteArtist(Scanner scanner) {
        System.out.println("=== Delete Artist ===");
        artistController.displayAllArtists();
        System.out.print("Enter artist ID to delete: ");
        int artistID;
        try {
            artistID = Integer.parseInt(scanner.nextLine());
            artistController.deleteArtist(artistID);
            System.out.println("Artist deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid artist ID. Please enter a valid number.");
        }
    }
}