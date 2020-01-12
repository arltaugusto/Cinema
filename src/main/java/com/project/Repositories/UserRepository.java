package com.project.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Entities.User;

public interface UserRepository extends JpaRepository<User, String> {
	
	Optional<User> findByEmailInAndPasswordIn(String email, String password);
}
