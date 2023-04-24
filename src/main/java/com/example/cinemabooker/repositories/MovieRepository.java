package com.example.cinemabooker.repositories;

import com.example.cinemabooker.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    Page<Movie> findAllByScreeningsBetweenOrderByTitleAscScreeningsAsc(Pageable pageable);
}
