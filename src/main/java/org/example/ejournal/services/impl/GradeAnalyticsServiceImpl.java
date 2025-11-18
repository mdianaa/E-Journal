package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.GradeAnalyticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.example.ejournal.util.CheckExistsUtil.*;

@Service
@RequiredArgsConstructor
public class GradeAnalyticsServiceImpl implements GradeAnalyticsService {

    private final GradeRepository gradeRepository;
    private final SchoolRepository schoolRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Override
    @Transactional
    public BigDecimal viewAverageGradeForSubject(long schoolId, String subjectType, String classNumber) {
        checkIfSchoolExists(schoolRepository, schoolId);

        schoolClassRepository.findActiveByClassNameAndSchoolId(classNumber, schoolId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Active class '" + classNumber + "' not found in school " + schoolId));

        Double avg = gradeRepository.avgForSubjectTypeInSchoolClass(schoolId, subjectType, classNumber);
        return toScale(avg); // 2 decimals, HALF_UP, null -> 0.00
    }

    @Override
    @Transactional
    public BigDecimal viewAverageGradeForTeacher(long teacherId) {
        checkIfTeacherExists(teacherRepository, teacherId);

        return toScale(gradeRepository.avgForTeacher(teacherId));
    }

    @Override
    @Transactional
    public BigDecimal viewAverageGradeForStudent(long studentId) {
        checkIfStudentExists(studentRepository, studentId);

        return toScale(gradeRepository.avgForStudent(studentId));
    }

    @Override
    @Transactional
    public BigDecimal viewAverageGradeForSchool(long schoolId) {
        checkIfSchoolExists(schoolRepository, schoolId);

        return toScale(gradeRepository.avgForSchool(schoolId));
    }

    @Override
    @Transactional
    public int viewGradeCountInSchoolClass(BigDecimal grade, long schoolClassId) {
        SchoolClass sc = schoolClassRepository.findById(schoolClassId)
                .orElseThrow(() -> new IllegalArgumentException("School class with id " + schoolClassId + " not found"));

        if (sc.isDeactivated()) {
            throw new IllegalStateException("Class " + schoolClassId + " is deactivated and excluded from analytics");
        }

        return gradeRepository.countInSchoolClass(normalize(grade), schoolClassId);
    }

    @Override
    @Transactional
    public int viewGradeCountForSubject(BigDecimal grade, long subjectId) {
        checkIfSubjectExists(subjectRepository, subjectId);

        return gradeRepository.countForSubject(normalize(grade), subjectId);
    }

    @Override
    @Transactional
    public int viewGradeCountForTeacher(BigDecimal grade, long teacherId) {
        checkIfTeacherExists(teacherRepository, teacherId);

        return gradeRepository.countForTeacher(normalize(grade), teacherId);
    }

    @Override
    @Transactional
    public int viewGradeCountInSchool(BigDecimal grade, long schoolId) {
        checkIfSchoolExists(schoolRepository, schoolId);

        return gradeRepository.countInSchool(normalize(grade), schoolId);
    }

    private static BigDecimal normalize(BigDecimal v) {
        return v == null ? null : v.setScale(2, RoundingMode.UNNECESSARY);
    }

    private static BigDecimal toScale(Double avg) {
        if (avg == null) return new BigDecimal("0.00");
        return new BigDecimal(avg.toString()).setScale(2, RoundingMode.HALF_UP);
    }
}
