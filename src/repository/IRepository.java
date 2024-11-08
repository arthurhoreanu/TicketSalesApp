package repository;

import model.Identifiable;

import java.util.List;

import java.util.Optional;


public interface IRepository<T extends Identifiable> {
    void create(T obj);

    //TODO talk about this modification from only void read to this..
    //optional instead of returning null
    Optional<T> read(Integer id);

    void update(T obj);

    void delete(Integer id);

    List<T> getAll();
}