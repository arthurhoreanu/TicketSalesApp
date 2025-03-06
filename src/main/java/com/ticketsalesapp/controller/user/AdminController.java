package main.java.com.ticketsalesapp.controller.user;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.service.user.AdminService;
import org.springframework.stereotype.Component;

@Component
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void login(String username, String password) {
        if (adminService.login(username, password)) {
            System.out.println("✅ Admin logged in successfully.");
        } else {
            System.out.println("❌ Invalid credentials.");
        }
    }

    public void logout() {
        adminService.logout();
        System.out.println("✅ Admin logged out successfully.");
    }

    public void createAdmin(String username, String email, String password) {
        try {
            adminService.createAdmin(username, email, password);
            System.out.println("✅ Admin created successfully.");
        } catch (ValidationException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void findAdminById(int id) {
        try {
            Admin admin = adminService.findAdminById(id);
            System.out.println("🔍 Found admin: " + admin);
        } catch (BusinessLogicException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void getCurrentAdmin() {
        Admin admin = adminService.getCurrentAdmin();
        if (admin != null) {
            System.out.println("👤 Logged in admin: " + admin);
        } else {
            System.out.println("❌ No admin is currently logged in.");
        }
    }
}
