package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;

public interface AbsenceService {

    AbsenceDtoResponse createAbsence(AbsenceDtoRequest absence, TeacherDtoRequest teacherDto, StudentDtoRequest studentDto, SubjectDtoRequest subjectDto);

    void excuseAbsence(long absenceId);
}
