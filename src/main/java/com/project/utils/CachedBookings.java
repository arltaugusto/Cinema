package com.project.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.project.entities.Seat;
import com.project.repositories.TemporalBookingsRepository;
import com.project.requestobjects.TemporalSeats;

@Component
public class CachedBookings implements TemporalBookingsRepository {
	
	private static Map<String, TemporalSeats> temporalSeatsList = new HashMap<>();

	public CachedBookings() {}
	
	@Override
	public TemporalSeats getTemporalSeatsByUserId(String userId) {
		return temporalSeatsList.get(userId);
	}

	@Override
	public void remove(String userId) {
		temporalSeatsList.remove(userId);
	}

	@Override
	public void put(String userId, TemporalSeats temporalSeats) {
		temporalSeatsList.put(userId, temporalSeats);
	}
	
	@Override
	public Map<String, TemporalSeats> getTemporalSeatsList() {
		return temporalSeatsList;
	}

	@Override
	public void removeSeat(String userId, Seat seat) {
		getTemporalSeatsByUserId(userId).removeSeat(seat); 
	}

}
