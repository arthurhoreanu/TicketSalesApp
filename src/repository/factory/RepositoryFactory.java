package repository.factory;

import model.*;
import repository.*;

public interface RepositoryFactory {
    IRepository<Artist> createArtistRepository();
    IRepository<Athlete> createAthleteRepository();
    IRepository<Seat> createSeatRepository();
    IRepository<Row> createRowRepository();
    IRepository<Section> createSectionRepository();
    IRepository<Venue> createVenueRepository();
    IRepository<Ticket> createTicketRepository();
    IRepository<Cart> createCartRepository();
    IRepository<PurchaseHistory> createPurchaseHistoryRepository();
    IRepository<User> createUserRepository();
    IRepository<Event> createEventRepository();
}