package org.example.ejournal.repositories;

import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.entities.TeacherPosition;
import org.example.ejournal.entities.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherPositionRepository extends JpaRepository<TeacherPosition,Long> {
	Optional<TeacherPosition> findByTeacherSubjectAndSchoolClass(TeacherSubject teacherSubject, SchoolClass schoolClass);
	
	List<TeacherPosition> findAllBySchoolClass(SchoolClass schoolClass);
}
