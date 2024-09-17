package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;

public interface AbsenceService {

    AbsenceDtoResponse createAbsence(AbsenceDtoRequest absence, long studentId, long subjectId);

    void excuseAbsence(long absenceId);
}
