package main.java.com.ticketsalesapp.repository.factory;

import main.java.com.ticketsalesapp.model.event.*;
import main.java.com.ticketsalesapp.model.ticket.Cart;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.user.User;
import main.java.com.ticketsalesapp.model.venue.Row;
import main.java.com.ticketsalesapp.model.venue.Seat;
import main.java.com.ticketsalesapp.model.venue.Section;
import main.java.com.ticketsalesapp.model.venue.Venue;
import main.java.com.ticketsalesapp.repository.*;

public class DBRepositoryFactory implements RepositoryFactory {

    @Override
    public DBBaseRepository<Artist> createArtistRepository() {
        return new DBBaseRepository<>(Artist.class);
    }

    @Override
    public DBBaseRepository<Athlete> createAthleteRepository() {
        return new DBBaseRepository<>(Athlete.class);
    }

    @Override
    public DBBaseRepository<Seat> createSeatRepository() {
        return new DBBaseRepository<>(Seat.class);
    }

    @Override
    public DBBaseRepository<Row> createRowRepository() {
        return new DBBaseRepository<>(Row.class);
    }

    @Override
    public DBBaseRepository<Section> createSectionRepository() {
        return new DBBaseRepository<>(Section.class);
    }

    @Override
    public DBBaseRepository<Venue> createVenueRepository() {
        return new DBBaseRepository<>(Venue.class);
    }

    @Override
    public DBBaseRepository<Ticket> createTicketRepository() {
        return new DBBaseRepository<>(Ticket.class);
    }

    @Override
    public DBBaseRepository<Cart> createCartRepository() {
        return new DBBaseRepository<>(Cart.class);
    }

    @Override
    public DBBaseRepository<Admin> createAdminRepository() {
        return new DBBaseRepository<>(Admin.class);
    }

    @Override
    public DBBaseRepository<Customer> createCustomerRepository() {
        return new DBBaseRepository<>(Customer.class);
    }

    @Override
    public DBBaseRepository<Concert> createConcertRepository() {
        return new DBBaseRepository<>(Concert.class);
    }

    @Override
    public DBBaseRepository<SportsEvent> createSportsEventRepository() {
        return new DBBaseRepository<>(SportsEvent.class);
    }

    @Override
    public DBBaseRepository<ConcertLineUp> createConcertLineUpRepository() {
        return new DBBaseRepository<>(ConcertLineUp.class);
    }

    @Override
    public DBBaseRepository<SportsEventLineUp> createSportsEventLineUpRepository() {
        return new DBBaseRepository<>(SportsEventLineUp.class);
    }

}