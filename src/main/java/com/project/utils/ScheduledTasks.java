package com.project.utils;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.cinema.BookController;

@Component
public class ScheduledTasks {


//	@Scheduled(fixedRate = 5000)
//	public void cleanIncompleteBookings() {
//		BookController.temporaryBookedSeats.entrySet().stream()
//			.filter(map -> map.getValue().getInitTime().plusMinutes(10).compareTo(LocalDateTime.now()) < 0)
//			.map(Map.Entry::getValue)
//			.forEach(temporalBooking -> temporalBooking.setOpen(false));
//	}
}