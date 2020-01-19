package com.project.Cinema;

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

import com.project.Entities.Movie;
import com.project.Entities.Play;
import com.project.Repositories.BookRepository;
import com.project.Repositories.MovieRepository;
import com.project.Repositories.PlayRepository;
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
	public @ResponseBody ResponseEntity<Movie> addNewMovie (@RequestBody Movie movie) {
		return BasicEntitySaver.save(movie, movieRepository);
	}
	
	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<Movie> modifyMovie (@RequestBody Movie movie) {
		return BasicEntitySaver.save(movie, movieRepository);
	}
	
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteMovie(@RequestBody Movie movieRequest) {
		Optional<Movie> movieOptional = movieRepository.findById(movieRequest.getid());
		if(movieOptional.isPresent()) {
			Movie movie = movieOptional.get();
			List<Play> plays = movie.getPlays();
			plays.stream()
				.map(Play::getBooks)
				.forEach(bookRepository::deleteAll);
			playRepository.deleteAll(plays);
			movieRepository.delete(movie);
			return new ResponseEntity<String>("Deleted", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Movie not Found", HttpStatus.BAD_REQUEST);
	}
}
