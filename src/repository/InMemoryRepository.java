package repository;

import model.Identifiable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<T extends Identifiable> implements IRepository<T> {
    private final Map<Integer, T> data = new HashMap<>();

    @Override
    public void create(T obj) {
        data.putIfAbsent(obj.getID(), obj);
    }

    // Since read is void, we remove the return statement
    @Override
    public void read(Integer id) {
        // No return value - this method now effectively does nothing meaningful
        data.get(id); // This line is redundant and doesn't have any effect
    }

    @Override
    public void update(T obj) {
        data.replace(obj.getID(), obj);
    }

    @Override
    public void delete(Integer id) {
        data.remove(id);
    }

    @Override
    public List<T> getAll() {
        return data.values().stream().toList();
    }
}
