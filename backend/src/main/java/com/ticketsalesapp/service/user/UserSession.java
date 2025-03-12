package main.java.com.ticketsalesapp.service.user;

import lombok.Setter;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.user.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Setter
@Component
public class UserSession {
    private User currentUser;

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public boolean isAdmin(User currentUser) {
        return currentUser instanceof Admin;
    }

    public boolean isCustomer(User currentUser) {
        return currentUser instanceof Customer;
    }

    public void logout() {
        currentUser = null;
    }
}
