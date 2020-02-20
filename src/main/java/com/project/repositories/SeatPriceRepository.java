package com.project.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.SeatPrice;

public interface SeatPriceRepository extends JpaRepository<SeatPrice, LocalDateTime>{
	
}
