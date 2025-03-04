package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.model.event.Artist;
import main.java.com.ticketsalesapp.service.ArtistService;

import java.util.List;

public class ArtistController {
    private final ArtistService artistService;

    /**
     * Constructor for ArtistController.
     * @param artistService The instance of ArtistService used to handle artist operations.
     */
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    /**
     * Creates a new artist with the specified name and genre.
     * @param artistName The name of the artist to add.
     * @param genre The genre associated with the artist.
     * @return true if the artist was successfully created, false otherwise.
     */
    public boolean createArtist(String artistName, String genre) {
        boolean success = artistService.createArtist(artistName, genre);
        if (success) {
            System.out.println("Artist added successfully.");
        } else {
            System.out.println("Failed to add artist.");
        }
        return success;
    }

    /**
     * Updates the details of an existing artist specified by the artist ID.
     * @param artistId The ID of the artist to update.
     * @param newName The new name for the artist.
     * @param newGenre The new genre for the artist.
     * @return true if the artist was successfully updated, false otherwise.
     */
    public boolean updateArtist(int artistId, String newName, String newGenre) {
        boolean success = artistService.updateArtist(artistId, newName, newGenre);
        if (success) {
            System.out.println("Artist updated successfully.");
        } else {
            System.out.println("Artist not found or could not be updated.");
        }
        return success;
    }

    /**
     * Deletes an artist with the specified ID.
     * @param artistId The ID of the artist to delete.
     * @return true if the artist was successfully deleted, false otherwise.
     */
    public boolean deleteArtist(int artistId) {
        boolean success = artistService.deleteArtist(artistId);
        if (success) {
            System.out.println("Artist deleted successfully.");
        } else {
            System.out.println("Artist not found or could not be deleted.");
        }
        return success;
    }

    /**
     * Retrieves a list of all artists in the system.
     * @return A list of all artists.
     */
    public List<Artist> getAllArtists() {
        List<Artist> artists = artistService.getAllArtists();
        if (!artists.isEmpty()) {
            System.out.println("All Artists:");
            artists.forEach(System.out::println);
        } else {
            System.out.println("No artists found.");
        }
        return artists;
    }

    /**
     * Searches for an artist by name.
     * @param artistName The name of the artist to search for.
     * @return The artist if found, or null if no artist matches the specified name.
     */
    public Artist findArtistByName(String artistName) {
        Artist artist = artistService.findArtistByName(artistName);
        if (artist != null) {
            System.out.println("Artist found: " + artist);
        } else {
            System.out.println("Artist with name '" + artistName + "' not found.");
        }
        return artist;
    }

    /**
     * Retrieves an artist by their ID.
     * @param artistID The ID of the artist to retrieve.
     * @return The artist with the specified ID, or null if no artist is found.
     */
    public Artist findArtistByID(int artistID) {
        Artist artist = artistService.findArtistByID(artistID);
        if (artist != null) {
            System.out.println("Artist found: " + artist);
        } else {
            System.out.println("Artist with ID " + artistID + " not found.");
        }
        return artist;
    }

    /**
     * Finds artists that match a specified genre.
     * @param genre The genre to filter artists by.
     * @return A list of artists who perform in the specified genre.
     */
    public List<Artist> findArtistsByGenre(String genre) {
        List<Artist> artists = artistService.findArtistsByGenre(genre);
        if (!artists.isEmpty()) {
            System.out.println("Artists in genre '" + genre + "':");
            artists.forEach(System.out::println);
        } else {
            System.out.println("No artists found in genre '" + genre + "'.");
        }
        return artists;
    }
}
