package repository;

import model.Identifiable;

import java.util.List;

public interface IRepository<T extends Identifiable> {
    void create(T obj);

    void read(Integer id);

    void update(T obj);

    void delete(Integer id);

    List<T> getAll();
}