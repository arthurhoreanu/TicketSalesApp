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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_lineup_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    static Controller controller = ControllerProvider.getController();

    public ConcertLineUp() {}

    public ConcertLineUp(Concert concert, Artist artist) {
        this.concert = concert;
        this.artist = artist;
    }

    public Integer getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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
                "id=" + id +
                ", concert=" + concert +
                ", artist=" + artist +
                '}';
    }

    @Override
    public String toCsv() {
        return id + "," + concert.getID() + "," + artist.getID();
    }

    public static ConcertLineUp fromCsv(String line) {
        String[] fields = line.split(",");
        int id = Integer.parseInt(fields[0]);
        int concertID = Integer.parseInt(fields[1]);
        int artistID = Integer.parseInt(fields[2]);
        Concert concert = controller.findConcertByID(concertID);
        Artist artist = controller.findArtistByID(artistID);
        ConcertLineUp concertLineUp = new ConcertLineUp(concert, artist);
        concertLineUp.setID(id);
        return concertLineUp;
    }
}