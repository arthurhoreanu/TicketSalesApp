package model;

/**
 * Represents an artist with an ID, name, and genre. Implements Identifiable and FavouriteEntity interfaces.
 */
public class Artist implements Identifiable, FavouriteEntity {
    private int artistID;
    private String artistName;
    private String genre;

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

    /**
     * Gets the name of the artist.
     * @return the artist's name
     */
    @Override
    public String getName() {
        return this.artistName;
    }

    /**
     * Gets the artist's name.
     * @return the artist's name
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Sets a new name for the artist.
     * @param artistName the new name of the artist
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * Gets the genre associated with the artist.
     * @return the genre of the artist
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets a new genre for the artist.
     * @param genre the new genre of the artist
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Returns a string representation of the artist, including artistID, artistName, and genre.
     * @return a string representing the artist's details
     */
    @Override
    public String toString() {
        return "Artist{" + "artistID=" + artistID + ", artistName='" + artistName + '\'' + ", genre='" + genre + '\'' + '}';
    }
}
