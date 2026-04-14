package org.example.ejournal.repositories;

import org.example.ejournal.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByNameIgnoreCase(String name);

    // Count how many schools reference a given subject (for cleanup)
    @Query("""
           select count(s)
           from School s
           join s.subjects subj
           where subj.id = :subjectId
           """)
    long countSchoolsUsingSubject(@Param("subjectId") Long subjectId);

}
