package com.proyect.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}
