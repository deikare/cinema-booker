package com.example.cinemabooker.services;

import com.example.cinemabooker.services.exceptions.IdAlreadyDefinedException;
import com.example.cinemabooker.services.interfaces.EntityUpdateInterface;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseServiceWithUpdate<T extends EntityUpdateInterface<T>, R extends JpaRepository<T, String>> extends BaseService<T, R> {
    public BaseServiceWithUpdate(R repository, Logger logger) {
        super(repository, logger);
    }

    void update(String id, T newEntity) throws IdAlreadyDefinedException {
        repository.findById(id)
                .map(t -> { //update if exists
                    t.update(newEntity);
                    logger.info("Entity updated, current state: " + t);
                    return save(t);
                })
                .orElseGet(() -> { //create otherwise
                    create(newEntity);
                    logger.info("Entity not found, new entity created");
                    return newEntity;
                });
    }
}
