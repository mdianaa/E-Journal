package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;

import java.util.Set;

public interface AbsenceService {

    AbsenceDtoResponse createAbsence(AbsenceDtoRequest absence, long studentId, long subjectId);

    Set<AbsenceDtoResponse> getAbsencesForStudent(long studentId);

    Set<AbsenceDtoResponse> showAllAbsencesForStudentAsStudent();

    Set<AbsenceDtoResponse> showAllAbsencesForStudentAsParent(long studentId);

    Set<AbsenceDtoResponse> showAllAbsencesForStudentAsTeacher(long studentId);

    Set<AbsenceDtoResponse> showAllAbsencesForStudentAsHeadmaster(long studentId);

    void excuseAbsence(long absenceId);
}
