package com.example.cinemabooker.services;

import com.example.cinemabooker.model.Seat;
import com.example.cinemabooker.repositories.SeatRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SeatService extends BaseService<Seat, SeatRepository> {
    public SeatService(SeatRepository repository) {
        super(repository, LoggerFactory.getLogger(SeatService.class));
    }
}
