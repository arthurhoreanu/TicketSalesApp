package repository;

import model.Identifiable;

import java.util.List;

public interface IRepository<T extends Identifiable> {

    /**
     * Creates and adds an object to the repository.
     * @param obj The object to be added to the repository.
     */
    void create(T obj);

    /**
     * Retrieves an object from the repository by its ID.
     * @param id The ID of the object to be retrieved.
     */
    void read(Integer id);

    /**
     * Updates an existing object in the repository.
     * @param obj The object to be updated in the repository.
     */
    void update(T obj);

    /**
     * Deletes an object from the repository by its ID.
     * @param id The ID of the object to be deleted.
     */
    void delete(Integer id);

    /**
     * Retrieves all objects from the repository.
     * The list will contain all objects in the repository or be empty if no objects are stored.
     * @return A list of all objects stored in the repository.
     */
    List<T> getAll();
}
