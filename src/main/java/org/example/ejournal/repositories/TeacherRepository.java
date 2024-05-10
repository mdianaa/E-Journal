package org.example.ejournal.repositories;

import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.models.School;
import org.example.ejournal.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByFirstNameAndLastName(String firstName, String lastName);

    List<Teacher> findBySchool(School school);

//    @Query(value = "SELECT new org.example.ejournal.dtos.response.TeacherDtoResponse(t.firstName, t.lastName, t.school.name) " +
//            "FROM Teacher t " +
//            "WHERE t.id =: teacherId")
//    Optional<TeacherDtoResponse> findByTeacherId(long teacherId);
}
