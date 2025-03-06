package main.java.com.ticketsalesapp.model.event;

import lombok.Getter;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.FavouriteEntity;
import main.java.com.ticketsalesapp.model.Identifiable;

import javax.persistence.*;
import java.util.List;

/**
 * Represents an artist with an ID, name, and genre. Implements Identifiable and FavouriteEntity interfaces.
 */
@Entity
@Table(name = "artist")
public class Artist implements Identifiable, FavouriteEntity {

    public Artist() {}

    @Id
    @Column(name = "artist_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int artistID;

    /**
     * -- SETTER --
     *  Sets a new name for the artist.
     * -- GETTER --
     *  Gets the artist's name.
     *
     */
    @Getter
    @Setter
    @Column(name = "artist_name", nullable = false)
    private String artistName;

    /**
     * -- SETTER --
     *  Sets a new genre for the artist.
     * -- GETTER --
     *  Gets the genre associated with the artist.
     *
     */
    @Getter
    @Setter
    @Column(name = "genre", nullable = false)
    private String genre;

    @Transient
    @ManyToMany
    @JoinTable(
            name = "concert_lineup",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "concert_id")
    )
    private List<Concert> concerts;

    /**
     * Constructs an Artist with the specified ID, name, and genre.
     * @param artistID   the unique ID of the artist
     * @param artistName the name of the artist
     * @param genre      the genre associated with the artist
     */
    public Artist(int artistID, String artistName, String genre) {
        this.artistID = artistID;
        this.artistName = artistName;
        this.genre = genre;
    }

    /**
     * Gets the unique ID of the artist.
     * @return the artist's ID
     */
    @Override
    public Integer getID() {
        return this.artistID;
    }

    public void setID(int id) {
        this.artistID = id;
    }

    /**
     * Gets the name of the artist.
     * @return the artist's name
     */
    @Override
    public String getName() {
        return this.artistName;
    }

    /**
     * Returns a string representation of the artist, including artistID, artistName, and genre.
     * @return a string representing the artist's details
     */
    @Override
    public String toString() {
        return "Artist{" + "artistID=" + artistID + ", artistName='" + artistName + '\'' + ", genre='" + genre + '\'' + '}';
    }

    public static Artist fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int artistID = Integer.parseInt(fields[0]);
        String artistName = fields[1];
        String genre = fields[2];
        return new Artist(artistID, artistName, genre);
    }

    @Override
    public String toCsv() {
        return getID() + "," + getArtistName() + "," + getGenre();
    }
}