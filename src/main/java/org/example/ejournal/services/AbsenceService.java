package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;

public interface AbsenceService {

    AbsenceDtoRequest createAbsence(AbsenceDtoRequest absence, TeacherDtoRequest teacherDto, StudentDtoRequest studentDto, SubjectDtoRequest subjectDto);

    void deleteAbsence(long absenceId);

    // TODO: извинени и неизвинени отсъствия като полета в базата?
}
