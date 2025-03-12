package com.ticketsalesapp.model.event;

import lombok.Getter;
import lombok.Setter;
import com.ticketsalesapp.model.user.FavouriteEntity;
import com.ticketsalesapp.model.Identifiable;

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

    @Column(name = "artist_name", nullable = false)
    private String artistName;

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
    public Integer getId() {
        return this.artistID;
    }

    public void setId(int id) {
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

    public int getArtistID() {
        return artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenre() {
        return genre;
    }

    public List<Concert> getConcerts() {
        return concerts;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setConcerts(List<Concert> concerts) {
        this.concerts = concerts;
    }
}