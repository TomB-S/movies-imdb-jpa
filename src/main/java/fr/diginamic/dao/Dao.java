package fr.diginamic.dao;

import java.util.List;

public interface Dao<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();

}
