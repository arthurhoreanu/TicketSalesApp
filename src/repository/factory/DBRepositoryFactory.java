package repository.factory;

import model.*;
import repository.*;

public class DBRepositoryFactory implements RepositoryFactory {

    @Override
    public DBRepository<User> createUserRepository() {
        return new DBRepository<>(User.class);
    }

    @Override
    public DBRepository<Event> createEventRepository() {
        return new DBRepository<>(Event.class);
    }

    @Override
    public DBRepository<Artist> createArtistRepository() {
        return new DBRepository<>(Artist.class);
    }

    @Override
    public DBRepository<Venue> createVenueRepository() {
        return new DBRepository<>(Venue.class);
    }

    // Adaugă metode pentru celelalte entități
}