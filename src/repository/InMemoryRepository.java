package repository;

import model.Identifiable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<T extends Identifiable> implements IRepository<T> {
    private final Map<Integer, T> data = new HashMap<>();

    @Override
    public void create(T obj) {
        data.putIfAbsent(obj.getID(), obj);
    }

    //TODO I'm nor sure if this is good since return null, suggestion from intellij
    @Override
    public Optional<T> read(Integer id) {
        data.get(id);
        return null;
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