package org.example.ejournal.repositories;

import org.example.ejournal.enums.SubjectType;
import org.example.ejournal.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query(value = "SELECT count(g) FROM Grade g JOIN g.student s " +
            "WHERE s.schoolClass.className = :schoolClass AND g.value = :grade")
    int findCountOfGradeBySchoolClass(BigDecimal grade, String schoolClass);

    @Query(value = "SELECT count(g) FROM Grade g JOIN g.subject s " +
            "WHERE s.subjectType = :subjectType AND g.value = :grade")
    int findCountOfGradeBySubject(BigDecimal grade, SubjectType subjectType);

    @Query(value = "SELECT count(g) FROM Grade g JOIN g.gradedByTeacher t " +
            "WHERE t.id = :teacherId AND g.value = :grade")
    int findCountOfGradeByTeacher(BigDecimal grade, long teacherId);

    @Query(value = "SELECT count(g) FROM Grade g JOIN g.student s JOIN s.school sch " +
            "WHERE sch.id = :schoolId AND g.value = :grade")
    int findCountOfGradeBySchool(BigDecimal grade, long schoolId);

    @Query(value = "SELECT AVG(g.value) FROM Grade g " +
            "JOIN g.subject s " +
            "JOIN g.student st " +
            "JOIN st.schoolClass sc " +
            "JOIN sc.school sch " +
            "WHERE sch.id = :schoolId " +
            "AND s.subjectType = :subjectType AND sc.className = :classNumber")
    BigDecimal findAverageGradeForSubject(long schoolId, SubjectType subjectType, String classNumber);

    @Query(value = "SELECT AVG(g.value) FROM Grade g " +
            "WHERE g.gradedByTeacher.id = :teacherId ")
    BigDecimal findAverageGradeForTeacher(long teacherId);

    @Query(value = "SELECT AVG(g.value) FROM Grade g " +
            "WHERE g.student.id = :studentId")
    BigDecimal findAverageGradeForStudent(long studentId);

    @Query(value = "SELECT AVG(g.value) FROM Grade g " +
            "WHERE g.gradedByTeacher.school.id = :schoolId")
    BigDecimal findAverageGradeForSchool(long schoolId);
}
