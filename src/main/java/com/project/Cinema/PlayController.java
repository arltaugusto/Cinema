package com.project.Cinema;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
