package com.project.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.project.entities.Seat;
import com.project.repositories.TemporalBookingsRepository;
import com.project.requestobjects.TemporalBooking;

@Component
public class CachedBookings implements TemporalBookingsRepository {
	
	private static Map<String, TemporalBooking> temporalSeatsList = new HashMap<>();

	public CachedBookings() {}
	
	@Override
	public TemporalBooking getTemporalBookingByUserId(String userId) {
		return temporalSeatsList.get(userId);
	}

	@Override
	public void remove(String userId) {
		temporalSeatsList.remove(userId);
	}

	@Override
	public void put(String userId, TemporalBooking temporalBooking) {
		temporalSeatsList.put(userId, temporalBooking);
	}
	
	@Override
	public Map<String, TemporalBooking> getTemporalBookingList() {
		return temporalSeatsList;
	}

	@Override
	public void removeSeat(String userId, Seat seat) {
		getTemporalBookingByUserId(userId).removeSeat(seat); 
	}

}
