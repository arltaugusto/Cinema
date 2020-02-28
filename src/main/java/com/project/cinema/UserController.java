package com.project.cinema;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.entities.User;
import com.project.exceptions.EmailUnavailableException;
import com.project.exceptions.EntityNotFoundException;
import com.project.exceptions.InvalidCredentialsException;
import com.project.repositories.UserRepository;
import com.project.requestobjects.AuthenticationRequest;
import com.project.requestobjects.AuthenticationResponse;
import com.project.requestobjects.UserDTO;
import com.project.security.JwtUtils;
import com.project.security.MyUserDetailsService;
import com.project.utils.BasicEntityUtils;

@CrossOrigin
@RestController
@RequestMapping(path="/user") 
public class UserController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtils jwtTokenUtils;
	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private BookController bookController;
	
	private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid Username or password";
	
	@PostMapping(path="/add", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<User> addNewUser (@RequestBody UserDTO user) throws EmailUnavailableException {
		String email = user.getEmail();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
		User us = new User(email, user.getName(), "ROLE_USER", passwordEncoder.encode(user.getPassword()));
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
	public @ResponseBody ResponseEntity<User> modifyUser (@RequestBody UserDTO user) throws EmailUnavailableException, EntityNotFoundException, InvalidCredentialsException {
		String newEmail = user.getEmail();
		User userDb = BasicEntityUtils.entityFinder(userRepository.findById(user.getUserId()));
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		validatePassword(user, userDb, passwordEncoder);
		if(StringUtils.isNotBlank(newEmail) && !userDb.getEmail().equals(newEmail))
			checkEmailStatus(newEmail);
		userDb.updateData(user);
		return BasicEntityUtils.save(userDb, userRepository);
	}
	
	private void validatePassword(UserDTO user, User userDb, BCryptPasswordEncoder passwordEncoder)	throws InvalidCredentialsException {
		if(!passwordEncoder.matches(user.getPassword(), userDb.getPassword())) {
			throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
		}
	}
	
	@PostMapping(path="/login", consumes = "application/json", produces = "application/json") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<AuthenticationResponse> login (@RequestBody AuthenticationRequest authenticationRequest) throws InvalidCredentialsException {
		try {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
				);
		} catch(BadCredentialsException e) {
			throw e;
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String jwt = jwtTokenUtils.generateToken(userDetails);
		return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody User getUserData(@PathVariable("id") String id) {
		return userRepository.findById(id).get();
	}
	
	@GetMapping(path="/getUser") 
	public @ResponseBody User getUserFromJwt(@RequestHeader("Authorization") String jwt) throws EntityNotFoundException {
		String userToken = jwt.split(StringUtils.SPACE)[1];
		return BasicEntityUtils.entityFinder(userRepository.findByEmail(jwtTokenUtils.extractUsername(userToken)));
	}
}
