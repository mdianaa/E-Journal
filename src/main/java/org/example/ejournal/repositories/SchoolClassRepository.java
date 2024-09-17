package org.example.ejournal.repositories;

import org.example.ejournal.entities.AcademicYear;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.enums.ClassSection;
import org.example.ejournal.enums.GradeLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByGradeLevelAndClassSection(GradeLevel gradeLevel, ClassSection classSection);
    List<SchoolClass> findAllBySchoolAndAcademicYear(School school, AcademicYear academicyear);
    List<SchoolClass> findBySchool(School school);
    
    
}
