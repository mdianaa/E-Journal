package org.example.ejournal.repositories;

import org.example.ejournal.entities.School;
import org.example.ejournal.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByFirstNameAndLastName(String firstName, String lastName);
    
    Optional<Teacher> findByFirstNameAndLastNameAndSchool(String firstName, String lastName,School school);
    List<Teacher> findBySchool(School school);
    
    List<Teacher> findByHeadTeacherTrue();

//    @Query(value = "SELECT new org.example.ejournal.dtos.response.TeacherDtoResponse(t.firstName, t.lastName, t.school.name) " +
//            "FROM Teacher t " +
//            "WHERE t.id =: teacherId")
//    Optional<TeacherDtoResponse> findByTeacherId(long teacherId);
}
