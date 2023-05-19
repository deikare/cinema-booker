package com.example.cinemabooker.services;

import com.example.cinemabooker.model.SeatsRow;
import com.example.cinemabooker.repositories.SeatsRowRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SeatsRowService extends BaseService<SeatsRow, SeatsRowRepository> {
    public SeatsRowService(SeatsRowRepository repository) {
        super(repository, LoggerFactory.getLogger(SeatsRowService.class));
    }
}
