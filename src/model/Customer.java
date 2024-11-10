package model;

import java.util.HashSet;
import java.util.Set;

public class Customer extends User {
    private String role;
    private Set<FavouriteItem> favourites;

    public Customer(int userId, String username, String email, String password) {
        super(userId, username, email, password);
        this.role = role;
        this.favourites = new HashSet<>();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAccessLevel() {
        return "Customer";
    }

    public boolean addFavorite(FavouriteItem item) {
        return favourites.add(item);
    }

    public boolean removeFavorite(FavouriteItem item) {
        return favourites.remove(item);
    }

    public Set<FavouriteItem> getFavorites() {
        return favourites;
    }

    @Override
    public String toString() {
        return "Customer{" + "role='" + role + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}
