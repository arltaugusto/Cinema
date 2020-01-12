package com.proyect.Cinema;

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

import com.proyect.Entities.Book;
import com.proyect.Entities.Play;
import com.proyect.Entities.Seat;
import com.proyect.Entities.SeatPK;
import com.proyect.Entities.User;
import com.proyect.Repositories.BookRepository;
import com.proyect.Repositories.PlayRepository;
import com.proyect.Repositories.SeatRepository;
import com.proyect.Repositories.UserRepository;
import com.proyect.exceptions.ApplicationErrors;
import com.proyect.exceptions.SeatAlreadyBookedException;
import com.proyect.requestObjects.BookRequest;

@RestController
@CrossOrigin
@RequestMapping(path="/books")
public class BookController {

	@Autowired private BookRepository bookRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private PlayRepository playRepository;
	@Autowired private SeatRepository seatRepository;
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<String> addNewBook (@RequestBody BookRequest bookRequest) throws SeatAlreadyBookedException {
		Book book = bookRequest.getBook();
		Play p = playRepository.findById(bookRequest.getPlayPk()).get();
		User u = userRepository.findById(bookRequest.getUserId()).get();
		int sala = bookRequest.getPlayPk().getSalaId();
		List<Seat> seats = bookRequest.getSeats().stream()
			.map(num -> new SeatPK(sala, num))
			.map(seatRepository::findById)
			.map(Optional::get)
			.collect(Collectors.toList());
		checkSeatAvailability(seats, p);
		p.setAvailableSeats(p.getAvailableSeats() - seats.size());
		book.setUser(u);
		book.setPlay(p);
		book.setSeats(seats);
		playRepository.save(p);
		bookRepository.save(book);
		return new ResponseEntity<String>("Sucess", HttpStatus.OK);
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody ResponseEntity<Iterable<Book>> getUserBooks(@PathVariable("id") int id) {
		return new ResponseEntity<Iterable<Book>>(userRepository.findById(id).get().getBooks(), HttpStatus.OK);
	}
	
	private void checkSeatAvailability(List<Seat> seats, Play play) throws SeatAlreadyBookedException {
		List<Book> bookings = play.getBooks();
		for (Seat seat : seats) {
			for (Book book : bookings) {
				if(book.getSeats().contains(seat)) {
					throw new SeatAlreadyBookedException(seat);
				}
			}
		}
	}
}
