package main.java.com.ticketsalesapp.repository.factory;

import main.java.com.ticketsalesapp.model.event.*;
import main.java.com.ticketsalesapp.model.ticket.Cart;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.venue.Row;
import main.java.com.ticketsalesapp.model.venue.Seat;
import main.java.com.ticketsalesapp.model.venue.Section;
import main.java.com.ticketsalesapp.model.venue.Venue;
import main.java.com.ticketsalesapp.repository.*;

public class InMemoryRepositoryFactory implements RepositoryFactory {

    @Override
    public BaseRepository<Artist> createArtistRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Athlete> createAthleteRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Seat> createSeatRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Row> createRowRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Section> createSectionRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Venue> createVenueRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Ticket> createTicketRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Cart> createCartRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Admin> createAdminRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Customer> createCustomerRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<Concert> createConcertRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<SportsEvent> createSportsEventRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<ConcertLineUp> createConcertLineUpRepository() {
        return new InMemoryBaseRepository<>();
    }

    @Override
    public BaseRepository<SportsEventLineUp> createSportsEventLineUpRepository() {
        return new InMemoryBaseRepository<>();
    }

}