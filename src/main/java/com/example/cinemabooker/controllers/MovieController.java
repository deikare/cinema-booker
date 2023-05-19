package com.example.cinemabooker.controllers;

import com.example.cinemabooker.controllers.representation.assemblers.MovieModelAssembler;
import com.example.cinemabooker.controllers.representation.assemblers.MovieWithScreeningsModelAssembler;
import com.example.cinemabooker.controllers.representation.models.MovieWithScreeningsModel;
import com.example.cinemabooker.model.Movie;
import com.example.cinemabooker.model.Screening;
import com.example.cinemabooker.repositories.MovieRepository;
import com.example.cinemabooker.services.MovieService;
import com.example.cinemabooker.services.ReservationService;
import com.example.cinemabooker.services.ScreeningService;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController extends BaseControllerWithGetOne<Movie, MovieRepository, MovieService, MovieModelAssembler> {
    private final MovieWithScreeningsModelAssembler movieWithScreeningsModelAssembler;
    private final PagedResourcesAssembler<MovieWithScreeningsModel> movieWithScreeningsPagedResourcesAssembler;
    private final ScreeningService screeningService;

    protected MovieController(MovieService service, PagedResourcesAssembler<Movie> pagedResourcesAssembler, MovieModelAssembler modelAssembler, MovieWithScreeningsModelAssembler movieWithScreeningsModelAssembler, PagedResourcesAssembler<MovieWithScreeningsModel> movieWithScreeningsPagedResourcesAssembler, ScreeningService screeningService) {
        super(LoggerFactory.getLogger(MovieController.class), service, pagedResourcesAssembler, modelAssembler);
        this.movieWithScreeningsModelAssembler = movieWithScreeningsModelAssembler;
        this.movieWithScreeningsPagedResourcesAssembler = movieWithScreeningsPagedResourcesAssembler;
        this.screeningService = screeningService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<MovieWithScreeningsModel>>> all(@RequestParam(defaultValue = ControllerDefaults.PAGE_NUMBER_AS_STRING) int page, @RequestParam(defaultValue = ControllerDefaults.PAGE_SIZE_AS_STRING) int size, @RequestParam(required = false) Instant start, @RequestParam(required = false) Instant end) {
        if (start == null)
            start = Instant.EPOCH;
        if (end == null)
            end = ControllerDefaults.END;
        end = end.minus(ReservationService.PRE_SCREENING_DURATION, ChronoUnit.MINUTES);
        logger.info("Received get request for all movies where screenings{start=" + start + ", end=" + end + "}, page=" + page + ", size=" + size);
        Sort sort = Sort.by("movie.title").ascending().and(Sort.by("screeningTime").ascending());
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Screening> screenings = screeningService.findAllBetween(start, end, pageable);
        CollectionModel<MovieWithScreeningsModel> moviesCollectionModel = movieWithScreeningsModelAssembler.toCollectionModel(screenings.toList());
        pageable = PageRequest.of(page, size); //override so there is no sort param in response
        Page<MovieWithScreeningsModel> pagedMovies = new PageImpl<>(new ArrayList<>(moviesCollectionModel.getContent()), pageable, screenings.getTotalElements());
        PagedModel<EntityModel<MovieWithScreeningsModel>> pagedMoviesView = movieWithScreeningsPagedResourcesAssembler.toModel(pagedMovies);

        return new ResponseEntity<>(pagedMoviesView, HttpStatus.OK);
    }
}
