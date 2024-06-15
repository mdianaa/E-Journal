package org.example.ejournal.repositories;

import org.example.ejournal.entities.Absence;
import org.example.ejournal.entities.Student;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findAllByStudent(Student student);

    List<Absence> findBySubject(Subject subject);

    List<Absence> findAllByTeacher(Teacher teacher);
}
