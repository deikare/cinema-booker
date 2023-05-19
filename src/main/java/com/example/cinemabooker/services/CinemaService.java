package com.example.cinemabooker.services;

import com.example.cinemabooker.model.Cinema;
import com.example.cinemabooker.repositories.CinemaRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CinemaService extends BaseService<Cinema, CinemaRepository> {
    public CinemaService(CinemaRepository repository) {
        super(repository, LoggerFactory.getLogger(CinemaService.class));
    }
}
