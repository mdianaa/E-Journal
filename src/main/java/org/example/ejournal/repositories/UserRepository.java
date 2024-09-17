package org.example.ejournal.repositories;

import org.example.ejournal.entities.User;
import org.example.ejournal.entities.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserAuthentication_Username(String username);
}
