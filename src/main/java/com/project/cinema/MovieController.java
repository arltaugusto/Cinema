package com.project.cinema;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.entities.Movie;
import com.project.entities.Play;
import com.project.repositories.BookRepository;
import com.project.repositories.MovieRepository;
import com.project.repositories.PlayRepository;
import com.project.requestobjects.MovieDTO;
import com.project.utils.BasicEntitySaver;

@Controller
@CrossOrigin
@RequestMapping(path="/movies") 
public class MovieController {
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private PlayRepository playRepository;
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<Movie> addNewMovie (@RequestBody MovieDTO movie) {
		return BasicEntitySaver.save(new Movie(movie.getName(), movie.getDuration()), movieRepository);
	}
	
	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<Movie> modifyMovie (@RequestBody MovieDTO movie) {
		Optional<Movie> mov = movieRepository.findById(movie.getId());
		return BasicEntitySaver.save(mov.isPresent() ? mov.get() : null, movieRepository);
	}
	
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteMovie(@RequestBody MovieDTO movieRequest) {
		Optional<Movie> movieOptional = movieRepository.findById(movieRequest.getId());
		if(movieOptional.isPresent()) {
			Movie movie = movieOptional.get();
			List<Play> plays = movie.getPlays();
			plays.stream()
				.map(Play::getBooks)
				.forEach(bookRepository::deleteAll);
			playRepository.deleteAll(plays);
			movieRepository.delete(movie);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		}
		return new ResponseEntity<>("Movie not Found", HttpStatus.BAD_REQUEST);
	}
}
