package com.proyect.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.Entities.Play;
import com.proyect.Entities.PlayPK;

public interface PlayRepository extends JpaRepository<Play, PlayPK> {

}
