package com.ticketsalesapp.repository.factory;

import com.ticketsalesapp.model.event.Artist;
import com.ticketsalesapp.model.event.Athlete;
import com.ticketsalesapp.model.user.Admin;
import com.ticketsalesapp.model.user.Customer;
import com.ticketsalesapp.repository.*;

public interface RepositoryFactory {
    Repository<Admin> createAdminRepository();
    Repository<Customer> createCustomerRepository();
    Repository<Artist> createArtistRepository();
    Repository<Athlete> createAthleteRepository();

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