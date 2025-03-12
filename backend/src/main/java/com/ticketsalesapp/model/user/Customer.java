package com.ticketsalesapp.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Represents a customer user with specific preferences, favourites, and a shopping cart.
 */
@Getter
@Setter
public class Customer extends User {
    private Set<FavouriteEntity> favourites = new HashSet<>();
    private Map<Integer, Integer> preferredSections = new HashMap<>();
//    private Cart cart;

    public Customer(int userId, String username, String email, String password) {
        super(userId, username, email, password);
        this.preferredSections = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<FavouriteEntity> getFavourites() {
        return favourites;
    }

    @Override
    public Integer getId(){
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
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
