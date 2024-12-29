package repository.factory;

import model.*;
import repository.*;

public class DBRepositoryFactory implements RepositoryFactory {

    // TODO aici noile Repo-uri DB

    @Override
    public DBRepository<Artist> createArtistRepository() {
        return new DBRepository<>(Artist.class);
    }

    @Override
    public DBRepository<Athlete> createAthleteRepository() {
        return new DBRepository<>(Athlete.class);
    }

    @Override
    public DBRepository<Cart> createCartRepository() {
        return new DBRepository<>(Cart.class);
    }

    @Override
    public DBRepository<PurchaseHistory> createPurchaseHistoryRepository() {
        return new DBRepository<>(PurchaseHistory.class);
    }

    @Override
    public IRepository<User> createUserRepository() {
        CombinedRepository<User> combinedRepository = new CombinedRepository<>();
        combinedRepository.registerRepository(Admin.class, new DBRepository<>(Admin.class));
        combinedRepository.registerRepository(User.class, new DBRepository<>(User.class));
        return combinedRepository;
    }

    @Override
    public IRepository<Event> createEventRepository() {
        CombinedRepository<Event> combinedRepository = new CombinedRepository<>();
        combinedRepository.registerRepository(Concert.class, new DBRepository<>(Concert.class));
        combinedRepository.registerRepository(SportsEvent.class, new DBRepository<>(SportsEvent.class));
        return combinedRepository;
    }

}