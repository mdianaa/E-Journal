package org.example.ejournal.repositories;

import org.example.ejournal.entities.BadNote;
import org.example.ejournal.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface BadNoteRepository extends JpaRepository<BadNote, Long> {

    Set<BadNote> findAllByStudent_IdOrderByDayDesc(long studentId);

    Set<BadNote> findAllByTeacher_IdOrderByDayDesc(long teacherId);
}
