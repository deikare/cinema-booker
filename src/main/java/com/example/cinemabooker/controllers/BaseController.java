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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BaseController<T extends EntityInterface, R extends JpaRepository<T, String>, S extends BaseService<T, R>, A extends RepresentationModelAssembler<T, EntityModel<T>>>  {
    protected final Logger logger;
    protected final S service;
    protected final PagedResourcesAssembler<T> pagedResourcesAssembler;
    protected final A modelAssembler;

    protected BaseController(Logger logger, S service, PagedResourcesAssembler<T> pagedResourcesAssembler, A modelAssembler) {
        this.logger = logger;
        this.service = service;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.modelAssembler = modelAssembler;
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable String id) {
        logger.info("Received request delete one entity: id=" + id);
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
