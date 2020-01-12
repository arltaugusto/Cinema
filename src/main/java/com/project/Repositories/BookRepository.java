package com.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Entities.Booking;

public interface BookRepository extends JpaRepository<Booking, Integer>{
//	List<Book> findByIdUser
}
