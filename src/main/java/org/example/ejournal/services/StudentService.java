package org.example.ejournal.services;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.*;

import java.util.List;
import java.util.Set;

public interface StudentService {

    StudentDtoResponse createStudent(StudentDtoRequest studentDto, SchoolDtoRequest schoolDto, SchoolClassDtoRequest schoolClassDto, ParentDtoRequest parentDto, UserRegisterDtoRequest userRegisterDtoRequest);

    StudentDtoResponse editStudent(long studentId, StudentDtoRequest studentDto);

    List<GradeDtoResponse> showAllGradesForSubject(long studentId, SubjectDtoRequest subjectDto);

    Set<AbsenceDtoResponse> showAllAbsencesForStudent(long studentId);

    List<BadNoteDtoResponse> showAllBadNotesForStudent(long studentId);

    StudentDtoResponse viewStudent(long studentId);

    Set<StudentDtoResponse> showAllStudentsInSchool(long schoolId);

    void withdrawStudent(long studentId);
}
