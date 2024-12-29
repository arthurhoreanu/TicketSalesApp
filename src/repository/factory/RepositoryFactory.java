package repository.factory;

import model.*;
import repository.*;

public interface RepositoryFactory {
    IRepository<User> createUserRepository();
    IRepository<Event> createEventRepository();
    IRepository<Artist> createArtistRepository();
    IRepository<Venue> createVenueRepository();
    // Adaugă alte metode pentru celelalte entități
}