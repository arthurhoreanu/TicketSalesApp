package repository.factory;

import model.*;
import repository.*;

public class DBRepositoryFactory implements RepositoryFactory {

    @Override
    public DBRepository<Artist> createArtistRepository() {
        return new DBRepository<>(Artist.class);
    }

    @Override
    public DBRepository<Athlete> createAthleteRepository() {
        return new DBRepository<>(Athlete.class);
    }

    @Override
    public DBRepository<Seat> createSeatRepository() {
        return new DBRepository<>(Seat.class);
    }

    @Override
    public DBRepository<Row> createRowRepository() {
        return new DBRepository<>(Row.class);
    }

    @Override
    public DBRepository<Section> createSectionRepository() {
        return new DBRepository<>(Section.class);
    }

    @Override
    public DBRepository<Venue> createVenueRepository() {
        return new DBRepository<>(Venue.class);
    }

    @Override
    public DBRepository<Ticket> createTicketRepository() {
        return new DBRepository<>(Ticket.class);
    }

    @Override
    public DBRepository<Cart> createCartRepository() {
        return new DBRepository<>(Cart.class);
    }


    @Override
    public DBRepository<User> createUserRepository() {
        return new DBRepository<>(User.class);
    }

    @Override
    public DBRepository<Event> createEventRepository() {
        return new DBRepository<>(Event.class);
    }

    @Override
    public DBRepository<ConcertLineUp> createConcertLineUpRepository() {
        return new DBRepository<>(ConcertLineUp.class);
    }

    @Override
    public DBRepository<SportsEventLineUp> createSportsEventLineUpRepository() {
        return new DBRepository<>(SportsEventLineUp.class);
    }

}