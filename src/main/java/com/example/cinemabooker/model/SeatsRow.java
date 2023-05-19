package com.example.cinemabooker.model;

import com.example.cinemabooker.services.interfaces.EntityInterface;
import jakarta.persistence.*;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SeatsRows")
public class SeatsRow extends AbstractEntity implements EntityInterface {
    private int position;

    @OneToMany(mappedBy = "row", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("position asc")
    private List<Seat> seats = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id")
    private Screening screening;

    public SeatsRow() {
    }

    public SeatsRow(int position) {
        this.position = position;
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setRow(this);
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
