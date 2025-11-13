package org.example.ejournal.repositories;

import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.entities.Schedule;
import org.example.ejournal.entities.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findBySchoolClass_Id(Long schoolClassId);

    Optional<Schedule> findBySchoolClass_IdAndSemesterIgnoreCaseAndShiftIgnoreCase(
            Long schoolClassId, String semester, String shift);

}

