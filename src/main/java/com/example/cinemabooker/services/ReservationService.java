package com.example.cinemabooker.services;

import com.example.cinemabooker.controllers.requests.ReservationRequest;
import com.example.cinemabooker.model.*;
import com.example.cinemabooker.repositories.ReservationRepository;
import com.example.cinemabooker.repositories.ScreeningRepository;
import com.example.cinemabooker.repositories.SeatRepository;
import com.example.cinemabooker.services.exceptions.BadReservationRequestException;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService extends BaseService<Reservation, ReservationRepository> {
    private final SeatRepository seatRepository;

    private final ScreeningRepository screeningRepository;

    public ReservationService(ReservationRepository repository, SeatRepository seatRepository, ScreeningRepository screeningRepository) {
        super(repository, LoggerFactory.getLogger(ReservationService.class));
        this.seatRepository = seatRepository;
        this.screeningRepository = screeningRepository;
    }

    public static final long PRE_SCREENING_DURATION = 15L;

    @Transactional
    public Reservation validateAndSaveReservation(ReservationRequest reservationRequest) throws BadReservationRequestException {
        String screeningId = reservationRequest.getScreeningId();

        Screening screening = findFetchedScreening(screeningId);

        Instant threshold = screening.getScreeningTime().minus(PRE_SCREENING_DURATION, ChronoUnit.MINUTES);
        if (Instant.now().isAfter(threshold)) {
            String msg = "Request has been sent too late, timestamp limit was: " + threshold;
            logger.info(msg);
            throw new BadReservationRequestException(msg);
        }

        Reservation reservation = new Reservation(screening, reservationRequest.getName(), reservationRequest.getSurname());

        List<Seat> seatsToSave = getSeatsWithUpdatedReservation(reservationRequest.getSeats(), reservation, screening);

        seatRepository.saveAll(seatsToSave);
        reservation = repository.save(reservation);
        logger.info("Successfully created new " + reservation);
        return reservation;
    }

    private Screening findFetchedScreening(String screeningId) throws BadReservationRequestException {
        return screeningRepository.findByIdAndFetchSeatsRows(screeningId).orElseThrow(() -> {
            String msg = "Screening{id=" + screeningId + "} not found";
            logger.info(msg);
            return new BadReservationRequestException(msg);
        });
    }

    private List<Seat> getSeatsWithUpdatedReservation(Map<Long, ReservationRequest.SeatReservation> seatReservationMap, Reservation reservation, Screening screening) throws BadReservationRequestException {
        List<Seat> result = new ArrayList<>();

        seatReservationMap.forEach((rowPosition, seatRequest) -> {
            SeatsRow seatsRow = getSeatsRowWithPosition(screening.getSeatsRows(), rowPosition);

            int first = seatRequest.getFirst();
            List<SeatType> types = seatRequest.getTypes();
            int size = types.size();
            int last = size + first - 1;

            List<Seat> seats = seatsRow.getSeats();
            if (last > seats.size()) {
                String msg = "Too many seats in row " + rowPosition + ": received request to reserve seats in range [" + first + "," + last + "], last seat number = " + (seats.size());
                logger.info(msg);
                throw new BadReservationRequestException(msg);
            }

            first--;
            for (int i = 0; i < size; i++) {
                Seat seat = seats.get(first);
                if (seat.getReservation() != null) {
                    String msg = "seat{row=" + rowPosition + ",number=" + seat.getPosition() + "} is already taken";
                    logger.info(msg);
                    throw new BadReservationRequestException(msg);
                }

                reservation.addSeat(seat, types.get(i));
                result.add(seat);
                first++;
            }
        });

        return result;
    }

    private SeatsRow getSeatsRowWithPosition(List<SeatsRow> seatsRows, long rowPosition) throws BadReservationRequestException {
        if (rowPosition > seatsRows.size()) {
            String msg = "Seats row with position=" + rowPosition + " not found";
            logger.info(msg);
            throw new BadReservationRequestException(msg);
        }

        return seatsRows.get((int) rowPosition - 1);
    }
}
