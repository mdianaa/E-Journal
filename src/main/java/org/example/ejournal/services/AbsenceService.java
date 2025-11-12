package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;

import java.util.Set;

public interface AbsenceService {

    AbsenceDtoResponse createAbsence(AbsenceDtoRequest absence);

    Set<AbsenceDtoResponse> viewAllAbsencesForStudent(long studentId);

    Set<AbsenceDtoResponse> viewAllAbsencesGivenByTeacher(long teacherId);

    void excuseAbsence(long absenceId);
}
