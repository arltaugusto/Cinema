package com.project.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.project.repositories.TemporalBookingsRepository;
import com.project.requestobjects.TemporalSeats;

@Component
public class CachedBookings implements TemporalBookingsRepository {
	
	private static Map<String, TemporalSeats> temporalSeatsList = new HashMap<>();

	public CachedBookings() {}
	
	@Override
	@Cacheable("bookings")
	public TemporalSeats getTemporalSeatsByUserId(String userId) {
		return temporalSeatsList.get(userId);
	}

	@Override
	public void remove(String userId, TemporalSeats temporalSeats) {
		temporalSeatsList.remove(userId, temporalSeats);
	}

	@Override
	public void updateStatus(String userId, TemporalSeats temporalSeats) {
		temporalSeatsList.put(userId, temporalSeats);
	}
	
	@Override
	public void replace(String userId, TemporalSeats temporalSeats) {
		temporalSeatsList.replace(userId, temporalSeats);
	}
	
	@Cacheable("temporalSeats")
	public Map<String, TemporalSeats> getTemporalSeatsList() {
		return temporalSeatsList;
	}

}
