package org.example.ejournal.repositories;

import org.example.ejournal.entities.TeacherSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherScheduleRepository extends JpaRepository<TeacherSchedule, Long> {

    Optional<TeacherSchedule> findByTeacher_IdAndSemesterAndShift(
            Long teacherId,
            String semester,
            String shift
    );

    List<TeacherSchedule> findAllByTeacher_Id(Long teacherId);
}
