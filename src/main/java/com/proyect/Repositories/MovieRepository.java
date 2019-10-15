package com.proyect.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.Entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
	
}
