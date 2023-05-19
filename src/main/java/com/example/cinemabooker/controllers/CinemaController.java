package com.example.cinemabooker.controllers;

import com.example.cinemabooker.controllers.representation.assemblers.CinemaModelAssembler;
import com.example.cinemabooker.model.Cinema;
import com.example.cinemabooker.repositories.CinemaRepository;
import com.example.cinemabooker.services.CinemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cinemas")
public class CinemaController extends BaseControllerWithPost<Cinema, CinemaRepository, CinemaService, CinemaModelAssembler> {
    public CinemaController(CinemaService service, PagedResourcesAssembler<Cinema> pagedResourcesAssembler, CinemaModelAssembler modelAssembler) {
        super(LoggerFactory.getLogger(CinemaController.class), service, pagedResourcesAssembler, modelAssembler);
    }
}
