package com.example.cinemabooker.model;

import com.example.cinemabooker.services.interfaces.EntityInterface;
import jakarta.persistence.*;


@Entity
@Table(name = "Seats")
public class Seat extends AbstractEntity implements EntityInterface {
    private long position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seats_row_id")
    private SeatsRow row;

    @ManyToOne(optional = true)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Enumerated
    private SeatType type;


    public Seat() {

    }

    public Seat(long position) {
        this.position = position;
    }

    public SeatsRow getRow() {
        return row;
    }

    public void setRow(SeatsRow row) {
        this.row = row;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public SeatType getType() {
        return type;
    }

    public void setType(SeatType type) {
        this.type = type;
    }


}
