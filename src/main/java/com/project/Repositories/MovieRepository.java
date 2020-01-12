package com.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
	
}
