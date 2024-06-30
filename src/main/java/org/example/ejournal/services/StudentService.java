package org.example.ejournal.services;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.dtos.response.StudentDtoResponse;

import java.util.List;
import java.util.Set;

public interface StudentService {

    StudentDtoRequest createStudent(StudentDtoRequest studentDto, SchoolDtoRequest schoolDto, SchoolClassDtoRequest schoolClassDto, ParentDtoRequest parentDto, UserRegisterDtoRequest userRegisterDtoRequest);

    StudentDtoRequest editStudent(long studentId, StudentDtoRequest studentDto);

    List<GradeDtoResponse> showAllGradesForSubject(long studentId, SubjectDtoRequest subjectDto);

    Set<AbsenceDtoResponse> showAllAbsencesForStudent(long studentId);

    StudentDtoResponse viewStudent(long studentId);

    Set<StudentDtoResponse> showAllStudentsInSchool(long schoolId);

    void withdrawStudent(long studentId);
}
