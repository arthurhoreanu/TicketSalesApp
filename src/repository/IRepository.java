package repository;

import model.Identifiable;

import java.util.List;

public interface IRepository<T extends Identifiable> {
    void create(T obj);

    // Changed the return type of read to void
    void read(Integer id);

    void update(T obj);

    void delete(Integer id);

    List<T> getAll();
}
