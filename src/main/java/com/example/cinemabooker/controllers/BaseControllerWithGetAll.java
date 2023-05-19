package com.example.cinemabooker.controllers;

import com.example.cinemabooker.services.BaseService;
import com.example.cinemabooker.services.interfaces.EntityInterface;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class BaseControllerWithGetAll<T extends EntityInterface, R extends JpaRepository<T, String>, S extends BaseService<T, R>, A extends RepresentationModelAssembler<T, EntityModel<T>>> extends BaseControllerWithGetOne<T, R, S, A> {
    protected BaseControllerWithGetAll(Logger logger, S service, PagedResourcesAssembler<T> pagedResourcesAssembler, A modelAssembler) {
        super(logger, service, pagedResourcesAssembler, modelAssembler);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<T>>> all(@RequestParam(defaultValue = ControllerDefaults.PAGE_NUMBER_AS_STRING) int page, @RequestParam(defaultValue = ControllerDefaults.PAGE_SIZE_AS_STRING) int size) {
        Pageable pageable = PageRequest.of(page, size);
        logger.info("Received request get paged entities: page=" + page + ", size=" + size);
        Page<T> entityPage = service.findAll(pageable);
        PagedModel<EntityModel<T>> entitiesView = pagedResourcesAssembler.toModel(entityPage, modelAssembler);
        return new ResponseEntity<>(entitiesView, HttpStatus.OK);
    }
}
