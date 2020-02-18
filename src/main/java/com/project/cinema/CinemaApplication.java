package com.project.cinema;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.project.entities.Movie;
import com.project.entities.Play;
import com.project.entities.PlayPK;
import com.project.entities.Room;
import com.project.entities.Seat;
import com.project.entities.SeatPK;
import com.project.entities.User;
import com.project.repositories.MovieRepository;
import com.project.repositories.PlayRepository;
import com.project.repositories.SalaRepository;
import com.project.repositories.SeatRepository;
import com.project.repositories.UserRepository;
import com.project.utils.BasicEntityUtils;

@SpringBootApplication
@ComponentScan({"com.project.cinema","com.project.requestobjects","com.project.exceptions", "com.project.utils", "com.project.security"})
@EntityScan("com.project.entities")
@EnableJpaRepositories("com.project.repositories")
@EnableScheduling
@EnableCaching
public class CinemaApplication {
	private boolean init = false;
	public static void main(String[] args) { 
		SpringApplication.run(CinemaApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner initializer(UserRepository userRepository, SalaRepository salaRepository, SeatRepository seatRepository, MovieRepository movieRepository, PlayRepository playRepository) {
		return args -> {
			if(init) {
			for (int i = 1; i <= 10; i++) {
				Room room = new Room();
				salaRepository.save(room);
				seatRepository.saveAll(createRoomSeats(room));
			}
			List<User> users = Arrays.asList(
				new User("admin@gmail.com", "Admin", "ROLE_ADMIN", "holahola"),
				new User("user@gmail.com", "User", "ROLE_USER", "holahola")
			);
			List<Movie> movies = Arrays.asList(
				new Movie("Star Wars", 180, "None", "synopsis of star wars"),
				new Movie("Harry Potter", 200, "None", "synopsis of harry potter")
			);
			PlayPK playPk = new PlayPK(1, 1, LocalDateTime.of(2021, Month.AUGUST, 29, 19, 30));
			movieRepository.saveAll(movies);
			playRepository.save(new Play(playPk, playPk.getStartTime().plusMinutes(180), 60, movies.get(0), BasicEntityUtils.entityFinder(salaRepository.findById(1))));
			userRepository.saveAll(users);
		}};
		
	}

	private List<Seat> createRoomSeats(Room room) {
		List<Seat> seats = new ArrayList<>();
		for(int i = 1; i <=60; i++) {
			seats.add(new Seat(new SeatPK(room.getId(), i), room));
		}
		return seats;
	}
}
