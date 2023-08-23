package com.example.BMSAug23.repositories;

import com.example.BMSAug23.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatsRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findByIdIn(List<Integer> seatIds);
}
