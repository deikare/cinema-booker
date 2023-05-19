package com.example.cinemabooker.controllers.representation.models;

import com.example.cinemabooker.model.Screening;
import com.example.cinemabooker.model.Seat;
import com.example.cinemabooker.model.SeatsRow;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class ScreeningWithSeatsModel extends RepresentationModel<ScreeningWithSeatsModel> {
    private final Instant screeningTime;

    private final List<SeatsRowModel> seatRows;

    public ScreeningWithSeatsModel(Screening screening) {
        this.seatRows = new LinkedList<>();
        this.screeningTime = screening.getScreeningTime();
        screening.getSeatsRows().forEach(seatsRow -> this.seatRows.add(new SeatsRowModel(seatsRow)));
    }

    public Instant getScreeningTime() {
        return screeningTime;
    }

    public List<SeatsRowModel> getSeatRows() {
        return seatRows;
    }

    private class SeatsRowModel extends RepresentationModel<SeatsRowModel> {
        private final long row;

        private final List<SeatModel> seats;

        public SeatsRowModel(SeatsRow seatsRow) {
            this.row = seatsRow.getPosition();
            this.seats = new LinkedList<>();
            seatsRow.getSeats().forEach(seat -> this.seats.add(new SeatModel(seat)));
        }

        public long getRow() {
            return row;
        }

        public List<SeatModel> getSeats() {
            return seats;
        }

        private class SeatModel extends RepresentationModel<SeatModel> {
            private final long number;
            private final boolean free;

            public SeatModel(Seat seat) {
                this.number = seat.getPosition();
                this.free = seat.getReservation() == null;
            }

            public long getNumber() {
                return number;
            }

            public boolean isFree() {
                return free;
            }
        }
    }
}
