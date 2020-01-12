package com.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Entities.Play;
import com.project.Entities.PlayPK;

public interface PlayRepository extends JpaRepository<Play, PlayPK> {

}
