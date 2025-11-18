package org.example.ejournal.repositories;

import org.example.ejournal.entities.Parent;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser_Id(Long userId);

    Optional<Student> findByUser_EmailIgnoreCase(String email);

    @Query("select s from Student s where s.school.id = :schoolId")
    Set<Student> findAllBySchoolId(@Param("schoolId") Long schoolId);

    Set<Student> findAllBySchoolClassId(@Param("classId") Long classId);
}
