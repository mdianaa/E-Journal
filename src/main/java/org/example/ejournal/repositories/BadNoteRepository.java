package org.example.ejournal.repositories;

import org.example.ejournal.entities.BadNote;
import org.example.ejournal.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadNoteRepository extends JpaRepository<BadNote, Long> {
    List<BadNote> findAllByStudent(Student student);
}
