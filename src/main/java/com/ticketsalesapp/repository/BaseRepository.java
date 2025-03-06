package main.java.com.ticketsalesapp.repository;

import main.java.com.ticketsalesapp.model.Identifiable;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends Identifiable> {

    /**
     * Creates and adds an object to the repository.
     *
     * @param obj The object to be added to the repository.
     * @return
     */
    boolean create(T obj);

    /**
     * Retrieves an object from the repository by its ID.
     * @param id The ID of the object to be retrieved.
     */
    Optional<T> read(Integer id);

    /**
     * Updates an existing object in the repository.
     *
     * @param obj The object to be updated in the repository.
     * @return
     */
    boolean update(T obj);

    /**
     * Deletes an object from the repository by its ID.
     *
     * @param id The ID of the object to be deleted.
     * @return
     */
    boolean delete(Integer id);

    /**
     * Retrieves all objects from the repository.
     * The list will contain all objects in the repository or be empty if no objects are stored.
     * @return A list of all objects stored in the repository.
     */
    List<T> getAll();
}
