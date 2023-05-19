package com.example.cinemabooker.services;

import com.example.cinemabooker.model.Movie;
import com.example.cinemabooker.repositories.MovieRepository;
import com.example.cinemabooker.repositories.ScreeningRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MovieService extends BaseService<Movie, MovieRepository> {
    private final ScreeningRepository screeningRepository;
    public MovieService(MovieRepository repository, ScreeningRepository screeningRepository) {
        super(repository, LoggerFactory.getLogger(MovieService.class));
        this.screeningRepository = screeningRepository;
    }
}
