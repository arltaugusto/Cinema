package com.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Entities.Seat;
import com.project.Entities.SeatPK;

public interface SeatRepository extends JpaRepository<Seat, SeatPK> {

}
