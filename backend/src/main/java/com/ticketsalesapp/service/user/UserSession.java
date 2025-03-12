package com.ticketsalesapp.service.user;

import lombok.Setter;
import com.ticketsalesapp.model.user.Admin;
import com.ticketsalesapp.model.user.Customer;
import com.ticketsalesapp.model.user.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Setter
@Component
public class UserSession {

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

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
