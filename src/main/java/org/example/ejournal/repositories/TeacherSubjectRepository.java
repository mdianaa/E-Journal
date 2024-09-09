package org.example.ejournal.repositories;

import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.entities.Teacher;
import org.example.ejournal.entities.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject,Long> {
	boolean existsByTeacherAndSubject(Teacher teacher, Subject subject);

}
