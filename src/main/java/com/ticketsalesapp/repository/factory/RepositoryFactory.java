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

public interface RepositoryFactory {
    Repository<Artist> createArtistRepository();
    Repository<Athlete> createAthleteRepository();
    Repository<Seat> createSeatRepository();
    Repository<Row> createRowRepository();
    Repository<Section> createSectionRepository();
    Repository<Venue> createVenueRepository();
    Repository<Ticket> createTicketRepository();
    Repository<Cart> createCartRepository();
    Repository<User> createUserRepository();
    Repository<Event> createEventRepository();
    Repository<ConcertLineUp> createConcertLineUpRepository();
    Repository<SportsEventLineUp> createSportsEventLineUpRepository();
}