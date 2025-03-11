package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.model.event.Artist;
import main.java.com.ticketsalesapp.service.ArtistService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
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
     */
    public void createArtist(String artistName, String genre) {
        artistService.createArtist(artistName, genre);
    }

    /**
     * Updates the details of an existing artist specified by the artist ID.
     * @param artistId The ID of the artist to update.
     * @param newName The new name for the artist.
     * @param newGenre The new genre for the artist.
     */
    public void updateArtist(int artistId, String newName, String newGenre) {
        artistService.updateArtist(artistId, newName, newGenre);
    }

    /**
     * Deletes an artist with the specified ID.
     * @param artistId The ID of the artist to delete.
     */
    public void deleteArtist(int artistId) {
        artistService.deleteArtist(artistId);
    }

    /**
     * Retrieves a list of all artists in the system.
     * @return A list of all artists.
     */
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    /**
     * Retrieves an artist by their ID.
     * @param artistID The ID of the artist to retrieve.
     * @return The artist with the specified ID, or null if no artist is found.
     */
    public Artist findArtistById(int artistID) {
        return artistService.findArtistById(artistID);
    }

    /**
     * Searches for an artist by name.
     * @param artistName The name of the artist to search for.
     * @return The artist if found, or null if no artist matches the specified name.
     */
    public Optional<Artist> findArtistByName(String artistName) {
        return artistService.findArtistByName(artistName);
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
