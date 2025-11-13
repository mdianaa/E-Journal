package org.example.ejournal.repositories;

import org.example.ejournal.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findAllByStudent_IdAndSubject_IdOrderByIdDesc(Long studentId, Long subjectId);

    List<Grade> findAllByStudent_IdOrderByIdDesc(Long studentId);

    // subjectType in a particular (active) class of a school
    @Query("""
           select avg(g.value)
           from Grade g
           join g.student st
           join st.schoolClass sc
           join g.subject subj
           where sc.school.id = :schoolId
             and sc.className = :className
             and sc.deactivated = false
             and lower(subj.name) = lower(:subjectType)
           """)
    Double avgForSubjectTypeInSchoolClass(@Param("schoolId") Long schoolId,
                                          @Param("subjectType") String subjectType,
                                          @Param("className") String className);

    // teacher average, excluding grades from deactivated classes
    @Query("""
           select avg(g.value)
           from Grade g
           join g.student st
           join st.schoolClass sc
           where g.teacher.id = :teacherId
             and sc.deactivated = false
           """)
    Double avgForTeacher(@Param("teacherId") Long teacherId);

    // student average (no class filter; the student’s current/old class doesn’t matter here)
    @Query("""
           select avg(g.value)
           from Grade g
           where g.student.id = :studentId
           """)
    Double avgForStudent(@Param("studentId") Long studentId);

    // school-wide average, excluding deactivated classes
    @Query("""
           select avg(g.value)
           from Grade g
           join g.student st
           join st.schoolClass sc
           where sc.school.id = :schoolId
             and sc.deactivated = false
           """)
    Double avgForSchool(@Param("schoolId") Long schoolId);

    // counts — all excluding deactivated classes
    @Query("""
           select count(g)
           from Grade g
           join g.student st
           join st.schoolClass sc
           where sc.id = :classId
             and sc.deactivated = false
             and g.value = :grade
           """)
    int countInSchoolClass(@Param("grade") BigDecimal grade, @Param("classId") Long classId);

    @Query("""
           select count(g)
           from Grade g
           join g.student st
           join st.schoolClass sc
           where g.subject.id = :subjectId
             and sc.deactivated = false
             and g.value = :grade
           """)
    int countForSubject(@Param("grade") BigDecimal grade, @Param("subjectId") Long subjectId);

    @Query("""
           select count(g)
           from Grade g
           join g.student st
           join st.schoolClass sc
           where g.gradedBy.id = :teacherId
             and sc.deactivated = false
             and g.value = :grade
           """)
    int countForTeacher(@Param("grade") BigDecimal grade, @Param("teacherId") Long teacherId);

    @Query("""
           select count(g)
           from Grade g
           join g.student st
           join st.schoolClass sc
           where sc.school.id = :schoolId
             and sc.deactivated = false
             and g.value = :grade
           """)
    int countInSchool(@Param("grade") BigDecimal grade, @Param("schoolId") Long schoolId);

    long countByStudent_Id(Long studentId);
}
