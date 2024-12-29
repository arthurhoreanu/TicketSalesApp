package repository.factory;

import model.*;
import repository.*;

public interface RepositoryFactory {

    // TODO aici noile Repo-uri generale

    IRepository<Artist> createArtistRepository();
    IRepository<Athlete> createAthleteRepository();
    IRepository<Cart> createCartRepository();
    IRepository<PurchaseHistory> createPurchaseHistoryRepository();
    IRepository<User> createUserRepository();
    IRepository<Event> createEventRepository();

}