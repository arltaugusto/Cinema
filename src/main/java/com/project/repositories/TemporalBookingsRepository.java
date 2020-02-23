package com.project.repositories;

import java.util.Map;

import com.project.entities.Seat;
import com.project.requestobjects.TemporalSeats;

public interface TemporalBookingsRepository {
	
	public TemporalSeats getTemporalSeatsByUserId(String userId);
	
	public void remove(String userId);
	
	public void removeSeat(String userId, Seat seat);

	public void put(String userId, TemporalSeats temporalSeats);
	
	public Map<String, TemporalSeats> getTemporalSeatsList();
	
}
