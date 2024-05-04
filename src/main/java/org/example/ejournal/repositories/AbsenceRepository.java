package org.example.ejournal.repositories;

import org.example.ejournal.models.Absence;
import org.example.ejournal.models.Student;
import org.example.ejournal.models.Subject;
import org.example.ejournal.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findAllByStudent(Student student);

    List<Absence> findBySubject(Subject subject);

    List<Absence> findAllByTeacher(Teacher teacher);
}
