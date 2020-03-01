package com.project.cinema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.project.entities.Seat;
import com.project.entities.SeatPK;
import com.project.entities.SeatPrice;
import com.project.entities.User;
import com.project.exceptions.EntityNotFoundException;
import com.project.exceptions.NoSeatBookedException;
import com.project.exceptions.SeatAlreadyBookedException;
import com.project.exceptions.SessionTimeOutException;
import com.project.repositories.BookRepository;
import com.project.repositories.PlayRepository;
import com.project.repositories.SeatPriceRepository;
import com.project.repositories.SeatRepository;
import com.project.repositories.TemporalBookingsRepository;
import com.project.repositories.UserRepository;
import com.project.requestobjects.BookRequestDTO;
import com.project.requestobjects.BookingDTO;
import com.project.requestobjects.SeatRequest;
import com.project.requestobjects.TemporalBooking;
import com.project.utils.BasicEntityUtils;
import com.project.utils.SeatManager;

@RestController
@CrossOrigin
@RequestMapping(path="/books")
public class BookController {

	@Autowired private BookRepository bookRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private PlayRepository playRepository;
	@Autowired private SeatRepository seatRepository;
	@Autowired private TemporalBookingsRepository temporalBookingsRepository;
	@Autowired private SeatPriceRepository seatPriceRepository;

	@PostMapping(path = "/bookTemporalSeat")
	public @ResponseBody ResponseEntity<Seat> bookTemporalSeat(@RequestBody SeatRequest seatRequest) throws SeatAlreadyBookedException, EntityNotFoundException, SessionTimeOutException {
		String userId = seatRequest.getUserId();
		SeatPK seatPk = new SeatPK(seatRequest.getRoomId(), seatRequest.getSeatId());
		Seat seat = BasicEntityUtils.entityFinder(seatRepository.findById(seatPk));
		Play play = BasicEntityUtils.entityFinder(playRepository.findById(seatRequest.getPlayPk()));
		checkSeatAvailability(seat, play);
		if (temporalBookingsRepository.getTemporalBookingList().containsKey(userId) && temporalBookingsRepository.getTemporalBookingByUserId(userId).getPlayPk().equals(seatRequest.getPlayPk())) {
			TemporalBooking temporalBooking = temporalBookingsRepository.getTemporalBookingByUserId(userId);
			checkSessionStatus(userId, temporalBooking);
			temporalBooking.addSeat(seat);
		} else {
			List<Seat> userSeatList = new ArrayList<>();
			userSeatList.add(seat);
			temporalBookingsRepository.put(userId, new TemporalBooking(userSeatList, seatRequest.getPlayPk(), LocalDateTime.now()));
		}
		play.setAvailableSeats(play.getAvailableSeats() - 1);
		playRepository.save(play);
		return new ResponseEntity<>(seat, HttpStatus.OK); 
	}

	@PostMapping(path = "/removeTemporalSeat")
	public @ResponseBody ResponseEntity<Seat> removeTemporalSeat(@RequestBody SeatRequest seatRequest) throws EntityNotFoundException, SessionTimeOutException {
		String userId = seatRequest.getUserId();
		TemporalBooking temporalBooking = temporalBookingsRepository.getTemporalBookingByUserId(userId);
		checkSessionStatus(userId, temporalBooking);
		SeatPK seatPk = new SeatPK(seatRequest.getRoomId(), seatRequest.getSeatId());
		Seat seat = BasicEntityUtils.entityFinder(seatRepository.findById(seatPk));
		Play play = BasicEntityUtils.entityFinder(playRepository.findById(seatRequest.getPlayPk()));
		List<Seat> seats = temporalBooking.getSeats();
		if(seats.contains(seat)) {
			play.setAvailableSeats(play.getAvailableSeats() + 1);
			temporalBookingsRepository.removeSeat(userId, seat);
			playRepository.save(play);
			return new ResponseEntity<>(seat, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<Booking> addNewBook (@RequestBody BookRequestDTO bookRequest) throws EntityNotFoundException, NoSeatBookedException {
		String userId = bookRequest.getUserId();
		User user = BasicEntityUtils.entityFinder(userRepository.findById(userId));
		Play play = BasicEntityUtils.entityFinder(playRepository.findById(bookRequest.getPlayPk()));
		TemporalBooking temporalBooking = temporalBookingsRepository.getTemporalBookingByUserId(userId);
		List<Seat> seats = temporalBooking.getSeats();
		if(seats.isEmpty()) {
			throw new NoSeatBookedException();
		}
		SeatPrice lastSeatPrice = BasicEntityUtils.entityFinder(seatPriceRepository.findAll().stream()
			.filter(seatPrice -> seatPrice.getActivationDate().compareTo(LocalDateTime.now()) < 0)
			.max(Comparator.comparing(SeatPrice::getActivationDate).thenComparing(SeatPrice::getSetDate)));
		double total = seats.stream()
			.mapToDouble(seat -> seat.isSuperSeat() ? lastSeatPrice.getSuperSeatPrice() : lastSeatPrice.getRegularSeatPrice())
			.sum();
		Booking booking = new Booking(user, play, seats, total);
		temporalBookingsRepository.remove(userId);
		bookRepository.save(booking);
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

	private void checkSeatAvailability(Seat seat, Play play) throws SeatAlreadyBookedException {
		List<TemporalBooking> temporalBookings = temporalBookingsRepository.getTemporalBookingList().entrySet().stream()
			.filter(map -> map.getValue() != null && map.getValue().getPlayPk().equals(play.getPlayPK()))
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
	
	private void checkSessionStatus(String userId, TemporalBooking temporalSeat) throws SessionTimeOutException {
		if(!temporalSeat.isOpen()) {
			temporalBookingsRepository.remove(userId);
			throw new SessionTimeOutException("Booking session expired");
		}
	}
}
