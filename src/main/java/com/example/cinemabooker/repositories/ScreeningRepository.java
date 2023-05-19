package com.example.cinemabooker.repositories;

import com.example.cinemabooker.model.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, String> {
    Page<Screening> findAllByScreeningTimeBetween(Instant start, Instant end, Pageable pageable);
    Page<Screening> findAllByMovieId(String movieId, Pageable pageable);
    @Query("SELECT s FROM Screening s JOIN FETCH s.seatsRows WHERE s.entityId = (:id)")
    Optional<Screening> findByIdAndFetchSeatsRows(String id);
}
