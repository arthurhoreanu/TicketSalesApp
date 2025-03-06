package main.java.com.ticketsalesapp.model;

/**
 * Interface representing an entity that can be uniquely identified by an ID.
 */
public interface Identifiable {
    Integer getID();
    void setID(int id);
}
