package main.java.com.ticketsalesapp.controller.user;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.service.user.AdminService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void createAdmin(String username, String email, String password) {
        adminService.createAdmin(username, email, password);
    }

    public boolean usernameExists(String username) {
        return adminService.usernameExists(username);
    }

    public boolean domainEmail(String email) {
        return adminService.domainEmail(email);
    }

    public Admin login(String username, String password) throws BusinessLogicException {
        return adminService.login(username, password);
    }

    public void logout() throws BusinessLogicException {
        adminService.logout();
    }

    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    public Admin findAdminById(int id) {
        return adminService.findAdminById(id);
    }

    public void deleteAdmin(int id) {
        adminService.deleteAdmin(id);
    }

    public void getCurrentAdmin() {
        Admin admin = adminService.getCurrentAdmin();
        if (admin != null) {
            System.out.println("👤 Logged in admin: " + admin);
        } else {
            System.out.println("❌ No admin is currently logged in.");
        }
    }

    public void displayAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        if (admins.isEmpty()) {
            System.out.println("There are no admins in the database.");
        }
        admins.forEach(System.out::println);
    }
}
