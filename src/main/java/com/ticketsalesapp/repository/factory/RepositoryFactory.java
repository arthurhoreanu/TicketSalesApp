package main.java.com.ticketsalesapp.repository.factory;

import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.repository.*;

public interface RepositoryFactory {
    Repository<Admin> createAdminRepository();
    Repository<Customer> createCustomerRepository();
//    BaseRepository<Artist> createArtistRepository();
//    BaseRepository<Athlete> createAthleteRepository();
//    BaseRepository<Seat> createSeatRepository();
//    BaseRepository<Row> createRowRepository();
//    BaseRepository<Section> createSectionRepository();
//    BaseRepository<Venue> createVenueRepository();
//    BaseRepository<Ticket> createTicketRepository();
//    BaseRepository<Cart> createCartRepository();
//    BaseRepository<Concert> createConcertRepository();
//    BaseRepository<SportsEvent> createSportsEventRepository();
//    BaseRepository<ConcertLineUp> createConcertLineUpRepository();
//    BaseRepository<SportsEventLineUp> createSportsEventLineUpRepository();
}