package main.java.com.ticketsalesapp.service;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.model.event.Artist;
import main.java.com.ticketsalesapp.repository.Repository;
import main.java.com.ticketsalesapp.repository.factory.RepositoryFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    private final Repository<Artist> artistRepository;

    public ArtistService(RepositoryFactory repositoryFactory) {
        this.artistRepository = repositoryFactory.createArtistRepository();
    }

    /**
     * Creates a new artist and adds it to the repository.
     * @param artistName The name of the artist.
     * @param genre The genre of the artist.
     */
    public void createArtist(String artistName, String genre) {
        if (findArtistByName(artistName).isPresent()) {
            throw new BusinessLogicException("Artist with name '" + artistName + "' already exists.");
        }
        Artist artist = new Artist(generateNewId(), artistName, genre);
        artistRepository.create(artist);
    }

    /**
     * Updates an existing artist's details.
     * @param artistId The ID of the artist to be updated.
     * @param newName The new name for the artist.
     * @param newGenre The new genre for the artist.
     */
    public void updateArtist(int artistId, String newName, String newGenre) {
        Artist artist = findArtistById(artistId);
        if (newName == null || newName.isBlank()) {
            throw new BusinessLogicException("Artist name cannot be null or empty.");
        }
        artist.setArtistName(newName);
        artist.setGenre(newGenre);
        artistRepository.update(artist);
    }

    /**
     * Deletes an artist from the repository by their ID.
     * @param artistId The ID of the artist to be deleted.
     */
    public void deleteArtist(int artistId) {
        findArtistById(artistId);
        artistRepository.delete(artistId);
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
    public Artist findArtistById(int artistID) {
        return artistRepository.read(artistID)
                .orElseThrow(() -> new BusinessLogicException("Artist not found"));
    }

    /**
     * Finds an artist by their name.
     * @param artistName The name of the artist to be found.
     * @return The artist with the specified name, or null if no artist was found.
     */
    public Optional<Artist> findArtistByName(String artistName) {
        return artistRepository.getAll().stream()
                .filter(artist -> artist.getArtistName().equalsIgnoreCase(artistName))
                .findFirst();
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

    private int generateNewId() {
        return artistRepository.getAll().stream()
                .mapToInt(Artist::getId)
                .max()
                .orElse(0) + 1;
    }
}