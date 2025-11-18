package org.example.ejournal.repositories;

import org.example.ejournal.entities.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {

    Optional<SchoolClass> findByClassNameAndSchool_Id(String name, Long schoolId);

    // Active class with same name in a school (to prevent duplicates for current year)
    boolean existsBySchool_IdAndClassNameIgnoreCaseAndDeactivatedFalse(Long schoolId, String className);

    // Find all classes for a school; caller can filter active-only in query level
    @Query("""
           select sc from SchoolClass sc
           where sc.school.id = :schoolId and (:activeOnly = false or sc.deactivated = false)
           """)
    Set<SchoolClass> findAllBySchoolId(@Param("schoolId") Long schoolId, @Param("activeOnly") boolean activeOnly);

    // Optional: ensure a teacher isn’t already head teacher of another active class
    @Query("""
           select count(sc) from SchoolClass sc
           where sc.headTeacher.id = :teacherId and sc.deactivated = false
           """)
    long countActiveByHeadTeacher(@Param("teacherId") Long teacherId);

    @Query("""
           select sc from SchoolClass sc
           where sc.id = :classId and sc.headTeacher.id = :teacherId
           """)
    Optional<SchoolClass> findByIdAndHeadTeacher(@Param("classId") Long classId,
                                                 @Param("teacherId") Long teacherId);

    @Query("""
           select sc from SchoolClass sc
           where sc.className = :className and sc.school.id = :schoolId and sc.deactivated = false
           """)
    Optional<SchoolClass> findActiveByClassNameAndSchoolId(@Param("className") String className,
                                                           @Param("schoolId") Long schoolId);

    @Modifying
    @Query("""
           UPDATE SchoolClass sc
           SET sc.deactivated = true
           WHERE sc.deactivated = false
             AND sc.schoolYearEnd < :today
           """)
    void deactivateClassesForPastSchoolYear(@Param("today") LocalDate today);

}
