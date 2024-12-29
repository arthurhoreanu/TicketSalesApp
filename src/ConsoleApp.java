import model.*;
import presentation.admin.AdminMenu;
import presentation.CustomerMenu;
import presentation.LoginMenu;
import presentation.StartMenu;
import presentation.MainMenu;
import repository.factory.RepositoryFactory;
import service.*;
import controller.*;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RepositoryFactory repositoryFactory = StartMenu.select(scanner);

        Controller controller = initializeController(repositoryFactory);

        boolean running = true;

        while (running) {
            if (controller.getAllUsers().isEmpty()) {
                running = MainMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() == null) {
                running = LoginMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() instanceof Admin) {
                running = AdminMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() instanceof Customer) {
                running = CustomerMenu.display(scanner, controller);
            }
        }
        scanner.close();
    }

    private static Controller initializeController(RepositoryFactory repositoryFactory) {

        // TODO aici noile Serivce și Controller, alfabetic ar fi mai nice

        // Service
        ArtistService artistService = new ArtistService((RepositoryFactory) repositoryFactory.createArtistRepository());
        AthleteService athleteService = new AthleteService((RepositoryFactory) repositoryFactory.createAthleteRepository());

        // Controller
        ArtistController artistController = new ArtistController(artistService);
        AthleteController athleteController = new AthleteController(athleteService);

        // Main Controller
        return new Controller(artistController, athleteController);
    }
}