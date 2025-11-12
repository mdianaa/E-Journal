package org.example.ejournal.services;

import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.GradeDtoResponse;

import java.math.BigDecimal;
import java.util.Set;

public interface GradeService {

    GradeDtoResponse createGrade(GradeDtoRequest grade);

    // GradeDtoResponse editGrade(long gradeId, GradeDtoRequest gradeDto);

    Set<GradeDtoResponse> showAllStudentGradesForSubject(long studentId, long subjectId);

    Set<GradeDtoResponse> showAllStudentGrades(long studentId);

}