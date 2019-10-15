package com.proyect.Cinema;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
	public @ResponseBody String addNewPlay (@RequestBody PlayPK playPk) {
		Movie movie = movieRepository.findById(playPk.getMovieId()).get();
		Sala sala = salaRepository.findById(playPk.getSalaId()).get();
		Long time = playPk.getStartTime().getTime() + movie.getDuration().getTime();
		Date endTime = new Date(time);
		Play play = new Play(playPk, endTime, 30, movie, sala);
//		if (isSalaAvailable(play)) {
//			playRepository.save(play);
//		}
		return "failed";
	}
	
//	private boolean isSalaAvailable(Play play) {
//		boolean isAvailable = true;
//		salaRepository
//		return true;
//	}
}
