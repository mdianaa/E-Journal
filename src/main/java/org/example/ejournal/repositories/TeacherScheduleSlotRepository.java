package org.example.ejournal.repositories;

import org.example.ejournal.entities.TeacherScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherScheduleSlotRepository extends JpaRepository<TeacherScheduleSlot, Long> {

    List<TeacherScheduleSlot> findAllByTeacherSchedule_Teacher_IdAndTeacherSchedule_SemesterAndTeacherSchedule_ShiftAndDayOrderByPeriodNumberAsc(
            Long teacherId,
            String semester,
            String shift,
            String dayOfWeek
    );
}
