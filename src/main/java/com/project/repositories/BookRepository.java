package com.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Booking;

public interface BookRepository extends JpaRepository<Booking, Integer> {
//	List<Book> findByIdUser
}
