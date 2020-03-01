package com.project.repositories;

import java.util.Map;

import com.project.entities.Seat;
import com.project.requestobjects.TemporalBooking;

public interface TemporalBookingsRepository {
	
	public TemporalBooking getTemporalBookingByUserId(String userId);
	
	public void remove(String userId);
	
	public void removeSeat(String userId, Seat seat);

	public void put(String userId, TemporalBooking temporalBooking);
	
	public Map<String, TemporalBooking> getTemporalBookingList();
	
}
