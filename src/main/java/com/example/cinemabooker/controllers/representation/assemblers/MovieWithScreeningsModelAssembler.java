package com.example.cinemabooker.controllers.representation.assemblers;

import com.example.cinemabooker.controllers.ControllerDefaults;
import com.example.cinemabooker.controllers.MovieController;
import com.example.cinemabooker.controllers.ScreeningController;
import com.example.cinemabooker.controllers.representation.models.MovieWithScreeningsModel;
import com.example.cinemabooker.model.Movie;
import com.example.cinemabooker.model.Screening;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MovieWithScreeningsModelAssembler implements RepresentationModelAssembler<Screening, MovieWithScreeningsModel> {
    @Override
    public MovieWithScreeningsModel toModel(Screening entity) {
        return null;
    }

    @Override
    public CollectionModel<MovieWithScreeningsModel> toCollectionModel(Iterable<? extends Screening> entities) {
        LinkedHashMap<String, MovieWithScreeningsModel> movies = new LinkedHashMap<>();

        StreamSupport.stream(entities.spliterator(), false).forEach(screening -> {
            Movie movie = screening.getMovie();
            String key = movie.getId();
            MovieWithScreeningsModel movieModel = movies.get(key);
            if (movieModel == null) {
                movieModel = new MovieWithScreeningsModel(movie);
                movies.put(key, movieModel);
                movieModel.add(linkTo(methodOn(MovieController.class).one(movie.getId())).withSelfRel());
            }

            MovieWithScreeningsModel.ScreeningModel screeningModel = new MovieWithScreeningsModel.ScreeningModel(screening);
            screeningModel.add(linkTo(methodOn(ScreeningController.class).one(screening.getId())).withSelfRel());
            movieModel.addScreening(screeningModel);
        });

        List<MovieWithScreeningsModel> convertedEntities = movies.values().stream().toList();

        return CollectionModel.of(convertedEntities,
                linkTo(methodOn(MovieController.class).all(ControllerDefaults.PAGE_NUMBER, ControllerDefaults.PAGE_SIZE, null, null)).withRel(ControllerDefaults.ALL_LINK_REL)
        );
    }
}
