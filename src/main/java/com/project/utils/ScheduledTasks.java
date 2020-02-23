package com.project.utils;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.entities.Play;
import com.project.exceptions.EntityNotFoundException;
import com.project.repositories.PlayRepository;
import com.project.repositories.TemporalBookingsRepository;
import com.project.requestobjects.TemporalSeats;

@Component
public class ScheduledTasks {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired private PlayRepository playRepository;
	@Autowired private TemporalBookingsRepository temporalBookingsRepository;
	
	@Scheduled(fixedRate = 30000)
	public void cleanIncompleteBookings() {
		Map<String, TemporalSeats> cachedBookings = temporalBookingsRepository.getTemporalSeatsList();
		if(MapUtils.isNotEmpty(cachedBookings)) {
			cachedBookings.entrySet().stream()
			.filter(map -> map.getValue().getInitTime().plusMinutes(5).isBefore(LocalDateTime.now()) && map.getValue().isOpen())
			.forEach(map -> {
				TemporalSeats temporalSeats =  map.getValue();
				try {
					Play play = BasicEntityUtils.entityFinder(playRepository.findById(temporalSeats.getPlayPk()));
					play.setAvailableSeats(play.getAvailableSeats() + temporalSeats.getSeats().size());
					playRepository.save(play);
				} catch (EntityNotFoundException e) {
					logger.error("Play not found", e);
				}
				temporalSeats.setOpen(false);
				temporalSeats.clearSeats();
			});
		}
	}
	
	@Scheduled(fixedRate = 1000 * 60 * 10)
	public void cleanExpiredLoginSessions() {
		Map<String, TemporalSeats> cachedBookings = temporalBookingsRepository.getTemporalSeatsList();
		if(MapUtils.isNotEmpty(cachedBookings))
			cachedBookings.entrySet().stream()
				.filter(map -> map.getValue().getInitTime().plusMinutes(30).compareTo(LocalDateTime.now()) < 0)
				.map(Map.Entry::getKey)
				.forEach(temporalBookingsRepository::remove);
	}
}