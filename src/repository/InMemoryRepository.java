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

    @Override
    public void read(Integer id) {
        data.get(id);
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