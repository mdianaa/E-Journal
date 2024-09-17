package org.example.ejournal.repositories;

import org.example.ejournal.entities.Parent;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Student> findByUserAuthenticationUsername(String username);

    List<Student> findAllByParent(Parent parent);

    List<Student> findAllByCurrentSchoolClass(SchoolClass schoolClass);

    List<Student> findBySchool(School school);
}
