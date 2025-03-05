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

public interface RepositoryFactory {
    BaseRepository<Artist> createArtistRepository();
    BaseRepository<Athlete> createAthleteRepository();
    BaseRepository<Seat> createSeatRepository();
    BaseRepository<Row> createRowRepository();
    BaseRepository<Section> createSectionRepository();
    BaseRepository<Venue> createVenueRepository();
    BaseRepository<Ticket> createTicketRepository();
    BaseRepository<Cart> createCartRepository();
    BaseRepository<Admin> createAdminRepository();
    BaseRepository<Customer> createCustomerRepository();
    BaseRepository<Concert> createConcertRepository();
    BaseRepository<SportsEvent> createSportsEventRepository();
    BaseRepository<ConcertLineUp> createConcertLineUpRepository();
    BaseRepository<SportsEventLineUp> createSportsEventLineUpRepository();
}