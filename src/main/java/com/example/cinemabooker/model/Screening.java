package com.example.cinemabooker.model;

import com.example.cinemabooker.services.interfaces.EntityUpdateInterface;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Screenings")
public class Screening extends AbstractEntity implements EntityUpdateInterface<Screening> {
    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    private Instant screeningTime;

    @OneToMany(mappedBy = "screening", orphanRemoval = true, cascade = CascadeType.ALL)
            @OrderBy("position asc")
    List<SeatsRow> seatsRows = new ArrayList<>();

    public Screening() {
    }

    public Screening(Movie movie, Room room, Instant screeningTime) {
        this.movie = movie;
        this.room = room;
        this.screeningTime = screeningTime;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void addSeatsRow(SeatsRow seatsRow) {
        seatsRows.add(seatsRow);
        seatsRow.setScreening(this);
    }

    public List<SeatsRow> getSeatsRows() {
        return seatsRows;
    }

    public void setSeatsRows(List<SeatsRow> seatsRows) {
        this.seatsRows = seatsRows;
    }

    public Instant getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(Instant screeningTime) {
        this.screeningTime = screeningTime;
    }

    @Override
    public String toString() {
        return "Screening{" +
                "movie=" + movie +
                ", room=" + room +
                ", screeningTime=" + screeningTime +
                ", id='" + entityId + '\'' +
                "} ";
    }

    @Override
    public void update(Screening entity) {
//TODO
    }
}
