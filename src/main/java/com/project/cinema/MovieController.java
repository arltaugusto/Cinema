package com.project.cinema;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
	
	@Autowired private MovieRepository movieRepository;
	@Autowired private BookRepository bookRepository;
	@Autowired private PlayRepository playRepository;
	@Autowired StorageService storageService;

	@PostMapping(path="/add", consumes = {"multipart/form-data"})
	public @ResponseBody ResponseEntity<Movie> addNewMovie (@RequestPart("movie") @Valid String movieStr, @RequestPart("imageFile") @Nullable MultipartFile imageFile) throws IOException {
		MovieDTO movieDto = BasicEntityUtils.convertToEntityFromString(MovieDTO.class, movieStr);
		validateEmptyInformation(movieDto);
		Movie movie = new Movie(movieDto.getName(), movieDto.getDuration(), movieDto.getSynopsis(), true);
		if(imageFile != null) {
			String path =  String.format("%s/%s", BucketName.MOVIE_IMAGE.getBucketName(), movie.getMovieId());
			movie.setImagePath(imageFile.getOriginalFilename());
			storageService.saveImage(imageFile, path);
		}
		return BasicEntityUtils.save(movie, movieRepository);
	}

	private void validateEmptyInformation(MovieDTO movieDto) {
		if(StringUtils.isBlank(movieDto.getName()) || !NumberUtils.isParsable(String.valueOf(movieDto.getDuration()))) {
			throw new IllegalStateException("Incomplete Data");
		}
	}

	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<Movie> modifyMovie (@RequestBody MovieDTO movie) throws EntityNotFoundException {
		Movie mov = BasicEntityUtils.entityFinder(movieRepository.findById(movie.getId()));
		mov.setDuration(movie.getDuration());
		mov.setName(movie.getName());
		mov.setSynopsis(movie.getSynopsis());
		return BasicEntityUtils.save(mov, movieRepository);
	}
	
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteMovie(@RequestBody MovieDTO movieRequest) throws EntityNotFoundException {
		Movie movie = BasicEntityUtils.entityFinder(movieRepository.findById(movieRequest.getId()));
		movie.setActive(false);
		movieRepository.save(movie);
		List<Play> plays = movie.getPlays().stream()
			.filter(play -> play.getPlayPK().getStartTime().isAfter(LocalDateTime.now()))
			.collect(Collectors.toList());
		if(!plays.isEmpty()) {
			plays.forEach(play -> {
				bookRepository.deleteAll(play.getBooks());
				playRepository.delete(play);
			});
		}
		movieRepository.delete(movie);
		return new ResponseEntity<>("Deleted", HttpStatus.OK);
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Movie> getAllMovies() {
		return movieRepository.findAll().stream()
			.filter(Movie::isActive)
			.collect(Collectors.toList());
	}
	
	@PostMapping(path="/getMoviePlays", consumes = "application/json")
	public @ResponseBody List<Play> getMoviePlays(@RequestBody MovieDTO movie) throws EntityNotFoundException {
		return BasicEntityUtils.entityFinder(movieRepository.findById(movie.getId())).getPlays()
			.stream()
			.filter(play -> play.getPlayPK().getStartTime().plusMinutes(10).isAfter(LocalDateTime.now()) && play.isActive())
			.collect(Collectors.toList());
	}

	@GetMapping(path = "image/download/{id}")
	public ResponseEntity<byte[]> getMovieImage(@PathVariable("id") String id) throws EntityNotFoundException {
		Movie movie = BasicEntityUtils.entityFinder(movieRepository.findById(id));
		return new ResponseEntity<>(storageService.downloadImage(movie), HttpStatus.OK);
	}
}
