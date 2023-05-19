package com.example.cinemabooker.controllers;

import com.example.cinemabooker.services.BaseService;
import com.example.cinemabooker.services.interfaces.EntityInterface;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BaseControllerWithPost<T extends EntityInterface, R extends JpaRepository<T, String>, S extends BaseService<T, R>, A extends RepresentationModelAssembler<T, EntityModel<T>>> extends BaseControllerWithGetAll<T, R, S, A> {
    protected BaseControllerWithPost(Logger logger, S service, PagedResourcesAssembler<T> pagedResourcesAssembler, A modelAssembler) {
        super(logger, service, pagedResourcesAssembler, modelAssembler);
    }

    @PostMapping
    public ResponseEntity<?> newEntity(@RequestBody T entity) {
        logger.info("Received request post entity" + entity);

        EntityModel<T> entityModel = modelAssembler.toModel(service.create(entity));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
