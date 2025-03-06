package main.java.com.ticketsalesapp.model.event;

import lombok.Getter;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.Identifiable;

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

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

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

    @Override
    public String toString() {
        return "ConcertLineUp{" +
                "id=" + id +
                ", concert=" + concert +
                ", artist=" + artist +
                '}';
    }
}