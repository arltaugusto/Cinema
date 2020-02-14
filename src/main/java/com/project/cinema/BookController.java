package com.project.cinema;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.project.entities.Room;
import com.project.entities.Seat;
import com.project.entities.SeatPK;
import com.project.entities.User;
import com.project.exceptions.InvalidCredentialsException;
import com.project.exceptions.SeatAlreadyBookedException;
import com.project.repositories.BookRepository;
import com.project.repositories.PlayRepository;
import com.project.repositories.SalaRepository;
import com.project.repositories.SeatRepository;
import com.project.repositories.UserRepository;
import com.project.requestobjects.BookRequestDTO;
import com.project.requestobjects.BookingDTO;
import com.project.requestobjects.SeatRequest;
import com.project.requestobjects.TemporalSeat;
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
	@Autowired private SalaRepository roomRepository;

	
	public static Map<String, TemporalSeat> temporaryBookedSeats = new HashMap<>();
	
	//FIXME create  a new exception
	@PostMapping(path = "/bookTemporalSeat")
	public @ResponseBody ResponseEntity<String> bookTemporalSeat(@RequestBody SeatRequest seatRequest) throws SeatAlreadyBookedException, InvalidCredentialsException {
		String userId = seatRequest.getUserId();
		SeatPK seatPk = new SeatPK(seatRequest.getRoomId(), seatRequest.getSeatId());
		Optional<Room> roomOp = roomRepository.findById(seatRequest.getRoomId());
		Optional<Seat> seatOp = seatRepository.findById(seatPk);
		if(!roomOp.isPresent() || !seatOp.isPresent()) {
			throw new InvalidCredentialsException("bad room");
		}
		Seat seat = seatOp.get();
		checkSeatAvailability(seat, seatRequest.getPlayPk());
		if (temporaryBookedSeats.containsKey(userId)) {
			TemporalSeat temporalSeat = temporaryBookedSeats.get(userId);
			if(!temporalSeat.isOpen()) {
				temporaryBookedSeats.remove(userId);
				//TODO throw exception
			}
			temporalSeat.addSeat(seat);
		} else {
			temporaryBookedSeats.put(userId, new TemporalSeat(Arrays.asList(seat), userId, seatRequest.getPlayPk(), LocalDateTime.now()));
		}
		return new ResponseEntity<>("Succed", HttpStatus.OK);
	}
	
	//FIXME create  a new exception
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<Booking> addNewBook (@RequestBody BookRequestDTO bookRequest) throws InvalidCredentialsException {
		String userId = bookRequest.getUserId();
		Optional<User> userOp = userRepository.findById(userId);
		Optional<Play> playOp = playRepository.findById(bookRequest.getPlayPk());
		if (!playOp.isPresent() || !userOp.isPresent())
			throw new InvalidCredentialsException("bad request");
		Play play = playOp.get();
		List<Seat> seats = temporaryBookedSeats.get(userId).getSeats();
		Booking booking = new Booking(userOp.get(), play, seats);
		play.setAvailableSeats(play.getAvailableSeats() - seats.size());
		temporaryBookedSeats.remove(userId);
		bookRepository.save(booking);
		return new ResponseEntity<>(booking, HttpStatus.OK);
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody ResponseEntity<Iterable<Booking>> getUserBooks(@PathVariable("id") String id) {
		return new ResponseEntity<>(userRepository.findById(id).get().getBooks(), HttpStatus.OK);
	}
	
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteBooking(@RequestBody BookingDTO book) {
		Optional<Booking> bookingOptional = bookRepository.findById(book.getBookId());
		if(bookingOptional.isPresent()) {
			Booking booking = bookingOptional.get();
			Play play = booking.getPlay();
			play.setAvailableSeats(play.getAvailableSeats() + booking.getSeats().size());
			playRepository.save(play);
			bookRepository.delete(booking);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		}
		return new ResponseEntity<>("No booking found", HttpStatus.BAD_REQUEST);
	}

	//FIXME create  a new exception
	private void checkSeatAvailability(Seat seat, PlayPK playPk) throws SeatAlreadyBookedException, InvalidCredentialsException {
		Optional<Play> playOp = playRepository.findById(playPk);
		if (!playOp.isPresent()) {
			throw new InvalidCredentialsException("asdasd");
		}
		Play play= playOp.get();
		List<TemporalSeat> temporalBookings = temporaryBookedSeats.entrySet().stream()
			.filter(map -> map.getValue().getPlayPk().equals(playPk))
			.map(Map.Entry::getValue)
			.collect(Collectors.toList());
		check(play.getBooks(), seat);
		check(temporalBookings, seat);
	}
	
	private <T extends SeatManager> void check(List<T> list, Seat seat) throws SeatAlreadyBookedException {
		for (T book : list) {
			List<SeatPK> seatsAlreadyBooked = book.getSeats().stream()
					.map(Seat::getSeatPk)
					.collect(Collectors.toList());
			if(seatsAlreadyBooked.contains(seat.getSeatPk())) {
				throw new SeatAlreadyBookedException(Arrays.asList(seat));
			}
		}
	}
}
