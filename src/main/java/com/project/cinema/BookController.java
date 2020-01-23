package com.project.cinema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.project.entities.User;
import com.project.exceptions.NoSeatBookedException;
import com.project.exceptions.SeatAlreadyBookedException;
import com.project.repositories.BookRepository;
import com.project.repositories.PlayRepository;
import com.project.repositories.SeatRepository;
import com.project.repositories.UserRepository;
import com.project.requestobjects.BookRequestDTO;
import com.project.requestobjects.BookingDTO;

@RestController
@CrossOrigin
@RequestMapping(path="/books")
public class BookController {

	@Autowired private BookRepository bookRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private PlayRepository playRepository;
	@Autowired private SeatRepository seatRepository;
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<Booking> addNewBook (@RequestBody BookRequestDTO bookRequest) throws SeatAlreadyBookedException, NoSeatBookedException {
		Play play = playRepository.findById(bookRequest.getPlayPk()).get();
		User user = userRepository.findById(bookRequest.getUserId()).get();
		
		int sala = bookRequest.getPlayPk().getSalaId();
		List<Seat> seats = bookRequest.getSeats().stream()
			.map(num -> new SeatPK(sala, num))
			.map(seatRepository::findById)
			.map(Optional::get)
			.collect(Collectors.toList());
		if (seats.isEmpty()) {
			throw new NoSeatBookedException();
		}
		checkSeatAvailability(seats, play);
		play.setAvailableSeats(play.getAvailableSeats() - seats.size());
		Booking booking = new Booking(user, play, seats);
		playRepository.save(play);
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

	private void checkSeatAvailability(List<Seat> seats, Play play) throws SeatAlreadyBookedException {
		List<Seat> seatsAlreadyBooked = new ArrayList<>();
		List<Booking> bookings = play.getBooks();
		for (Seat seat : seats) {
			for (Booking book : bookings) {
				if(book.getSeats().contains(seat)) {
					seatsAlreadyBooked.add(seat);
				}
			}
		}
		if(!seatsAlreadyBooked.isEmpty()) {
			throw new SeatAlreadyBookedException(seatsAlreadyBooked);
		}
	}
}
