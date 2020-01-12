package com.project.Cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.Entities.Movie;
import com.project.Repositories.MovieRepository;
import com.project.utils.BasicEntitySaver;

@Controller
@CrossOrigin
@RequestMapping(path="/movie") 
public class MovieController {
	@Autowired
	private MovieRepository movieRepository;
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<String> addNewMovie (@RequestBody Movie movie) {
		return BasicEntitySaver.save(movie, movieRepository);
	}
	
	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<String> modifyMovie (@RequestBody Movie movie) {
		return BasicEntitySaver.save(movie, movieRepository);
	}
}
