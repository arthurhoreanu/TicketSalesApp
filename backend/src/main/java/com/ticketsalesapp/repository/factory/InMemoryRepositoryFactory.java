package com.ticketsalesapp.repository.factory;

import com.ticketsalesapp.model.event.Artist;
import com.ticketsalesapp.model.event.Athlete;
import com.ticketsalesapp.model.user.Admin;
import com.ticketsalesapp.model.user.Customer;
import com.ticketsalesapp.repository.*;
import org.springframework.stereotype.Component;

@Component
public class InMemoryRepositoryFactory implements RepositoryFactory {

    @Override
    public Repository<Admin> createAdminRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Customer> createCustomerRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Artist> createArtistRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public Repository<Athlete> createAthleteRepository() {
        return new InMemoryRepository<>();
    }

//
//    @Override
//    public BaseRepository<Seat> createSeatRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<Row> createRowRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<Section> createSectionRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<Venue> createVenueRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<Ticket> createTicketRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<Cart> createCartRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<Concert> createConcertRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<SportsEvent> createSportsEventRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<ConcertLineUp> createConcertLineUpRepository() {
//        return new InMemoryBaseRepository<>();
//    }
//
//    @Override
//    public BaseRepository<SportsEventLineUp> createSportsEventLineUpRepository() {
//        return new InMemoryBaseRepository<>();
//    }

}