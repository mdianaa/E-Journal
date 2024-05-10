package org.example.ejournal.repositories;

import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.WeekDay;
import org.example.ejournal.models.Schedule;
import org.example.ejournal.models.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT new org.example.ejournal.dtos.response.ScheduleDtoResponse(s.day, s.semester, s.shiftType, s.periodType, s.schoolClass.className, s.subject.subjectType) " +
            "FROM Schedule s " +
            "WHERE s.day = :day " +
            "AND s.schoolClass = :schoolClass " +
            "AND s.semester = :semester")
    Optional<ScheduleDtoResponse> findScheduleForDayAndClassAndSemester(WeekDay day, SchoolClass schoolClass, SemesterType semester);

}

