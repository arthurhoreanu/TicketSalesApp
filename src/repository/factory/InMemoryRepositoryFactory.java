package repository.factory;

import model.*;
import repository.*;

public class InMemoryRepositoryFactory implements RepositoryFactory {

    @Override
    public IRepository<User> createUserRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Event> createEventRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Artist> createArtistRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Venue> createVenueRepository() {
        return new InMemoryRepository<>();
    }

    // Adaugă metode pentru celelalte entități
}