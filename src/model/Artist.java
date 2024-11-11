package model;

public class Artist implements Identifiable, FavouriteEntity {
    private int artistID;
    private String artistName;
    private String genre;

    public Artist(int artistID, String artistName, String genre) {
        this.artistID = artistID;
        this.artistName = artistName;
        this.genre = genre;
    }

    @Override
    public Integer getID() {
        return this.artistID;
    }

    @Override
    public String getName() {
        return this.artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Artist{" + "artistID=" + artistID + ", artistName='" + artistName + '\'' + ", genre='" + genre + '\'' + '}';
    }
}
