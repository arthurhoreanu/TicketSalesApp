package model;

/**
 * Interface representing an entity (artist/athlete) that can be marked as a favorite.
 * Any entity implementing this interface should provide an ID and a name.
 */
public interface FavouriteEntity {
    /**
     * Gets the unique ID of the favorite entity.
     * @return the ID of the entity
     */
    Integer getID();

    /**
     * Gets the name of the favorite entity.
     * @return the name of the entity
     */
    String getName();
}
