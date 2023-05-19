package com.example.cinemabooker.model;

import com.example.cinemabooker.services.interfaces.EntityInterface;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Rooms")
public class Room extends AbstractEntity implements EntityInterface {
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    private Long roomNumber;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "room")
    Set<Screening> screenings = new HashSet<>();

    public Room(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Room() {

    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public void addScreening(Screening screening) {
        screenings.add(screening);
        screening.setRoom(this);
    }

    public Long getRoomNumber() {
        return roomNumber;
    }
}
