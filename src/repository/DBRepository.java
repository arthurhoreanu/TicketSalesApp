package repository;

import model.Identifiable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class DBRepository<T extends Identifiable> implements IRepository<T> {
    private final EntityManagerFactory entityManagerFactory;
    private final Class<T> entityClass;

    public DBRepository(EntityManagerFactory entityManagerFactory, Class<T> entityClass) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityClass = entityClass;
    }

    @Override
    public void create(T entity) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public T read(Integer id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> getAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(T entity) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}