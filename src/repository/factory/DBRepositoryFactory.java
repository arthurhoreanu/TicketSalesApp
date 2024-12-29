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

}