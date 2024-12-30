package repository.factory;

import model.Identifiable;
import repository.IRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinedRepository<T extends Identifiable> implements IRepository<T> {

    private final Map<Class<? extends T>, IRepository<? extends T>> subRepositories = new HashMap<>();

    /**
     * Registers a sub-repository for a specific class.
     *
     * @param clazz      The class the repository is associated with.
     * @param repository The repository instance.
     * @param <S>        The specific type of T managed by the repository.
     */
    public <S extends T> void registerRepository(Class<S> clazz, IRepository<S> repository) {
        subRepositories.put(clazz, repository);
    }

    /**
     * Finds the repository for a specific object type.
     */
    @SuppressWarnings("unchecked")
    private <S extends T> IRepository<S> getRepository(Class<S> clazz) {
        IRepository<? extends T> repository = subRepositories.get(clazz);
        if (repository == null) {
            return null;
        }
        return (IRepository<S>) repository; // Safe cast because of controlled registration
    }

    @Override
    public void create(T obj) {
        @SuppressWarnings("unchecked")
        Class<T> objClass = (Class<T>) obj.getClass();
        IRepository<T> repository = getRepository(objClass);
        if (repository == null) {
            throw new IllegalArgumentException("No repository registered for class: " + obj.getClass());
        }
        repository.create(obj);
    }

    @Override
    public T read(Integer id) {
        for (IRepository<? extends T> repository : subRepositories.values()) {
            T obj = repository.read(id); // Suppressing warning for cast
            if (obj != null) {
                return obj;
            }
        }
        return null; // Not found
    }

    @Override
    public void update(T obj) {
        @SuppressWarnings("unchecked")
        Class<T> objClass = (Class<T>) obj.getClass();
        IRepository<T> repository = getRepository(objClass);
        if (repository == null) {
            throw new IllegalArgumentException("No repository registered for class: " + obj.getClass());
        }
        repository.update(obj);
    }

    @Override
    public void delete(Integer id) {
        boolean deleted = false;
        for (IRepository<? extends T> repository : subRepositories.values()) {
            if (repository.read(id) != null) {
                repository.delete(id);
                deleted = true;
                break;
            }
        }
        if (!deleted) {
            throw new IllegalArgumentException("No object found with ID: " + id);
        }
    }

    @Override
    public List<T> getAll() {
        List<T> allItems = new ArrayList<>();
        for (IRepository<? extends T> repository : subRepositories.values()) {
            @SuppressWarnings("unchecked")
            List<T> items = (List<T>) repository.getAll();
            allItems.addAll(items);
        }
        return allItems;
    }
}