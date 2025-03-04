package main.java.com.ticketsalesapp.model;

/**
 * Interface representing an entity that can be uniquely identified by an ID.
 * Any class implementing this interface should provide a unique ID.
 */
public interface Identifiable {
    /**
     * Gets the unique ID of the entity.
     * @return the ID of the entity
     */
    Integer getID();

    String toCsv();

    static <T> T fromCsv(String csvLine) {
        return null;
    }

    void setID(int Int);
}