package com.proyect.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.Entities.Seat;
import com.proyect.Entities.SeatPK;

public interface SeatRepository extends JpaRepository<Seat, SeatPK> {

}
