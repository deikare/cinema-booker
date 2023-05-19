package com.example.cinemabooker.model;

import com.example.cinemabooker.services.interfaces.EntityInterface;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Reservations")
public class Reservation extends AbstractEntity implements EntityInterface {
    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reservation")
    private Set<Seat> seats = new HashSet<>();


    public Reservation(Screening screening, String name, String surname) {
        this.screening = screening;
        this.expirationTime = screening.getScreeningTime();
        this.name = name;
        this.surname = surname;
        this.cost = 0;
    }

    private Instant expirationTime;
    private String name;
    private String surname;

    private double cost;

    public Reservation() {
    }

    public void addSeat(Seat seat, SeatType seatType) {
        seats.add(seat);
        seat.setReservation(this);
        seat.setType(seatType);
        cost += getCost(seatType);
    }

    private double getCost(SeatType seatType) {
        return seatType == SeatType.ADULT ? 25 : seatType == SeatType.STUDENT ? 18 : 12.5;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "screening=" + screening +
                ", seats=" + seats +
                ", expirationTime=" + expirationTime +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", cost=" + cost +
                ", entityId='" + entityId + '\'' +
                "} " + super.toString();
    }
}
