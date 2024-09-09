package org.example.ejournal.repositories;

import org.example.ejournal.entities.TeacherPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherPositionRepository extends JpaRepository<TeacherPosition,Long> {
}
