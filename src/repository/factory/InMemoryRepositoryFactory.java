package repository.factory;

import model.*;
import repository.*;

public class InMemoryRepositoryFactory implements RepositoryFactory {

    // TODO aici noile Repo-uri InMemory

    @Override
    public IRepository<Artist> createArtistRepository() {
        return new InMemoryRepository<>();
    }

    @Override
    public IRepository<Athlete> createAthleteRepository() {
        return new InMemoryRepository<>();
    }

}