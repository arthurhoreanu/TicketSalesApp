package main.java.com.ticketsalesapp.model.user;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents an admin user with specific access privileges.
 */
@NoArgsConstructor
public class Admin extends User {
    public Admin(int userId, String username, String email, String password) {
        super(userId, username, email, password);
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