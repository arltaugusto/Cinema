package com.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, String> {
	
}
