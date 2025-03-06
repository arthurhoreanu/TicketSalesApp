package main.java.com.ticketsalesapp.model.user;

import lombok.NoArgsConstructor;

/**
 * Represents an admin user with specific access privileges.
 */
@NoArgsConstructor
public class Admin extends User {
    public Admin(int userId, String username, String email, String password) {
        super(userId, username, email, password);
    }
}