package controller;

public class Controller {
    private final AccountController accountController;

    public Controller(AccountController accountController) {
        this.accountController = accountController;
    }

    public void createAccount(String role, String username, String password, String email) {
        accountController.createAccount(role, username, password, email);
    }

    public void login(String username, String password) {
        accountController.login(username, password);
    }

    public void logout() {
        accountController.logout();
    }

    public void deleteAccount(int id) {
        accountController.deleteAccount(id);
    }

    public boolean isUsernameTaken(String username) {
        return accountController.isUsernameTaken(username);
    }
}
