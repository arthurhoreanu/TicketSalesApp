package repository.factory;

import model.*;
import repository.*;

public class FileRepositoryFactory implements RepositoryFactory {

    @Override
    public FileRepository<Artist> createArtistRepository() {
        return new FileRepository<>("src/repository/data/artists.csv", Artist::fromCsv);
    }

    @Override
    public FileRepository<Athlete> createAthleteRepository() {
        return new FileRepository<>("src/repository/data/athletes.csv", Athlete::fromCsv);
    }

    @Override
    public FileRepository<Seat> createSeatRepository() {
        return new FileRepository<>("src/repository/data/seats.csv", Seat::fromCsv);
    }

    @Override
    public FileRepository<Row> createRowRepository() {
        return new FileRepository<>("src/repository/data/rows.csv", Row::fromCsv);
    }

    @Override
    public FileRepository<Section> createSectionRepository() {
        return new FileRepository<>("src/repository/data/sections.csv", Section::fromCsv);
    }

    @Override
    public FileRepository<Venue> createVenueRepository() {
        return new FileRepository<>("src/repository/data/venues.csv", Venue::fromCsv);
    }

    @Override
    public FileRepository<Ticket> createTicketRepository() {
        return new FileRepository<>("src/repository/data/tickets.csv", Ticket::fromCsv);
    }

    @Override
    public FileRepository<Cart> createCartRepository() {
        return new FileRepository<>("src/repository/data/carts.csv", Cart::fromCsv);
    }

    @Override
    public FileRepository<PurchaseHistory> createPurchaseHistoryRepository() {
        return new FileRepository<>("src/repository/data/purchase_histories.csv", PurchaseHistory::fromCsv);
    }

    @Override
    public IRepository<User> createUserRepository() {
        CombinedRepository<User> combinedRepository = new CombinedRepository<>();
        combinedRepository.registerRepository(Admin.class, new FileRepository<>("src/repository/data/admins.csv", Admin::fromCsv));
        combinedRepository.registerRepository(Customer.class, new FileRepository<>("src/repository/data/customers.csv", Customer::fromCsv));
        return combinedRepository;
    }

    @Override
    public IRepository<Event> createEventRepository() {
        CombinedRepository<Event> combinedRepository = new CombinedRepository<>();
        combinedRepository.registerRepository(Concert.class, new FileRepository<>("src/repository/data/concerts.csv", Concert::fromCsv));
        combinedRepository.registerRepository(SportsEvent.class, new FileRepository<>("src/repository/data/sports_events.csv", SportsEvent::fromCsv));
        return combinedRepository;
    }

    @Override
    public FileRepository<ConcertLineUp> createConcertLineUpRepository() {
        return new FileRepository<>("src/repository/data/concert_line_ups.csv", ConcertLineUp::fromCsv);
    }

    @Override
    public FileRepository<SportsEventLineUp> createSportsEventLineUpRepository() {
        return new FileRepository<>("src/repository/data/sports_event_line_ups.csv", SportsEventLineUp::fromCsv);
    }

}