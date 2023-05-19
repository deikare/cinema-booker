package com.example.cinemabooker.repositories;

import com.example.cinemabooker.model.SeatsRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatsRowRepository extends JpaRepository<SeatsRow, String> {
}
