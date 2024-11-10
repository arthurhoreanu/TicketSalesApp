package presentation.actions;

import controller.Controller;
import model.Artist;

import java.util.List;
import java.util.Scanner;

public class ArtistAction {

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
        } else {
            for (Artist artist : artists) {
                System.out.println(artist);
            }
        }

        System.out.print("Enter artist ID to update: ");
        int artistID = Integer.parseInt(scanner.nextLine());

        Artist artist = controller.findArtistByID(artistID);
        if (artist == null) {
            System.out.println("Artist not found.");
            return;
        }

        System.out.print("Enter new artist name (or press Enter to keep current name): ");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            artist.setArtistName(newName);
        }

        System.out.print("Enter new genre (or press Enter to keep current genre): ");
        String newGenre = scanner.nextLine();
        if (!newGenre.trim().isEmpty()) {
            artist.setGenre(newGenre);
        }

        controller.updateArtist(artistID, newName, newGenre);
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
