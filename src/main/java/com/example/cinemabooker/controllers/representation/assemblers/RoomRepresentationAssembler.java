package com.example.cinemabooker.controllers.representation.assemblers;

import com.example.cinemabooker.controllers.CinemaController;
import com.example.cinemabooker.controllers.ControllerDefaults;
import com.example.cinemabooker.controllers.RoomController;
import com.example.cinemabooker.model.Room;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class RoomRepresentationAssembler implements RepresentationModelAssembler<Room, EntityModel<Room>> {
    @Override
    public EntityModel<Room> toModel(Room entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(RoomController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(RoomController.class).all(ControllerDefaults.PAGE_NUMBER, ControllerDefaults.PAGE_SIZE, null)).withRel(ControllerDefaults.ALL_LINK_REL),
                linkTo(methodOn(CinemaController.class).one(entity.getCinema().getId())).withRel("cinema"),
                linkTo(methodOn(RoomController.class).all(ControllerDefaults.PAGE_NUMBER, ControllerDefaults.PAGE_SIZE, entity.getCinema().getId())).withRel("all_with_cinema")
        );
    }
}
