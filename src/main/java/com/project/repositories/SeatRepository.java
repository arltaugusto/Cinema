package com.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Seat;
import com.project.entities.SeatPK;

public interface SeatRepository extends JpaRepository<Seat, SeatPK> {

}
