//TODO import all model
import model.User;

//TODO import all repository
import repository.IRepository;
import repository.InMemoryRepository;

//TODO import all service
import service.AccountService;

//TODO import all controller
import controller.AccountController;

public class ConsoleApp {
    public static void main(String[] args) {
        IRepository<User> userRepository = new InMemoryRepository<>();
        AccountService accountService = new AccountService(userRepository);
        AccountController accountController = new AccountController(accountService);

//        accountController.createAccount("Admin", "arthur", "arthurspassword", "arthur.horeanu@email.com");
//        accountController.createAccount("Admin", "malina", "malinaspassword", "malina.dumitrescu@email.com");
//        accountController.login("arthur", "arthurspassword");
//        accountController.logout();
    }
}