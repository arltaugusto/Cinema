package com.project.utils;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.repositories.TemporalBookingsRepository;
import com.project.requestobjects.TemporalSeats;

@Component
public class ScheduledTasks {
	
	@Autowired private TemporalBookingsRepository temporalBookingsRepository;
	@Scheduled(fixedRate = 30000)
	public void cleanIncompleteBookings() {
		Map<String, TemporalSeats> cachedBookings = temporalBookingsRepository.getTemporalSeatsList();
		if(MapUtils.isNotEmpty(cachedBookings)) {
			
			for(Map.Entry<String, TemporalSeats> map : cachedBookings.entrySet()) {
				TemporalSeats temporalSeat = map.getValue();
				if(temporalSeat != null && temporalSeat.getInitTime().plusMinutes(1).isBefore(LocalDateTime.now()) ) {
					temporalSeat.setOpen(false);
					temporalBookingsRepository.updateStatus(map.getKey(), temporalSeat);
				}
			}
//			cachedBookings.entrySet().stream()
//			.filter(map -> map.getValue().getInitTime().plusMinutes(5).compareTo(LocalDateTime.now()) < 0)
//			.forEach(map -> {
//				TemporalSeats temporalSeats =  map.getValue();
//				temporalSeats.setOpen(false);
//				temporalSeats.clearSeats();
//				map.setValue(temporalSeats);
//			});
		}
	}
	
//	@Scheduled(fixedRate = 1000 * 60 * 10)
//	public void cleanExpiredLoginSessions() {
//		Map<String, TemporalSeats> cachedBookings = temporalBookingsRepository.getTemporalSeatsList();
//		if(MapUtils.isNotEmpty(cachedBookings))
//			cachedBookings.entrySet().stream()
//			.filter(map -> map.getValue().getInitTime().plusMinutes(30).compareTo(LocalDateTime.now()) < 0)
//			.map(Map.Entry::getKey)
//			.forEach(key -> temporalBookingsRepository.remove(key,));
//	}
}