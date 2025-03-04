package main.java.com.ticketsalesapp.repository.factory;

import main.java.com.ticketsalesapp.model.event.*;
import main.java.com.ticketsalesapp.model.ticket.Cart;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.user.User;
import main.java.com.ticketsalesapp.model.venue.Row;
import main.java.com.ticketsalesapp.model.venue.Seat;
import main.java.com.ticketsalesapp.model.venue.Section;
import main.java.com.ticketsalesapp.model.venue.Venue;
import main.java.com.ticketsalesapp.repository.*;

public class InMemoryRepositoryFactory implements RepositoryFactory {

    @Override
    public Repository<Artist> createArtistRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Athlete> createAthleteRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Seat> createSeatRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Row> createRowRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Section> createSectionRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Venue> createVenueRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Ticket> createTicketRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Cart> createCartRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<User> createUserRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Event> createEventRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<ConcertLineUp> createConcertLineUpRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<SportsEventLineUp> createSportsEventLineUpRepository() {
        return new InMemoryRepository<>();
    }

}