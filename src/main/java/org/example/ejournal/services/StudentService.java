package org.example.ejournal.services;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.models.Absence;
import org.example.ejournal.models.Grade;
import org.example.ejournal.models.Student;

import java.util.Set;

public interface StudentService {

    StudentDtoRequest createStudent(StudentDtoRequest studentDto, SchoolDtoRequest schoolDto, SchoolClassDtoRequest schoolClassDto, ParentDtoRequest parentDto);

    StudentDtoRequest editStudent(long studentId, StudentDtoRequest studentDto);

    Set<Grade> showAllGradesForSubject(long studentId, SubjectDtoRequest subjectDto);

    Set<Absence> showAllAbsencesForStudent(long studentId);

    Set<Student> showAllStudentsInSchool(long schoolId);

    void withdrawStudent(long studentId);
}
