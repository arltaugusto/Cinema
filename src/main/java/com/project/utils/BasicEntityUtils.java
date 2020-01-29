package com.project.utils;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface BasicEntityUtils {
	
	public static <U> ResponseEntity<U> save(U entity, JpaRepository<U, ?> repository) {
		repository.save(entity);
		return new ResponseEntity<>(entity, HttpStatus.OK);
	}

	public static <T> T convertToEntityFromString(Class<T> object, String jsonString) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonString, object);
	}
}
