package com.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Room;

public interface SalaRepository extends JpaRepository<Room, Integer> {

}
