package org.example.ejournal.repositories;

import org.example.ejournal.models.Headmaster;
import org.example.ejournal.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

@Repository
public interface HeadmasterRepository extends JpaRepository<Headmaster, Long> {
    Optional<Headmaster> findByFirstNameAndLastName(String firstName, String lastName);

    List<Headmaster> findBySchool(School school);
}
