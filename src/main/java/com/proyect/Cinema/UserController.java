package com.proyect.Cinema;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyect.Entities.User;
import com.proyect.Repositories.UserRepository;
import com.proyect.exceptions.InvalidCredentialsException;


@Controller
@CrossOrigin
@RequestMapping(path="/user") 
public class UserController {
	@Autowired 
	private UserRepository userRepository;
	
	private static final String invalidCredentialsMessage = "Invalid Username or password";
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody String addNewUser (@RequestBody User user) {
		userRepository.save(user);
		return "Saved";
	}
	
	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody String modifyUser (@RequestBody User user) {
		userRepository.save(user);
		return "Saved";
	}
	
	@PostMapping(path="/login", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody String login (@RequestBody User user) throws InvalidCredentialsException {
		Optional<User> u = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
		try {
			u.get();
		} catch (Exception e) {
			throw new InvalidCredentialsException(invalidCredentialsMessage);
		}
		return "Login Success";
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}
}
