package com.proyect.Cinema;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
		public @ResponseBody String addNewBook (@RequestBody BookRequest bookRequest) {
			Book book = bookRequest.getBook();
			Play p = playRepository.findById(bookRequest.getPlayPk()).get();
			User u = userRepository.findById(bookRequest.getUserId()).get();
			List<Seat> seats = bookRequest.getSeats().stream().map(num -> {
				SeatPK s = new SeatPK(bookRequest.getPlayPk().getSalaId(), num);
				return seatRepository.findById(s).get();
			}).collect(Collectors.toList());
			p.setAvailableSeats(p.getAvailableSeats() - seats.size());
			book.setUser(u);
			book.setPlay(p);
			book.setSeats(seats);
			playRepository.save(p);
			bookRepository.save(book);
			return "Sucess";
		}
		
		@GetMapping(path="/{id}")
		public @ResponseBody Iterable<Book> getUserBooks(@PathVariable("id") int id) {
			return userRepository.findById(id).get().getBooks();
		}
		
}
