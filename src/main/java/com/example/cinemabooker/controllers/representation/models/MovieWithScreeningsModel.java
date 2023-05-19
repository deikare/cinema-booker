package com.example.cinemabooker.controllers.representation.models;

import com.example.cinemabooker.model.Movie;
import com.example.cinemabooker.model.Screening;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;


public class MovieWithScreeningsModel extends RepresentationModel<MovieWithScreeningsModel> {
    private final String title;
    private final List<ScreeningModel> screenings = new LinkedList<>();

    public MovieWithScreeningsModel(Movie movie) {
        this.title = movie.getTitle();
//        screenings.forEach(screening -> this.screenings.add(new ScreeningModel(screening)));
    }

    public String getTitle() {
        return title;
    }

    public void addScreening(ScreeningModel screeningModel) {
        screenings.add(screeningModel);
    }

    public List<ScreeningModel> getScreenings() {
        return screenings;
    }

    public static class ScreeningModel extends RepresentationModel<ScreeningModel> {
        private final Instant screeningTime;

        public ScreeningModel(Screening screening) {
            this.screeningTime = screening.getScreeningTime();
        }

        public Instant getScreeningTime() {
            return screeningTime;
        }
    }


}
