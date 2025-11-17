package org.example.ejournal.repositories;

import org.example.ejournal.entities.Parent;
import org.example.ejournal.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByUser_EmailIgnoreCase(String email);

    // Find parent for a specific student (if Student has parent field)
    @Query("""
           select p
           from Parent p
           join p.children c
           where c.id = :studentId
           """)
    Optional<Parent> findByStudentId(@Param("studentId") long studentId);

    // All parents that have at least one child in the given school
    @Query("""
           select distinct p
           from Parent p
           join p.children c
           where c.school.id = :schoolId
           """)
    Set<Parent> findAllByChildSchoolId(@Param("schoolId") long schoolId);
}

