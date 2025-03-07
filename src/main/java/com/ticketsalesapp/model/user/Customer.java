package main.java.com.ticketsalesapp.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.ticket.Cart;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a customer user with specific preferences, favourites, and a shopping cart.
 */
@Getter
@Setter
@NoArgsConstructor
public class Customer extends User {
    private Set<FavouriteEntity> favourites = new HashSet<>();
    private Map<Integer, Integer> preferredSections = new HashMap<>();
    private Cart cart;

    public Customer(int userId, String username, String email, String password) {
        super(userId, username, email, password);
        this.preferredSections = new HashMap<>();
    }

    public void addFavourite(FavouriteEntity item) {
        favourites.add(item);
    }

    public void removeFavourite(FavouriteEntity item) {
        favourites.remove(item);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
