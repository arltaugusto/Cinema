package com.project.Cinema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.Entities.Movie;
import com.project.Entities.Play;
import com.project.Entities.PlayPK;
import com.project.Entities.Sala;
import com.project.Repositories.BookRepository;
import com.project.Repositories.MovieRepository;
import com.project.Repositories.PlayRepository;
import com.project.Repositories.SalaRepository;
import com.project.Repositories.UserRepository;
import com.project.exceptions.NoTimeAvailableException;

@RestController
@CrossOrigin
@RequestMapping(path="/plays")
public class PlayController {
	
	@Autowired private BookRepository bookRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private PlayRepository playRepository;
	@Autowired private SalaRepository salaRepository;
	@Autowired private MovieRepository movieRepository;

	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<String> addNewPlay (@RequestBody PlayPK playPk) throws NoTimeAvailableException {
		Movie movie = movieRepository.findById(playPk.getMovieId()).get();
		Sala sala = salaRepository.findById(playPk.getSalaId()).get();
		LocalDateTime endTime = playPk.getStartTime().plusMinutes(movie.getDuration());
		Play play = new Play(playPk, endTime, 60, movie, sala);
		isSalaAvailable(play);
		playRepository.save(play);
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
	
	@GetMapping(path="/all", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<List<Play>> getPlays (@RequestBody PlayPK playPk) throws NoTimeAvailableException {
		return new ResponseEntity<List<Play>>(playRepository.findAll().stream()
				.filter(play -> play.getPlayPK().getStartTime().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList()),
				HttpStatus.OK);
				
	}
	
	@PostMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deletePlay(@RequestBody PlayPK playPk) {
		Optional<Play> playOptional = playRepository.findById(playPk);
		if(playOptional.isPresent()) {
			Play play = playOptional.get();
			bookRepository.deleteAll(play.getBooks());
			playRepository.delete(play);
			return new ResponseEntity<String>("Deleted", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Play not Found", HttpStatus.BAD_REQUEST);
	}
	
	private void isSalaAvailable(Play play) throws NoTimeAvailableException {
		LocalDateTime newStartTime = play.getPlayPK().getStartTime();
		LocalDateTime newEndTime = play.getEndTime();
		List<Play> plays = playRepository.findAll();
		for(Play p : plays) {
			LocalDateTime currentStartTime = p.getPlayPK().getStartTime();
			LocalDateTime currentEndTime = p.getEndTime();
			if (!((newStartTime.compareTo(currentStartTime) < 0 && newEndTime.compareTo(currentStartTime) < 0) || (newStartTime.compareTo(currentEndTime) > 0 && newEndTime.compareTo(currentEndTime) > 0) && p.getPlayPK().getSalaId() == play.getPlayPK().getSalaId())) {
				throw new NoTimeAvailableException(play);
			}
		}
	}
}
