package service;

import model.*;
import repository.IRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistService {
    private final IRepository<Artist> artistRepository;
    private final IRepository<Event> eventRepository;

    public ArtistService(IRepository<Artist> artistRepository, IRepository<Event> eventRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
    }

    // Method to add a new artist
    public boolean createArtist(int artistId, String artistName, String genre) {
        artistId = artistRepository.getAll().size() + 1;  // Generate unique ID based on the current size
        Artist artist = new Artist(artistId, artistName, genre);
        artistRepository.create(artist);
        System.out.println("Artist added successfully.");
        return true;
    }

    // Method to update an existing artist
    public boolean updateArtist(int artistId, String newName, String newGenre) {
        Artist artist = findArtistById(artistId);
        if (artist != null) {
            artist.setArtistName(newName);
            artist.setGenre(newGenre);
            artistRepository.update(artist);
            System.out.println("Artist updated successfully.");
            return true;
        } else {
            System.out.println("Artist not found.");
            return false;
        }
    }

    // Method to delete an artist by ID
    public boolean deleteArtist(int artistId) {
        Artist artist = findArtistById(artistId);
        if (artist != null) {
            artistRepository.delete(artistId);
            System.out.println("Artist deleted successfully.");
            return true;
        } else {
            System.out.println("Artist not found.");
            return false;
        }
    }

    // Retrieves a list of all artists
    public List<Artist> getAllArtists() {
        return artistRepository.getAll();
    }

    private Artist findArtistById(int artistId) {
        return artistRepository.getAll().stream()
                .filter(artist -> artist.getID() == artistId)
                .findFirst()
                .orElse(null);
    }

    // Finds an artist by name
    public Artist findArtistByName(String artistName) {
        return artistRepository.getAll().stream()
                .filter(artist -> artist.getArtistName().equalsIgnoreCase(artistName))
                .findFirst()
                .orElse(null);
    }

    // Method to get a list of events for a specific artist (implements showEventList)
    public List<Event> getEventsByArtist(Artist artist) {
        return eventRepository.getAll().stream()
                .filter(event -> event instanceof Concert)  // Filter only Concert events
                .filter(event -> ((Concert) event).getArtist().equals(artist))  // Match the artist
                .collect(Collectors.toList());
    }
}
