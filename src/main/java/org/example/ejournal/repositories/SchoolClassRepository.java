package org.example.ejournal.repositories;

import org.example.ejournal.entities.School;
import org.example.ejournal.entities.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByClassName(String className);

    List<SchoolClass> findBySchool(School school);
}
