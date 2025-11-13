package org.example.ejournal.repositories;

import org.example.ejournal.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByNameIgnoreCase(String name);
}
