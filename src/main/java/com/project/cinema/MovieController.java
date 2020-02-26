package com.project.cinema;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.entities.Movie;
import com.project.entities.Play;
import com.project.exceptions.EntityNotFoundException;
import com.project.repositories.BookRepository;
import com.project.repositories.MovieRepository;
import com.project.repositories.PlayRepository;
import com.project.requestobjects.MovieDTO;
import com.project.utils.BasicEntityUtils;
import com.project.utils.BucketName;
import com.project.utils.StorageService;

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
	@Autowired StorageService storageService;


	@PostMapping(path="/add", consumes = {"multipart/form-data"}) // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<Movie> addNewMovie (@RequestPart("movie") @Valid String movieStr, @RequestPart("imageFile")@Valid @NotNull @NotBlank MultipartFile imageFile) throws IOException {
		MovieDTO movieDto = BasicEntityUtils.convertToEntityFromString(MovieDTO.class, movieStr);
		Movie movie = new Movie(movieDto.getName(), movieDto.getDuration(), movieDto.getSynopsis());
		String path = String.format("%s/%s", BucketName.MOVIE_IMAGE.getBucketName(), movie.getMovieId());
		movie.setImagePath(imageFile.getOriginalFilename());
//		if(!imageFile.isEmpty())
//			storageService.saveImage(imageFile, path);
		return BasicEntityUtils.save(movie, movieRepository);
	}
	
	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<Movie> modifyMovie (@RequestBody MovieDTO movie) throws EntityNotFoundException {
		Movie mov = BasicEntityUtils.entityFinder(movieRepository.findById(movie.getId()));
		mov.setDuration(movie.getDuration());
		mov.setName(movie.getName());
		mov.setSynopsis(movie.getSynopsis());
		return BasicEntityUtils.save(mov, movieRepository);
	}
	
	//TODO test
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteMovie(@RequestBody MovieDTO movieRequest) throws IOException {
		Optional<Movie> movieOptional = movieRepository.findById(movieRequest.getId());
		if(movieOptional.isPresent()) {
			Movie movie = movieOptional.get();
			List<Play> plays = movie.getPlays();
//			StorageService.deleteImage(Paths.get(movie.getImagePath()));
			plays.stream()
				.map(Play::getBooks)
				.map(bookingList -> bookingList.stream()
					.filter(book -> book.getPlay().getPlayPK().getStartTime().compareTo(LocalDateTime.now()) > 0)
					.collect(Collectors.toList()))
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
	
	@PostMapping(path="/getMoviePlays", consumes = "application/json")
	public @ResponseBody List<Play> getMoviePlays(@RequestBody MovieDTO movie) throws EntityNotFoundException {
		return BasicEntityUtils.entityFinder(movieRepository.findById(movie.getId())).getPlays();
	}
//
//	@GetMapping(path = "image/download/{id}")
//	public byte[] getMovieImage(@PathVariable("id") String id) throws NumberFormatException, EntityNotFoundException {
//		Movie movie = BasicEntityUtils.entityFinder(movieRepository.findById(Integer.parseInt(id)));
//		return storageService.downloadImage(movie);
//	}
}
