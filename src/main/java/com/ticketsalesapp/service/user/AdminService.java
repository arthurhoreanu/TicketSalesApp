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

    public boolean login(String username, String password) {
        var user = adminRepository.getAll().stream()
                .filter(a -> a.getUsername().equals(username) && a.getPassword().equals(password))
                .findFirst();
        if (user.isPresent()) {
            userSession.setCurrentUser(user.get());
            return true;
        }
        return false;
    }

    public void logout() {
        userSession.logout();
    }

    public void createAdmin(String username, String email, String password) {
        if (adminRepository.getAll().stream().anyMatch(a -> a.getUsername().equals(username))) {
            throw new ValidationException("Username already taken");
        }
        if (!domainEmail("@tsc.com")) {
            throw new ValidationException("Invalid email domain");
        }
        Admin admin = new Admin(generateNewId(), username, email, password);
        adminRepository.create(admin);
    }

    private int generateNewId() {
        return adminRepository.getAll().stream()
                .mapToInt(Admin::getID)
                .max()
                .orElse(0) + 1;
    }

    private boolean domainEmail(String email) {
        return email.endsWith("@tsc.com");
    }

    public Admin findAdminById(int id) {
        return adminRepository.read(id)
                .orElseThrow(() -> new BusinessLogicException("Admin not found"));
    }

    public Admin getCurrentAdmin() {
        return userSession.getCurrentUser()
                .filter(user -> user instanceof Admin)
                .map(user -> (Admin) user)
                .orElseThrow(() -> new BusinessLogicException("No customer is logged in."));
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.getAll();
    }

    public Admin findByUsernameAndPassword(String username, String password)
        throws BusinessLogicException {
        return adminRepository.getAll().stream()
            .filter(a -> a.getUsername().equals(username) && a.getPassword().equals(password))
            .findFirst()
            .orElseThrow(() -> new BusinessLogicException("Admin not found"));
        }
}