package com.proyect.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.Entities.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{
//	List<Book> findByIdUser
}
