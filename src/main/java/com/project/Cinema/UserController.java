package com.project.Cinema;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.Entities.User;
import com.project.Repositories.UserRepository;
import com.project.exceptions.InvalidCredentialsException;


@Controller
@CrossOrigin
@RequestMapping(path="/user") 
public class UserController {
	@Autowired 
	private UserRepository userRepository;
	
	private static final String invalidCredentialsMessage = "Invalid Username or password";
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<String> addNewUser (@RequestBody User user) {
		userRepository.save(user);
		return new ResponseEntity<String>("Saved", HttpStatus.OK);
	}
	
	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<String> modifyUser (@RequestBody User user) {
		userRepository.save(user);
		return new ResponseEntity<String>("Saved", HttpStatus.OK);
	}
	
	@PostMapping(path="/login", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<User> login (@RequestBody User user) throws InvalidCredentialsException {
		Optional<User> u = userRepository.findByEmailInAndPasswordIn(user.getEmail(), user.getPassword());
		User us;
		try {
			us = u.get();
		} catch (Exception e) {
			throw new InvalidCredentialsException(invalidCredentialsMessage);
		}
		return new ResponseEntity<User>(us, HttpStatus.OK);
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}
}
