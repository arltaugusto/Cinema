package com.project.cinema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.entities.Booking;
import com.project.entities.Movie;
import com.project.entities.Play;
import com.project.entities.PlayPK;
import com.project.entities.Room;
import com.project.entities.Seat;
import com.project.exceptions.EntityNotFoundException;
import com.project.exceptions.InvalidCredentialsException;
import com.project.exceptions.NoTimeAvailableException;
import com.project.repositories.BookRepository;
import com.project.repositories.MovieRepository;
import com.project.repositories.PlayRepository;
import com.project.repositories.SalaRepository;
import com.project.repositories.TemporalBookingsRepository;
import com.project.repositories.UserRepository;
import com.project.requestobjects.TemporalSeats;
import com.project.utils.BasicEntityUtils;

@RestController
@CrossOrigin
@RequestMapping(path="/plays")
public class PlayController {
	
	@Autowired private BookRepository bookRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private PlayRepository playRepository;
	@Autowired private SalaRepository salaRepository;
	@Autowired private MovieRepository movieRepository;
	private TemporalBookingsRepository temporalBookingsRepository;
	private static final long MINUTES_BEFORE_MOVIE = 15; 
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<Play> addNewPlay (@RequestBody PlayPK playPk) throws NoTimeAvailableException, EntityNotFoundException {
		Movie movie = BasicEntityUtils.entityFinder(movieRepository.findById(playPk.getMovieId()));
		Room sala = BasicEntityUtils.entityFinder(salaRepository.findById(playPk.getRoomId()));
		LocalDateTime endTime = playPk.getStartTime().plusMinutes(movie.getDuration());
		Play play = new Play(playPk, endTime, 60, movie, sala);
		isSalaAvailable(play);
		return BasicEntityUtils.save(play, playRepository);
	}
	
	@GetMapping(path="/all", produces = "application/json")
	public @ResponseBody ResponseEntity<List<Play>> getPlays () {
		return new ResponseEntity<>(playRepository.findAll().stream()
				.filter(play -> play.getPlayPK().getStartTime().isAfter(LocalDateTime.now().plusMinutes(MINUTES_BEFORE_MOVIE)))
				.collect(Collectors.toList()),
				HttpStatus.OK);
	}
	
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deletePlay(@RequestBody PlayPK playPk) throws EntityNotFoundException {
		Play play = BasicEntityUtils.entityFinder(playRepository.findById(playPk));
		if(play.getPlayPK().getStartTime().compareTo(LocalDateTime.now()) > 0) {
			bookRepository.deleteAll(play.getBooks());
		}
		playRepository.delete(play);
		return new ResponseEntity<>("Deleted", HttpStatus.OK);
	}
	
	//TODO test
	private void isSalaAvailable(Play play) throws NoTimeAvailableException {
		LocalDateTime newStartTime = play.getPlayPK().getStartTime();
		LocalDateTime newEndTime = play.getEndTime();
		List<Play> plays = playRepository.findAll();
		for(Play p : plays) {
			LocalDateTime currentStartTime = p.getPlayPK().getStartTime();
			LocalDateTime currentEndTime = p.getEndTime();
			if (!((newStartTime.compareTo(currentStartTime) < 0 && newEndTime.compareTo(currentStartTime) < 0) ||
					(newStartTime.compareTo(currentEndTime) > 0 && newEndTime.compareTo(currentEndTime) > 0)) 
					&& p.getPlayPK().getRoomId() == play.getPlayPK().getRoomId()) {
				throw new NoTimeAvailableException(play);
			}
		}
	}

	@PostMapping(path = "/getPlay")
	public @ResponseBody ResponseEntity<Play> getPlay(@RequestBody PlayPK id) throws EntityNotFoundException {
		return new ResponseEntity<>(BasicEntityUtils.entityFinder(playRepository.findById(id)), HttpStatus.OK);
	}
	
	//TODO test
	@PostMapping(path = "/getPlayBookedSeats")
	public @ResponseBody List<Seat> getPlayBookedSeats(@RequestBody PlayPK id) throws EntityNotFoundException {
		Play play = BasicEntityUtils.entityFinder(playRepository.findById(id));
		List<Seat> totalBookedSeats = new ArrayList<>();
		List<Seat> alradyBookedSeats = play.getBooks().stream()
			.map(Booking::getSeats)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		List<Seat> temporaryBookedSeats = temporalBookingsRepository.getTemporalSeatsList().entrySet()
			.stream()
			.map(Map.Entry::getValue)
			.map(TemporalSeats::getSeats)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		totalBookedSeats.addAll(alradyBookedSeats);
		totalBookedSeats.addAll(temporaryBookedSeats);
		return totalBookedSeats;
	}
}
