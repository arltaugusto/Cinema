package com.project.cinema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.entities.Booking;
import com.project.entities.Play;
import com.project.entities.PlayPK;
import com.project.entities.Seat;
import com.project.entities.SeatPK;
import com.project.entities.User;
import com.project.exceptions.EntityNotFoundException;
import com.project.exceptions.SeatAlreadyBookedException;
import com.project.exceptions.SessionTimeOutException;
import com.project.repositories.BookRepository;
import com.project.repositories.PlayRepository;
import com.project.repositories.SeatRepository;
import com.project.repositories.TemporalBookingsRepository;
import com.project.repositories.UserRepository;
import com.project.requestobjects.BookRequestDTO;
import com.project.requestobjects.BookingDTO;
import com.project.requestobjects.SeatRequest;
import com.project.requestobjects.TemporalSeats;
import com.project.utils.BasicEntityUtils;
import com.project.utils.SeatManager;

@RestController
@CrossOrigin
@RequestMapping(path="/books")
public class BookController {

	private static final Logger log = LoggerFactory.getLogger(BookController.class);
	@Autowired private BookRepository bookRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private PlayRepository playRepository;
	@Autowired private SeatRepository seatRepository;
	@Autowired private TemporalBookingsRepository temporalBookingsRepository;

	@PostMapping(path = "/bookTemporalSeat")
	public @ResponseBody ResponseEntity<String> bookTemporalSeat(@RequestBody SeatRequest seatRequest) throws SeatAlreadyBookedException, EntityNotFoundException, SessionTimeOutException {
		String userId = seatRequest.getUserId();
		SeatPK seatPk = new SeatPK(seatRequest.getRoomId(), seatRequest.getSeatId());
		Seat seat = BasicEntityUtils.entityFinder(seatRepository.findById(seatPk));
		checkSeatAvailability(seat, seatRequest.getPlayPk());
		if (temporalBookingsRepository.getTemporalSeatsList().containsKey(userId) && temporalBookingsRepository.getTemporalSeatsByUserId(userId).getPlayPk().equals(seatRequest.getPlayPk())) {
			TemporalSeats temporalSeat = temporalBookingsRepository.getTemporalSeatsByUserId(userId);
			if(!temporalSeat.isOpen()) {
				temporalBookingsRepository.updateStatus(userId, null);
				throw new SessionTimeOutException("Booking session expired");
			}
			temporalSeat.addSeat(seat);
			temporalBookingsRepository.updateStatus(userId, temporalSeat);
		} else {
			List<Seat> userSeatList = new ArrayList<>();
			userSeatList.add(seat);
				temporalBookingsRepository.updateStatus(userId, new TemporalSeats(userSeatList, userId, seatRequest.getPlayPk(), LocalDateTime.now()));
		}
		return new ResponseEntity<>("{\"message\": \"booked\"}", HttpStatus.OK);
	}
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<Booking> addNewBook (@RequestBody BookRequestDTO bookRequest) throws EntityNotFoundException {
		String userId = bookRequest.getUserId();
		User user = BasicEntityUtils.entityFinder(userRepository.findById(userId));
		Play play = BasicEntityUtils.entityFinder(playRepository.findById(bookRequest.getPlayPk()));
		TemporalSeats temporalSeatsByUserId = temporalBookingsRepository.getTemporalSeatsByUserId(userId);
		List<Seat> seats = temporalSeatsByUserId.getSeats();
		Booking booking = new Booking(user, play, seats);
		play.setAvailableSeats(play.getAvailableSeats() - seats.size());
		temporalBookingsRepository.remove(userId, temporalSeatsByUserId);
		bookRepository.save(booking);
		playRepository.save(play);
		return new ResponseEntity<>(booking, HttpStatus.OK);
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody ResponseEntity<Iterable<Booking>> getUserBooks(@PathVariable("id") String id) throws EntityNotFoundException {
		return new ResponseEntity<>(BasicEntityUtils.entityFinder(userRepository.findById(id)).getBooks(), HttpStatus.OK);
	}
	
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteBooking(@RequestBody BookingDTO book) throws EntityNotFoundException {
			Booking booking = BasicEntityUtils.entityFinder(bookRepository.findById(book.getBookId()));
			Play play = booking.getPlay();
			play.setAvailableSeats(play.getAvailableSeats() + booking.getSeats().size());
			playRepository.save(play);
			bookRepository.delete(booking);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
	}

	private void checkSeatAvailability(Seat seat, PlayPK playPk) throws SeatAlreadyBookedException, EntityNotFoundException {
		Play play= BasicEntityUtils.entityFinder(playRepository.findById(playPk));
		List<TemporalSeats> temporalBookings = temporalBookingsRepository.getTemporalSeatsList().entrySet().stream()
			.filter(map -> map.getValue() != null && map.getValue().getPlayPk().equals(playPk))
			.map(Map.Entry::getValue)
			.collect(Collectors.toList());
		check(play.getBooks(), seat);
		check(temporalBookings, seat);
	}
	
	private <T extends SeatManager> void check(List<T> list, Seat seat) throws SeatAlreadyBookedException {
		for (T book : list) {
			List<Seat> seatsAlreadyBooked = book.getSeats();
			if(seatsAlreadyBooked.contains(seat)) {
				throw new SeatAlreadyBookedException(Arrays.asList(seat));
			}
		}
	}
}
