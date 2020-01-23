package com.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
	
	Optional<User> findByEmailInAndPasswordIn(String email, String password);
	Optional<User> findByEmail(String email);

}
