package org.example.ejournal.repositories;

import org.example.ejournal.entities.Parent;
import org.example.ejournal.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByFirstNameAndLastName(String firstName, String lastName);

    List<Parent> findBySchool(School school);
}
