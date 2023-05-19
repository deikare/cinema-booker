package com.example.cinemabooker.controllers.representation.assemblers;

import com.example.cinemabooker.controllers.ReservationController;
import com.example.cinemabooker.model.Reservation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ReservationModelAssembler implements RepresentationModelAssembler<Reservation, EntityModel<Reservation>> {
    @Override
    public EntityModel<Reservation> toModel(Reservation entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ReservationController.class).one(entity.getId())).withSelfRel()
//                linkTo(methodOn(ReservationController.class))
                );
    }
}
