package com.example.cinemabooker.controllers;

import com.example.cinemabooker.services.BaseService;
import com.example.cinemabooker.services.interfaces.EntityInterface;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class BaseControllerWithGetOne<T extends EntityInterface, R extends JpaRepository<T, String>, S extends BaseService<T, R>, A extends RepresentationModelAssembler<T, EntityModel<T>>> extends BaseController<T, R, S, A> {
    protected BaseControllerWithGetOne(Logger logger, S service, PagedResourcesAssembler<T> pagedResourcesAssembler, A modelAssembler) {
        super(logger, service, pagedResourcesAssembler, modelAssembler);
    }

    @GetMapping("/{id}")
    public EntityModel<T> one(@PathVariable String id) {
        logger.info("Received request get one entity: id=" + id);
        T entity = service.find(id);
        return modelAssembler.toModel(entity);
    }
}
