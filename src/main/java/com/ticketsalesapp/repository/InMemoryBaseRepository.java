package main.java.com.ticketsalesapp.repository;

import main.java.com.ticketsalesapp.model.Identifiable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryBaseRepository<T extends Identifiable> implements BaseRepository<T> {
    private final Map<Integer, T> data = new HashMap<>();

    /**
     * Adds an object to the repository if it does not already exist.
     * @param obj The object to be added to the repository.
     */
    @Override
    public void create(T obj) {
        if (obj.getID() == 0) {
            int newId = data.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
            obj.setID(newId);
        }
        data.putIfAbsent(obj.getID(), obj);
    }

    /**
     * Retrieves an object from the repository by its ID.
     * @param id The ID of the object to be retrieved.
     */
    @Override
    public T read(Integer id) {
        return data.get(id);
    }

    /**
     * Updates an existing object in the repository.
     * @param obj The object to be updated in the repository.
     */
    @Override
    public void update(T obj) {
        data.replace(obj.getID(), obj);
    }

    /**
     * Removes an object from the repository by its ID.
     * @param id The ID of the object to be removed.
     */
    @Override
    public void delete(Integer id) {
        data.remove(id);
    }

    /**
     * Retrieves all objects from the repository.
     * The list will contain all values from the repository, or an empty list if no objects are stored.
     * @return A list containing all the objects currently stored in the repository.
     */
    @Override
    public List<T> getAll() {
        return data.values().stream().toList();
    }
}