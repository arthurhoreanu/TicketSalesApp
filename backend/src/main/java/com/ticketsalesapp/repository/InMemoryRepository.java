package main.java.com.ticketsalesapp.repository;

import main.java.com.ticketsalesapp.model.Identifiable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class InMemoryRepository<T extends Identifiable> implements Repository<T> {
    private final Map<Integer, T> data = new HashMap<>();

    /**
     * Adds an object to the repository if it does not already exist.
     *
     * @param obj The object to be added to the repository.
     * @return
     */
    @Override
    public boolean create(T obj) {
        if (obj.getId() == 0) {
            int newId = data.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
            obj.setId(newId);
        }
        data.putIfAbsent(obj.getId(), obj);
        return false;
    }

    /**
     * Retrieves an object from the repository by its ID.
     *
     * @param id The ID of the object to be retrieved.
     */
    @Override
    public Optional<T> read(Integer id) {
        return Optional.ofNullable(data.get(id));
    }

    /**
     * Updates an existing object in the repository.
     *
     * @param obj The object to be updated in the repository.
     * @return
     */
    @Override
    public boolean update(T obj) {
        data.replace(obj.getId(), obj);
        return false;
    }

    /**
     * Removes an object from the repository by its ID.
     *
     * @param id The ID of the object to be removed.
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        data.remove(id);
        return false;
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