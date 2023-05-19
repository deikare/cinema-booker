package com.example.cinemabooker.controllers;

import com.example.cinemabooker.controllers.representation.assemblers.RoomRepresentationAssembler;
import com.example.cinemabooker.model.Room;
import com.example.cinemabooker.repositories.RoomRepository;
import com.example.cinemabooker.services.RoomService;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController extends BaseControllerWithGetOne<Room, RoomRepository, RoomService, RoomRepresentationAssembler> {

    protected RoomController(RoomService service, PagedResourcesAssembler<Room> pagedResourcesAssembler, RoomRepresentationAssembler modelAssembler) {
        super(LoggerFactory.getLogger(RoomController.class), service, pagedResourcesAssembler, modelAssembler);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Room>>> all(@RequestParam(defaultValue = ControllerDefaults.PAGE_NUMBER_AS_STRING) int page, @RequestParam(defaultValue = ControllerDefaults.PAGE_SIZE_AS_STRING) int size, @RequestParam(required = false) String cinemaId) {
        Page<Room> rooms;
        logger.info("Received request get paged entities: page=" + page + ", size=" + size);
        Pageable pageable = PageRequest.of(page, size);
        if (cinemaId != null)
            rooms = service.findAll(cinemaId, pageable);
        else rooms = service.findAll(pageable);

        PagedModel<EntityModel<Room>> entitiesView = pagedResourcesAssembler.toModel(rooms, modelAssembler);
        return new ResponseEntity<>(entitiesView, HttpStatus.OK);
    }
}
