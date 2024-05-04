package org.example.ejournal.repositories;

import org.example.ejournal.enums.SubjectType;
import org.example.ejournal.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findBySubjectType(SubjectType subjectType);
}
