package com.proyect.Cinema;

import java.util.Date;
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

import com.proyect.Entities.Movie;
import com.proyect.Entities.Play;
import com.proyect.Entities.PlayPK;
import com.proyect.Entities.Sala;
import com.proyect.Repositories.BookRepository;
import com.proyect.Repositories.MovieRepository;
import com.proyect.Repositories.PlayRepository;
import com.proyect.Repositories.SalaRepository;
import com.proyect.Repositories.UserRepository;
import com.proyect.exceptions.NoTimeAvailableException;

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
		Long time = playPk.getStartTime().getTime() + movie.getDuration();
		Date endTime = new Date(time);
		Play play = new Play(playPk, endTime, 60, movie, sala);
		isSalaAvailable(play);
		playRepository.save(play);
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
	
	private void isSalaAvailable(Play play) throws NoTimeAvailableException {
		Date newStartTime = play.getPlayPK().getStartTime();
		Date newEndTime = play.getEndTime();
		List<Play> plays = playRepository.findAll();
		for(Play p : plays) {
			Date currentStartTime = p.getPlayPK().getStartTime();
			Date currentEndTime = p.getEndTime();
			if (!((newStartTime.compareTo(currentStartTime) < 0 && newEndTime.compareTo(currentStartTime) < 0) || (newStartTime.compareTo(currentEndTime) > 0 && newEndTime.compareTo(currentEndTime) > 0) && p.getPlayPK().getSalaId() == play.getPlayPK().getSalaId())) {
				throw new NoTimeAvailableException(play);
			}
		}
		
	}
}
