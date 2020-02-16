package com.project.utils;

import java.io.IOException;
import java.util.Optional;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.exceptions.EntityNotFoundException;

public abstract class BasicEntityUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(BasicEntityUtils.class);

	private BasicEntityUtils() {}
	
	public static <U> ResponseEntity<U> save(U entity, JpaRepository<U, ?> repository) {
		repository.save(entity);
		return new ResponseEntity<>(entity, HttpStatus.OK);
	}

	public static <T> T convertToEntityFromString(Class<T> object, String jsonString) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonString, object);
	}
	
	public static <T> T entityFinder(Optional<T> entity) throws EntityNotFoundException {
		if(!entity.isPresent()) {
			EntityNotFoundException ex = new EntityNotFoundException(entity.getClass().getName());
			logger.error("Entity not found", ex);
			throw ex;
		}
		return entity.get();
	}
}
