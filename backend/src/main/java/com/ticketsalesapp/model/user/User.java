package main.java.com.ticketsalesapp.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.Identifiable;

/**
 * Represents a general user in the system, containing basic information.
 * This class serves as a base for Admin and Customer.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class User implements Identifiable {
    protected int userId;
    protected String username;
    protected String email;
    protected String password;

    public User(int userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Integer getId() {
        return userId;
    }

    @Override
    public void setId(int id) {
        this.userId = id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [userId=" + userId + ", username=" + username + ", email=" + email + "]";
    }
}
