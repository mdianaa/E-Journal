package org.example.ejournal.repositories;

import org.example.ejournal.models.Parent;
import org.example.ejournal.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByFirstNameAndLastName(String firstName, String lastName);

    List<Parent> findBySchool(School school);
}
