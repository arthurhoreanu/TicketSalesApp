package main.java.com.ticketsalesapp.service;

import lombok.RequiredArgsConstructor;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.Artist;
import main.java.com.ticketsalesapp.repository.BaseRepository;
import main.java.com.ticketsalesapp.repository.factory.RepositoryFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final BaseRepository<Artist> artistBaseRepository;

    /**
     * Creates a new artist and adds it to the repository.
     * @param artistName The name of the artist.
     * @param genre The genre of the artist.
     * @return true if the artist was successfully created and added to the repository, false otherwise.
     */
    public boolean createArtist(String artistName, String genre) {
        if (findArtistByName(artistName) != null) {
            throw new BusinessLogicException("Artist with name '" + artistName + "' already exists.");
        }
        Artist artist = new Artist(0, artistName, genre);
        artistBaseRepository.create(artist);
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
        if(artist == null) {
            throw new ValidationException("Artist with id '" + artistId + "' does not exist.");
        }
        if (newName == null || newName.isBlank()) {
            throw new BusinessLogicException("Artist name cannot be null or empty.");
        }
        artist.setArtistName(newName);
        artist.setGenre(newGenre);
        artistBaseRepository.update(artist);
        return true;
    }

    /**
     * Deletes an artist from the repository by their ID.
     * @param artistId The ID of the artist to be deleted.
     * @return true if the artist was found and successfully deleted, false if the artist was not found.
     */
    public boolean deleteArtist(int artistId) {
        Artist artist = findArtistByID(artistId);
        if(artist == null) {
            throw new ValidationException("Artist with id '" + artistId + "' does not exist.");
        }
        artistBaseRepository.delete(artistId);
        return true;
    }

    /**
     * Retrieves a list of all artists from the repository.
     * @return A list of all artists in the repository.
     */
    public List<Artist> getAllArtists() {
        return artistBaseRepository.getAll();
    }

    /**
     * Finds an artist by their ID.
     * @param artistID The ID of the artist to be found.
     * @return The artist with the specified ID, or null if no artist was found.
     */
    public Artist findArtistByID(int artistID) {
        return artistBaseRepository.getAll().stream().filter(artist -> artist.getID() == artistID).findFirst().orElse(null);
    }

    /**
     * Finds an artist by their name.
     * @param artistName The name of the artist to be found.
     * @return The artist with the specified name, or null if no artist was found.
     */
    public Artist findArtistByName(String artistName) {
        return artistBaseRepository.getAll().stream().filter(artist -> artist.getArtistName().equalsIgnoreCase(artistName)).findFirst().orElse(null);
    }

    /**
     * Finds all artists within a specific genre.
     * @param genre The genre to filter artists by.
     * @return A list of artists who belong to the specified genre.
     */
    public List<Artist> findArtistsByGenre(String genre) {
        List<Artist> artistsInGenre = new ArrayList<>();
        for (Artist artist : artistBaseRepository.getAll()) {
            if (artist.getGenre().equalsIgnoreCase(genre)) {
                artistsInGenre.add(artist);
            }
        }
        return artistsInGenre;
    }
}