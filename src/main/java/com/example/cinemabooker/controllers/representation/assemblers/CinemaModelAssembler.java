package com.example.cinemabooker.controllers.representation.assemblers;

import com.example.cinemabooker.controllers.CinemaController;
import com.example.cinemabooker.controllers.ControllerDefaults;
import com.example.cinemabooker.controllers.RoomController;
import com.example.cinemabooker.model.Cinema;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class CinemaModelAssembler implements RepresentationModelAssembler<Cinema, EntityModel<Cinema>> {
    @Override
    public EntityModel<Cinema> toModel(Cinema entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CinemaController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(CinemaController.class).all(ControllerDefaults.PAGE_NUMBER, ControllerDefaults.PAGE_SIZE)).withRel(ControllerDefaults.ALL_LINK_REL),
                linkTo(methodOn(RoomController.class).all(ControllerDefaults.PAGE_NUMBER, ControllerDefaults.PAGE_SIZE, entity.getId())).withRel("rooms")
        );
    }
}
