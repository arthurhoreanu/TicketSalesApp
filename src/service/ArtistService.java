package service;

import model.*;
import repository.IRepository;

import java.util.ArrayList;
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
    public boolean createArtist(String artistName, String genre) {
        int newID = artistRepository.getAll().size() + 1;  // Generate unique ID based on the current size
        Artist artist = new Artist(newID, artistName, genre);
        artistRepository.create(artist);
        return true;
    }

    // Method to update an existing artist
    public boolean updateArtist(int artistId, String newName, String newGenre) {
        Artist artist = findArtistByID(artistId);
        if (artist != null) {
            artist.setArtistName(newName);
            artist.setGenre(newGenre);
            artistRepository.update(artist);
            return true;
        } else {
            return false;
        }
    }

    // Method to delete an artist by ID
    public boolean deleteArtist(int artistId) {
        Artist artist = findArtistByID(artistId);
        if (artist != null) {
            artistRepository.delete(artistId);
            return true;
        } else {
            return false;
        }
    }

    // Retrieves a list of all artists
    public List<Artist> getAllArtists() {
        return artistRepository.getAll();
    }

    public Artist findArtistByID(int artistID) {
        return artistRepository.getAll().stream().filter(artist -> artist.getID() == artistID).findFirst().orElse(null);
    }

    // Finds an artist by name
    public Artist findArtistByName(String artistName) {
        return artistRepository.getAll().stream().filter(artist -> artist.getArtistName().equalsIgnoreCase(artistName)).findFirst().orElse(null);
    }

    // Method to get a list of events for a specific artist (implements showEventList)
    public List<Event> getEventsByArtist(Artist artist) {
        return eventRepository.getAll().stream().filter(event -> event instanceof Concert)  // Filter only Concert events
                .filter(event -> ((Concert) event).getArtists().equals(artist))  // Match the artist
                .collect(Collectors.toList());
    }

    public List<Artist> findArtistsByGenre(String genre) {
        List<Artist> artistsInGenre = new ArrayList<>();
        for (Artist artist : artistRepository.getAll()) {
            if (artist.getGenre().equalsIgnoreCase(genre)) {
                artistsInGenre.add(artist);
            }
        }
        return artistsInGenre;
    }
}
