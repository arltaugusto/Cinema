//package com.project.cinema;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.project.repositories.TemporalBookingsRepository;
//import com.project.requestobjects.TemporalSeats;
//
//@Component
//public class Initialize implements CommandLineRunner{
//	
//  private static final Logger logger = LoggerFactory.getLogger(Initialize.class);
//
////	private CachedBookings cachedBookings;
//	private TemporalBookingsRepository temporalBookingsRepository;
//
//	public Initialize(TemporalBookingsRepository temporalBookingsRepository) {
//		this.temporalBookingsRepository = temporalBookingsRepository;
//	}
//	
//	@Override
//	public void run(String... args) throws Exception {
//		logger.info("adding map");
//		temporalBookingsRepository.put("user", new TemporalSeats());
//	}
//
//}
