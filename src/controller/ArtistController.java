package controller;

import model.Artist;
import model.Event;
import service.ArtistService;

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
     * Displays a success message if the artist is added successfully, otherwise displays a failure message.
     * @param artistName The name of the artist to add.
     * @param genre The genre associated with the artist.
     */
    public void createArtist(String artistName, String genre) {
        int artistID = artistService.getAllArtists().size() + 1; // Generate unique ID based on the current size
        boolean success = artistService.createArtist(artistName, genre);
        if (success) {
            System.out.println("Artist added successfully.");
        } else {
            System.out.println("Failed to add artist.");
        }
    }

    /**
     * Updates the details of an existing artist specified by the artist ID.
     * Displays a success message if the update is successful, or an error message if the artist was not found or could not be updated.
     * @param artistId The ID of the artist to update.
     * @param newName The new name for the artist.
     * @param newGenre The new genre for the artist.
     */
    public void updateArtist(int artistId, String newName, String newGenre) {
        boolean success = artistService.updateArtist(artistId, newName, newGenre);
        if (success) {
            System.out.println("Artist updated successfully.");
        } else {
            System.out.println("Artist not found or could not be updated.");
        }
    }

    /**
     * Deletes an artist with the specified ID.
     * Displays a success message if the deletion is successful, or an error message if the artist was not found or could not be deleted.
     * @param artistId The ID of the artist to delete.
     */
    public void deleteArtist(int artistId) {
        boolean success = artistService.deleteArtist(artistId);
        if (success) {
            System.out.println("Artist deleted successfully.");
        } else {
            System.out.println("Artist not found or could not be deleted.");
        }
    }

    /**
     * Retrieves a list of all artists in the system.
     * @return A list of all artists.
     */
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    /**
     * Searches for an artist by name.
     * @param artistName The name of the artist to search for.
     * @return The artist if found, or null if no artist matches the specified name.
     */
    public Artist findArtistByName(String artistName) {
        return artistService.findArtistByName(artistName);
    }

    /**
     * Retrieves an artist by their ID.
     * @param artistID The ID of the artist to retrieve.
     * @return The artist with the specified ID, or null if no artist is found.
     */
    public Artist findArtistByID(int artistID) {
        return artistService.findArtistByID(artistID);
    }

    /**
     * Retrieves a list of events associated with a specific artist.
     * @param artist The artist for whom to retrieve events.
     * @return A list of events that feature the specified artist.
     */
    public List<Event> getEventsByArtist(Artist artist) {
        return artistService.getEventsByArtist(artist);
    }

    /**
     * Finds artists that match a specified genre.
     * @param genre The genre to filter artists by.
     * @return A list of artists who perform in the specified genre.
     */
    public List<Artist> findArtistsByGenre(String genre) {
        return artistService.findArtistsByGenre(genre);
    }
}
