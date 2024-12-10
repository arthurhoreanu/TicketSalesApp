package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
     * Converts the current Artist object into a PreparedStatement for database operations.
     * @param stmt the PreparedStatement to populate with Artist data.
     * @throws SQLException if an SQL error occurs.
     */
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, this.artistID);
        stmt.setString(2, this.artistName);
        stmt.setString(3, this.genre);
    }

    /**
     * Creates an Artist object from a ResultSet.
     * @param rs the ResultSet containing Artist data.
     * @return an Artist object populated with data from the ResultSet.
     * @throws SQLException if an SQL error occurs.
     */
    public static Artist fromDatabase(ResultSet rs) throws SQLException {
        int artistID = rs.getInt("artist_id");
        String artistName = rs.getString("artist_name");
        String genre = rs.getString("genre");
        return new Artist(artistID, artistName, genre);
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