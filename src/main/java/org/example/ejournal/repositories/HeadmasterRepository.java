package org.example.ejournal.repositories;

import org.example.ejournal.entities.Headmaster;
import org.example.ejournal.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeadmasterRepository extends JpaRepository<Headmaster, Long> {
    Optional<Headmaster> findByFirstNameAndLastName(String firstName, String lastName);

    List<Headmaster> findBySchool(School school);
}
