package org.example.ejournal.repositories;

import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.entities.Teacher;
import org.example.ejournal.entities.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject,Long> {
	boolean existsByTeacherAndSubject(Teacher teacher, Subject subject);
	
	Optional<TeacherSubject> findByTeacherAndSubject(Teacher teacher, Subject subject);
	
	@Query("SELECT ts.teacher FROM TeacherSubject ts WHERE ts.subject.id = :subjectId")
	List<Teacher> findAllTeachersBySubject(@Param("subjectId") Long subjectId);
	
	List<TeacherSubject> findAllBySubjectIn(Set<Subject> subjects);
	
	@Query("SELECT ts FROM TeacherSubject ts WHERE ts.teacher.school.id = :schoolId AND ts.subject.school.id = :schoolId")
	List<TeacherSubject> findAllBySchool(@Param("schoolId") Long schoolId);
}
