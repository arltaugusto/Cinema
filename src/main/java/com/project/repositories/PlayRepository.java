package com.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Play;
import com.project.entities.PlayPK;

public interface PlayRepository extends JpaRepository<Play, PlayPK> {

}
