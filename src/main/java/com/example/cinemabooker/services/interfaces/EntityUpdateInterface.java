package com.example.cinemabooker.services.interfaces;

public interface EntityUpdateInterface<T> extends EntityInterface {
    void setId(String id);
    void update(T entity);
}
