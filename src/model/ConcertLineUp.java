package model;

import controller.Controller;

import javax.persistence.*;

/**
 * Represents the association between a Concert and an Artist.
 */
@Entity
@Table(name = "concert_lineup")
public class ConcertLineUp implements Identifiable {

    @Id
    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Id
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    static Controller controller = ControllerProvider.getController();

    public ConcertLineUp() {}

    public ConcertLineUp(Concert concert, Artist artist) {
        this.concert = concert;
        this.artist = artist;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "ConcertLineUp{" +
                "concert=" + concert +
                ", artist=" + artist +
                '}';
    }

    @Override
    public Integer getID() {
        return 0;
    }

    @Override
    public void setID(int Int) {
    }

    @Override
    public String toCsv() {
        return getConcert().getID() + "," + getArtist().getID();
    }

    public static ConcertLineUp fromCsv(String line) {
        String[] fields = line.split(",");
        int concertID = Integer.parseInt(fields[0]);
        int artistID = Integer.parseInt(fields[1]);
        return new ConcertLineUp(controller.findConcertByID(concertID), controller.findArtistByID(artistID));
    }
}