package repository.factory;

import model.*;
import repository.*;

public class FileRepositoryFactory implements RepositoryFactory {

    @Override
    public FileRepository<User> createUserRepository() {
        return new FileRepository<>("src/repository/data/users.csv", User::fromCsv);
    }

    @Override
    public FileRepository<Event> createEventRepository() {
        return new FileRepository<>("src/repository/data/events.csv", Event::fromCsv);
    }

    @Override
    public FileRepository<Artist> createArtistRepository() {
        return new FileRepository<>("src/repository/data/artists.csv", Artist::fromCsv);
    }

    @Override
    public FileRepository<Venue> createVenueRepository() {
        return new FileRepository<>("src/repository/data/venues.csv", Venue::fromCsv);
    }

    // Adaugă metode pentru celelalte entități
}