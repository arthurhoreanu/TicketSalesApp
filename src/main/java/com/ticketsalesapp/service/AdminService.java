package main.java.com.ticketsalesapp.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final BaseRepository<Admin> adminRepository;

    @Getter
    private Admin currentAdmin;

    public boolean login(String username, String password) {
        var admin = adminRepository.getAll().stream()
                .filter(a -> a.getUsername().equals(username) && a.getPassword().equals(password))
                .findFirst();

        if (admin.isPresent()) {
            currentAdmin = admin.get();
            return true;
        }
        return false;
    }

    public boolean logout() {
        if (currentAdmin == null) {
            return false;
        }
        currentAdmin = null;
        return true;
    }

    public boolean createAdmin(String username, String email, String password) {
        if (takenUsername(username)) {
            throw new ValidationException("Username already taken");
        }
        if (!domainEmail(email)) {
            throw new ValidationException("Invalid email domain");
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(password);
        adminRepository.create(admin);
        return true;
    }

    private boolean takenUsername(String username) {
        return adminRepository.getAll().stream()
                .anyMatch(admin -> admin.getUsername().equals(username));
    }

    private boolean domainEmail(String email) {
        return email.endsWith("@tsc.com");
    }

    public Admin findAdminById(int id) {
        return adminRepository.read(id)
                .orElseThrow(() -> new BusinessLogicException("Admin not found"));
    }
}