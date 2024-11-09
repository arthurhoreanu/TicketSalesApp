package controller;

import model.Artist;
import model.Event;
import service.ArtistService;

import java.util.List;

public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    // Method to add a new artist
    public void addArtist(String artistName, String genre) {
        int artistID = artistService.getAllArtists().size() + 1; // Generate unique ID based on the current size
        boolean success = artistService.createArtist(artistName, genre);
        if (success) {
            System.out.println("Artist added successfully.");
        } else {
            System.out.println("Failed to add artist.");
        }
    }

    // Method to update an existing artist
    public void updateArtist(int artistId, String newName, String newGenre) {
        boolean success = artistService.updateArtist(artistId, newName, newGenre);
        if (success) {
            System.out.println("Artist updated successfully.");
        } else {
            System.out.println("Artist not found or could not be updated.");
        }
    }

    // Method to delete an artist by ID
    public void deleteArtist(int artistId) {
        boolean success = artistService.deleteArtist(artistId);
        if (success) {
            System.out.println("Artist deleted successfully.");
        } else {
            System.out.println("Artist not found or could not be deleted.");
        }
    }

    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    public Artist findArtistByName(String artistName) {
        return artistService.findArtistByName(artistName);
    }

    public List<Event> getEventsByArtist(Artist artist) {
        return artistService.getEventsByArtist(artist);
    }
}
