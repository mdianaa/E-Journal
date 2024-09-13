package org.example.ejournal.repositories;

import org.example.ejournal.entities.Headmaster;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface HeadmasterRepository extends JpaRepository<Headmaster, Long> {
    Optional<Headmaster> findByFirstNameAndLastName(String firstName, String lastName);
    Optional<Headmaster> findByUserAuthentication_Username(String userAuthentication_username);
    List<Headmaster> findBySchool(School school);
}
