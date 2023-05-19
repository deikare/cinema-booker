package com.example.cinemabooker;

import com.example.cinemabooker.model.Cinema;
import com.example.cinemabooker.model.Movie;
import com.example.cinemabooker.model.Room;
import com.example.cinemabooker.model.Screening;
import com.example.cinemabooker.repositories.*;
import com.example.cinemabooker.services.CinemaService;
import com.example.cinemabooker.services.ScreeningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
public class InitializeDatabase {
    @Bean
    CommandLineRunner initDatabase(CinemaService cinemaService, MovieRepository movieRepository, ReservationRepository reservationRepository, RoomRepository roomRepository, ScreeningService screeningService) {
        return args -> {
            Cinema cinema = new Cinema("c");
            cinemaService.save(cinema);

            Long[] entityNumbers = {1L, 2L, 3L};

            ArrayList<Room> rooms = Arrays.stream(entityNumbers).map(roomNumber -> {
                Room room = new Room(roomNumber);
                cinema.addRoom(room);
                cinemaService.save(cinema);
                return roomRepository.save(room);
            }).collect(Collectors.toCollection(ArrayList::new));

            ArrayList<Movie> movies = Arrays.stream(entityNumbers).map(movieNumber -> {
                Movie movie = new Movie("m" + movieNumber);
                return movieRepository.save(movie);
            }).collect(Collectors.toCollection(ArrayList::new));

            Collections.shuffle(movies);
            int i = 0;
            for (Room room : rooms) {
                for (int j = 0; j < 2; j++) {
                    screeningService.createScreening(movies.get(i), room, Instant.now().plus(i + 2, ChronoUnit.HOURS), 5, 10);
                    i++;
                    if (i == movies.size())
                        i = 0;
                }
            }
        };
    }
}
