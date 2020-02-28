package com.project.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"com.project.cinema","com.project.requestobjects","com.project.exceptions", "com.project.utils", "com.project.security"})
@EntityScan("com.project.entities")
@EnableJpaRepositories("com.project.repositories")
@EnableScheduling
@EnableCaching
public class CinemaApplication {
	
	public static void main(String[] args) { 
		SpringApplication.run(CinemaApplication.class, args);
	}
}
