package com.example.cinemabooker.controllers.representation.assemblers;

import com.example.cinemabooker.controllers.ControllerDefaults;
import com.example.cinemabooker.controllers.MovieController;
import com.example.cinemabooker.controllers.RoomController;
import com.example.cinemabooker.controllers.ScreeningController;
import com.example.cinemabooker.model.Screening;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ScreeningModelAssembler implements RepresentationModelAssembler<Screening, EntityModel<Screening>> {
    @Override
    public EntityModel<Screening> toModel(Screening entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ScreeningController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(ScreeningController.class).all(ControllerDefaults.PAGE_NUMBER, ControllerDefaults.PAGE_SIZE, null, null, null)).withRel(ControllerDefaults.ALL_LINK_REL),
                linkTo(methodOn(ScreeningController.class).all(ControllerDefaults.PAGE_NUMBER, ControllerDefaults.PAGE_SIZE, null, null, entity.getMovie().getId())).withRel("all_for_movie"),
                linkTo(methodOn(MovieController.class).one(entity.getMovie().getId())).withRel("movie"),
                linkTo(methodOn(RoomController.class).one(entity.getRoom().getId())).withRel("room")
        );
    }
}
