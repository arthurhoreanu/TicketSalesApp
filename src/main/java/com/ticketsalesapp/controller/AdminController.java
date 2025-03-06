package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.user.User;
import main.java.com.ticketsalesapp.service.AdminService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public boolean login(String username, String password) {
        boolean success = adminService.login(username, password);
        if (success) {
            System.out.println("Admin logged in successfully");
        } else {
            System.out.println("Invalid credentials");
        }
        return success;
    }

    public boolean logout() {
        boolean success = adminService.logout();
        if (success) {
            System.out.println("Admin logged out successfully");
        } else {
            System.out.println("No admin currently logged in");
        }
        return success;
    }

    public boolean createAdmin(String username, String email, String password) {
        try {
            return adminService.createAdmin(username, email, password);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public Admin getCurrentAdmin() {
        return adminService.getCurrentAdmin();
    }

    public Admin findAdminById(int id) {
        try {
            return adminService.findAdminById(id);
        } catch (BusinessLogicException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}