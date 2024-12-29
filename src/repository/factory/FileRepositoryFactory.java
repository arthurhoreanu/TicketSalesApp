package repository.factory;

import model.*;
import repository.*;

public class FileRepositoryFactory implements RepositoryFactory {

    // TODO aici noile Repo-uri File

    @Override
    public FileRepository<Artist> createArtistRepository() {
        return new FileRepository<>("src/repository/data/artists.csv", Artist::fromCsv);
    }

    @Override
    public FileRepository<Athlete> createAthleteRepository() {
        return new FileRepository<>("src/repository/data/athletes.csv", Athlete::fromCsv);
    }

}