package com.project.cinema;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.entities.User;
import com.project.exceptions.EmailUnavailableException;
import com.project.exceptions.InvalidCredentialsException;
import com.project.repositories.UserRepository;
import com.project.requestobjects.UserDTO;
import com.project.utils.BasicEntityUtils;

@CrossOrigin
@RestController
@RequestMapping(path="/user") 
public class UserController {
	@Autowired 
	private UserRepository userRepository;
	private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid Username or password";
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<User> addNewUser (@RequestBody UserDTO user) throws EmailUnavailableException {
		String email = user.getEmail();
		User us = new User(email, user.getName(), false, user.getPassword());
		checkEmailStatus(email);
		return BasicEntityUtils.save(us, userRepository);
	}
	
	private void checkEmailStatus(String email) throws EmailUnavailableException {
		Optional<User> us = userRepository.findByEmail(email);
		if(us.isPresent()) {
			throw new EmailUnavailableException(email);
		}
	}

	@PutMapping(path="/modify", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<User> modifyUser (@RequestBody UserDTO user) throws EmailUnavailableException {
		String newEmail = user.getEmail();
		if(StringUtils.isNotBlank(newEmail))
			checkEmailStatus(newEmail);
		Optional<User> us = userRepository.findById(user.getUserId());
		if(us.isPresent()) {
			User userDb = us.get();
			userDb.updateData(user);
			return BasicEntityUtils.save(userDb, userRepository);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(path="/login", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<User> login (@RequestBody UserDTO user) throws InvalidCredentialsException {
		Optional<User> u = userRepository.findByEmailInAndPasswordIn(user.getEmail(), user.getPassword());
		User us;
		try {
			us = u.get();
		} catch (Exception e) {
			throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
		}
		return new ResponseEntity<>(us, HttpStatus.OK);
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}
