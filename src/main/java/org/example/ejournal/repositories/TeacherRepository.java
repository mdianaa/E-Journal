package org.example.ejournal.repositories;

import org.example.ejournal.models.School;
import org.example.ejournal.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByFirstNameAndLastName(String lastName, String lastName1);

    List<Teacher> findBySchool(School school);
}
