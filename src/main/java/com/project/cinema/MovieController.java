package com.project.cinema;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.entities.Movie;
import com.project.entities.Play;
import com.project.entities.User;
import com.project.repositories.BookRepository;
import com.project.repositories.MovieRepository;
import com.project.repositories.PlayRepository;
import com.project.requestobjects.MovieDTO;
import com.project.utils.BasicEntityUtils;
import com.project.utils.StorageUtils;

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
	@Autowired
	private Environment environment;
	
	@PostMapping(path="/add", consumes = {"multipart/form-data"}) // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<Movie> addNewMovie (@RequestPart("movie") @Valid String movieStr, @RequestPart("imageFile")@Valid @NotNull @NotBlank MultipartFile imageFile) throws IOException {
		MovieDTO movie = BasicEntityUtils.convertToEntityFromString(MovieDTO.class, movieStr);
		String path = environment.getProperty("images.directory") + imageFile.getOriginalFilename();
		if(!imageFile.isEmpty())
			StorageUtils.saveImage(imageFile, path);
		return BasicEntityUtils.save(new Movie(movie.getName(), movie.getDuration(), path, movie.getSynopsis()), movieRepository);
	}
	
	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<Movie> modifyMovie (@RequestBody MovieDTO movie) {
		Optional<Movie> mov = movieRepository.findById(movie.getId());
		return BasicEntityUtils.save(mov.isPresent() ? mov.get() : null, movieRepository);
	}
	
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteMovie(@RequestBody MovieDTO movieRequest) throws IOException {
		Optional<Movie> movieOptional = movieRepository.findById(movieRequest.getId());
		if(movieOptional.isPresent()) {
			Movie movie = movieOptional.get();
			List<Play> plays = movie.getPlays();
			StorageUtils.deleteImage(Paths.get(movie.getImagePath()));
			plays.stream()
				.map(Play::getBooks)
				.forEach(bookRepository::deleteAll);
			playRepository.deleteAll(plays);
			movieRepository.delete(movie);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		}
		return new ResponseEntity<>("Movie not Found", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Movie> getAllUsers() {
		return movieRepository.findAll();
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody List<Play> getMoviePlays(@PathVariable("id") int id) {
		return movieRepository.findById(id).get().getPlays();
	}
}
