package com.example.cinemabooker.repositories;

import com.example.cinemabooker.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    Page<Room> findAllByCinemaId(String cinemaId, Pageable pageable);
}
