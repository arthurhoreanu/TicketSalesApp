package repository.factory;

import model.*;
import repository.*;

import java.awt.print.Book;

public class InMemoryRepositoryFactory implements RepositoryFactory {

    @Override
    public IRepository<Artist> createArtistRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Athlete> createAthleteRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Seat> createSeatRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Row> createRowRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Section> createSectionRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Venue> createVenueRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Ticket> createTicketRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Cart> createCartRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<User> createUserRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Event> createEventRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<ConcertLineUp> createConcertLineUpRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<SportsEventLineUp> createSportsEventLineUpRepository() {
        return new InMemoryRepository<>();
    }

}