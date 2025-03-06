//package main.java.com.ticketsalesapp.repository.factory;
//
//import main.java.com.ticketsalesapp.model.user.Admin;
//import main.java.com.ticketsalesapp.model.user.Customer;
//import main.java.com.ticketsalesapp.repository.*;
//
//public class DBRepositoryFactory implements RepositoryFactory {
//
//    @Override
//    public DBRepository<Admin> createAdminRepository() {
//        return new DBRepository<>(Admin.class);
//    }
//
//    @Override
//    public DBRepository<Customer> createCustomerRepository() {
//        return new DBRepository<>(Customer.class);
//    }
//
////    @Override
////    public DBBaseRepository<Artist> createArtistRepository() {
////        return new DBBaseRepository<>(Artist.class);
////    }
////
////    @Override
////    public DBBaseRepository<Athlete> createAthleteRepository() {
////        return new DBBaseRepository<>(Athlete.class);
////    }
////
////    @Override
////    public DBBaseRepository<Seat> createSeatRepository() {
////        return new DBBaseRepository<>(Seat.class);
////    }
////
////    @Override
////    public DBBaseRepository<Row> createRowRepository() {
////        return new DBBaseRepository<>(Row.class);
////    }
////
////    @Override
////    public DBBaseRepository<Section> createSectionRepository() {
////        return new DBBaseRepository<>(Section.class);
////    }
////
////    @Override
////    public DBBaseRepository<Venue> createVenueRepository() {
////        return new DBBaseRepository<>(Venue.class);
////    }
////
////    @Override
////    public DBBaseRepository<Ticket> createTicketRepository() {
////        return new DBBaseRepository<>(Ticket.class);
////    }
////
////    @Override
////    public DBBaseRepository<Cart> createCartRepository() {
////        return new DBBaseRepository<>(Cart.class);
////    }
////
////    @Override
////    public DBBaseRepository<Concert> createConcertRepository() {
////        return new DBBaseRepository<>(Concert.class);
////    }
////
////    @Override
////    public DBBaseRepository<SportsEvent> createSportsEventRepository() {
////        return new DBBaseRepository<>(SportsEvent.class);
////    }
////
////    @Override
////    public DBBaseRepository<ConcertLineUp> createConcertLineUpRepository() {
////        return new DBBaseRepository<>(ConcertLineUp.class);
////    }
////
////    @Override
////    public DBBaseRepository<SportsEventLineUp> createSportsEventLineUpRepository() {
////        return new DBBaseRepository<>(SportsEventLineUp.class);
////    }
//
//}