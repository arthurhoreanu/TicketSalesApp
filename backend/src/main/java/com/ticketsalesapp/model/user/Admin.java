package com.ticketsalesapp.model.user;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents an admin user with specific access privileges.
 */
public class Admin extends User {
    public Admin(int userId, String username, String email, String password) {
        super(userId, username, email, password);
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}