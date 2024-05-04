package org.example.ejournal.repositories;

import org.example.ejournal.models.Parent;
import org.example.ejournal.models.School;
import org.example.ejournal.models.SchoolClass;
import org.example.ejournal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);

    List<Student> findAllByParent(Parent parent);

    List<Student> findAllBySchoolClass(SchoolClass schoolClass);

    List<Student> findBySchool(School school);
}
