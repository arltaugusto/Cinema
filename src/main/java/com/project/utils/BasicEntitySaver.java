package com.project.utils;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface BasicEntitySaver {
	
	public static <U> ResponseEntity<String> save(U entity, JpaRepository<U, ?> repository) {
		repository.save(entity);
		return new ResponseEntity<String>("Saved", HttpStatus.OK);
	}
}
