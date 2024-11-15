package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistService {
    private final IRepository<Artist> artistRepository;
    private final IRepository<Event> eventRepository;
    private final FileRepository<Artist> artistFileRepository;

    public ArtistService(IRepository<Artist> artistRepository, IRepository<Event> eventRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.artistFileRepository = new FileRepository<>("src/repository/data/artists.csv", Artist::fromCsvFormat);
        List<Artist> artistsFromFile = artistFileRepository.getAll();
        for (Artist artist : artistsFromFile) {
            artistRepository.create(artist);
        }
    }

    /**
     * Creates a new artist and adds it to the repository.
     * @param artistName The name of the artist.
     * @param genre The genre of the artist.
     * @return true if the artist was successfully created and added to the repository, false otherwise.
     */
    public boolean createArtist(String artistName, String genre) {
        int newID = artistRepository.getAll().size() + 1;  // Generate unique ID based on the current size
        Artist artist = new Artist(newID, artistName, genre);
        artistRepository.create(artist);
        artistFileRepository.create(artist);
        return true;
    }

    /**
     * Updates an existing artist's details.
     * @param artistId The ID of the artist to be updated.
     * @param newName The new name for the artist.
     * @param newGenre The new genre for the artist.
     * @return true if the artist was found and successfully updated, false if the artist was not found.
     */
    public boolean updateArtist(int artistId, String newName, String newGenre) {
        Artist artist = findArtistByID(artistId);
        if (artist != null) {
            artist.setArtistName(newName);
            artist.setGenre(newGenre);
            artistRepository.update(artist);
            artistFileRepository.update(artist);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes an artist from the repository by their ID.
     * @param artistId The ID of the artist to be deleted.
     * @return true if the artist was found and successfully deleted, false if the artist was not found.
     */
    public boolean deleteArtist(int artistId) {
        Artist artist = findArtistByID(artistId);
        if (artist != null) {
            artistRepository.delete(artistId);
            artistFileRepository.delete(artistId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a list of all artists from the repository.
     * @return A list of all artists in the repository.
     */
    public List<Artist> getAllArtists() {
        return artistRepository.getAll();
    }

    /**
     * Finds an artist by their ID.
     * @param artistID The ID of the artist to be found.
     * @return The artist with the specified ID, or null if no artist was found.
     */
    public Artist findArtistByID(int artistID) {
        return artistRepository.getAll().stream().filter(artist -> artist.getID() == artistID).findFirst().orElse(null);
    }

    /**
     * Finds an artist by their name.
     * @param artistName The name of the artist to be found.
     * @return The artist with the specified name, or null if no artist was found.
     */
    public Artist findArtistByName(String artistName) {
        return artistRepository.getAll().stream().filter(artist -> artist.getArtistName().equalsIgnoreCase(artistName)).findFirst().orElse(null);
    }

    /**
     * Retrieves a list of events associated with a specific artist.
     * @param artist The artist whose events are to be retrieved.
     * @return A list of events that involve the specified artist, filtered by Concert events.
     */
    public List<Event> getEventsByArtist(Artist artist) {
        return eventRepository.getAll().stream().filter(event -> event instanceof Concert)  // Filter only Concert events
                .filter(event -> ((Concert) event).getArtists().equals(artist))  // Match the artist
                .collect(Collectors.toList());
    }

    /**
     * Finds all artists within a specific genre.
     * @param genre The genre to filter artists by.
     * @return A list of artists who belong to the specified genre.
     */
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
