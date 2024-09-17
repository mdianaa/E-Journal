package org.example.ejournal.repositories;

import org.example.ejournal.entities.School;
import org.example.ejournal.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByFirstNameAndLastName(String firstName, String lastName);
    
    Optional<Teacher> findByFirstNameAndLastNameAndSchoolId(String firstName, String lastName, long school);
    List<Teacher> findBySchool(School school);
    
    List<Teacher> findByIsHeadTeacherTrueAndSchool(School school);
    List<Teacher> findByIsHeadTeacherTrue();
    
    Set<Teacher> findByIsHeadTeacherTrueAndSchoolId(long schoolId);
//    @Query(value = "SELECT new org.example.ejournal.dtos.response.TeacherDtoResponse(t.firstName, t.lastName, t.school.name) " +
//            "FROM Teacher t " +
//            "WHERE t.id =: teacherId")
//    Optional<TeacherDtoResponse> findByTeacherId(long teacherId);
}
