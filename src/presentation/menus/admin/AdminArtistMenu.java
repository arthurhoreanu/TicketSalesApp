package presentation.menus.admin;

import controller.Controller;
import presentation.actions.ArtistAction;

import java.util.Scanner;

public class AdminArtistMenu {
    public static void display(Scanner scanner, Controller controller) {
        boolean inArtistMenu = true;
        while (inArtistMenu) {
            System.out.println("==== Artist Management ====");
            System.out.println("1. Create Artist");
            System.out.println("2. View Artists");
            System.out.println("3. Update Artist");
            System.out.println("4. Delete Artist");
            System.out.println("0. Back to Admin Menu");
            System.out.println("==========================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    ArtistAction.handleCreateArtist(scanner, controller);
                    break;
                case "2":
                    ArtistAction.handleViewArtists(controller);
                    break;
                case "3":
                    ArtistAction.handleUpdateArtist(scanner, controller);
                    break;
                case "4":
                    ArtistAction.handleDeleteArtist(scanner, controller);
                    break;
                case "0":
                    inArtistMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }
}
