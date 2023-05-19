package com.example.cinemabooker.controllers;

import com.example.cinemabooker.controllers.requests.ReservationRequest;
import com.example.cinemabooker.controllers.representation.assemblers.ReservationModelAssembler;
import com.example.cinemabooker.model.Reservation;
import com.example.cinemabooker.repositories.ReservationRepository;
import com.example.cinemabooker.services.ReservationService;
import com.example.cinemabooker.services.exceptions.BadReservationRequestException;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController extends BaseControllerWithGetOne<Reservation, ReservationRepository, ReservationService, ReservationModelAssembler> {
    protected ReservationController(ReservationService service, PagedResourcesAssembler<Reservation> pagedResourcesAssembler, ReservationModelAssembler modelAssembler) {
        super(LoggerFactory.getLogger(ReservationController.class), service, pagedResourcesAssembler, modelAssembler);
    }

    @PostMapping
    public ResponseEntity<?> newEntity(@RequestBody @Valid ReservationRequest reservationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));

            logger.info("Received request post, invalid request: " + msg);
            throw new BadReservationRequestException(msg);
        }
        logger.info("Received request post " + reservationRequest);
        Reservation reservation;
        ResponseEntity<?> response;

        try {
            reservation = service.validateAndSaveReservation(reservationRequest);
            EntityModel<Reservation> reservationModel = modelAssembler.toModel(reservation);
            response = ResponseEntity
                    .created(reservationModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(reservationModel);
        } catch (BadReservationRequestException e) {
            response = ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
        logger.info("Post response: " + response);

        return response;
    }
}
