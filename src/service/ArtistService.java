package service;

import model.Artist;
import model.Concert;
import model.Event;

import repository.FileRepository;
import repository.IRepository;
import repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistService {
    private final IRepository<Artist> artistRepository;
    private final FileRepository<Artist> artistFileRepository;
    private final DBRepository<Artist> artistDBRepository;

    public ArtistService(IRepository<Artist> artistRepository) {
        this.artistRepository = artistRepository;
        this.artistFileRepository = new FileRepository<>("src/repository/data/artists.csv", Artist::fromCsv);
        this.artistDBRepository = new DBRepository<>(Artist.class);
        syncFromSource(artistFileRepository, artistDBRepository);
    }

    private void syncFromSource(FileRepository<Artist> fileRepository, DBRepository<Artist> dbRepository) {
        List<Artist> fileArtists = fileRepository.getAll();
        List<Artist> dbArtists = dbRepository.getAll();

        for (Artist artist : fileArtists) {
            if(findArtistByID(artist.getID()) == null) {
                artistRepository.create(artist);
            }
        }

        for (Artist artist : dbArtists) {
            if(findArtistByID(artist.getID()) == null) {
                artistRepository.create(artist);
            }
        }
    }

    /**
     * Creates a new artist and adds it to the repository.
     * @param artistName The name of the artist.
     * @param genre The genre of the artist.
     * @return true if the artist was successfully created and added to the repository, false otherwise.
     */
    public boolean createArtist(String artistName, String genre) {
        Artist artist = new Artist(0, artistName, genre); // ID-ul este setat la 0 inițial
        artistDBRepository.create(artist); // Baza de date generează ID-ul

        // Verificare: ID-ul generat este setat corect
        if (artist.getID() == 0) {
            throw new IllegalStateException("ID-ul nu a fost generat de baza de date.");
        }

        artistFileRepository.create(artist);
        artistRepository.create(artist); // InMemory primește ID-ul generat
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
            artistDBRepository.update(artist);
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
            artistDBRepository.delete(artistId);
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