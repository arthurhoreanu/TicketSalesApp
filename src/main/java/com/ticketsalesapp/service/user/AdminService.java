package main.java.com.ticketsalesapp.service.user;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.repository.Repository;
import main.java.com.ticketsalesapp.repository.factory.RepositoryFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final Repository<Admin> adminRepository;
    private final UserSession userSession;

    public AdminService(RepositoryFactory repositoryFactory, UserSession userSession) {
        this.adminRepository = repositoryFactory.createAdminRepository();
        this.userSession = userSession;
    }

    public void createAdmin(String username, String email, String password) {
        if (usernameExists(username)) {
            throw new ValidationException("Username already taken.");
        }
        if (!domainEmail(email)) {
            throw new ValidationException("Invalid email domain.");
        }
        Admin admin = new Admin(generateNewId(), username, email, password);
        adminRepository.create(admin);
    }

    public boolean usernameExists(String username) {
        return adminRepository.getAll().stream().anyMatch(a -> a.getUsername().equals(username));
    }

    public boolean domainEmail(String email) {
        return email.endsWith("@tsc.com");
    }

    public Admin login(String username, String password) throws BusinessLogicException {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessLogicException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessLogicException("Password cannot be empty");
        }

        Admin admin = adminRepository.getAll().stream()
                .filter(a -> a.getUsername().equals(username) && a.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Invalid credentials"));

        userSession.setCurrentUser(admin);
        return admin;
    }

    public void logout() throws BusinessLogicException {
        if (userSession.getCurrentUser().isEmpty()) {
            throw new BusinessLogicException("No user is currently logged in");
        }
        userSession.logout();
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.getAll();
    }

    public Admin findAdminById(int id) {
        return adminRepository.read(id)
                .orElseThrow(() -> new BusinessLogicException("Admin not found"));
    }

    public void deleteAdmin(int id) {
        findAdminById(id);
        adminRepository.delete(id);
    }




    private int generateNewId() {
        return adminRepository.getAll().stream()
                .mapToInt(Admin::getID)
                .max()
                .orElse(0) + 1;
    }

    public Admin getCurrentAdmin() {
        return userSession.getCurrentUser()
                .filter(user -> user instanceof Admin)
                .map(user -> (Admin) user)
                .orElseThrow(() -> new BusinessLogicException("No customer is logged in."));
    }

}