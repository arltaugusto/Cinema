package com.project.repositories;

import java.util.Map;

import com.project.requestobjects.TemporalSeats;

public interface TemporalBookingsRepository {
	
	public TemporalSeats getTemporalSeatsByUserId(String userId);
	
	public void remove(String userId);
	
	public void addSeat(String userId, TemporalSeats temporalSeats);
	
	public Map<String, TemporalSeats> getTemporalSeatsList();
	
	public void put(String userId, TemporalSeats temporalSeats);
}