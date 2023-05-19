package com.example.cinemabooker.model;

import com.example.cinemabooker.services.interfaces.EntityInterface;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Movies")
public class Movie extends AbstractEntity implements EntityInterface {
    private String title;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "movie")
    private Set<Screening> screenings = new HashSet<>();

    public Movie() {
    }

    public Movie(String title) {
        this.title = title;
    }

    public void addScreening(Screening screening) {
        screenings.add(screening);
        screening.setMovie(this);
    }

    public String getTitle() {
        return title;
    }

    public Set<Screening> getScreenings() {
        return screenings;
    }
}
