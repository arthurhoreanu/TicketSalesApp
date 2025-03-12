//package main.java.com.ticketsalesapp.repository.factory;
//
//import main.java.com.ticketsalesapp.model.user.Admin;
//import main.java.com.ticketsalesapp.model.user.Customer;
//import main.java.com.ticketsalesapp.repository.*;
//
//public class FileRepositoryFactory implements RepositoryFactory {
//
//    @Override
//    public FileRepository<Admin> createAdminRepository() {
//        return new FileRepository<>("src/repository/data/admins.csv", Admin::fromCsv);
//    }
//
//    @Override
//    public FileRepository<Customer> createCustomerRepository() {
//        return new FileRepository<>("src/repository/data/customers.csv", Customer::fromCsv);
//    }
//
////    @Override
////    public FileBaseRepository<Artist> createArtistRepository() {
////        return new FileBaseRepository<>("src/repository/data/artists.csv", Artist::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<Athlete> createAthleteRepository() {
////        return new FileBaseRepository<>("src/repository/data/athletes.csv", Athlete::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<Seat> createSeatRepository() {
////        return new FileBaseRepository<>("src/repository/data/seats.csv", Seat::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<Row> createRowRepository() {
////        return new FileBaseRepository<>("src/repository/data/rows.csv", Row::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<Section> createSectionRepository() {
////        return new FileBaseRepository<>("src/repository/data/sections.csv", Section::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<Venue> createVenueRepository() {
////        return new FileBaseRepository<>("src/repository/data/venues.csv", Venue::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<Ticket> createTicketRepository() {
////        return new FileBaseRepository<>("src/repository/data/tickets.csv", Ticket::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<Cart> createCartRepository() {
////        return new FileBaseRepository<>("src/repository/data/carts.csv", Cart::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<Concert> createConcertRepository() {
////        return new FileBaseRepository<>("src/repository/data/concerts.csv", Concert::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<SportsEvent> createSportsEventRepository() {
////        return new FileBaseRepository<>("src/repository/data/sports_events.csv", SportsEvent::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<ConcertLineUp> createConcertLineUpRepository() {
////        return new FileBaseRepository<>("src/repository/data/concert_line_ups.csv", ConcertLineUp::fromCsv);
////    }
////
////    @Override
////    public FileBaseRepository<SportsEventLineUp> createSportsEventLineUpRepository() {
////        return new FileBaseRepository<>("src/repository/data/sports_event_line_ups.csv", SportsEventLineUp::fromCsv);
////    }
//
//}