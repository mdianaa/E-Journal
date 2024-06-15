package org.example.ejournal.repositories;

import org.example.ejournal.entities.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long> {
    Optional<UserAuthentication> findByUsername(String username);
}
