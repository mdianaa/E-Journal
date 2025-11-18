package org.example.ejournal.services;

import org.example.ejournal.dtos.response.*;

import java.util.Set;

public interface StudentService {

    StudentDtoResponse viewStudent(String username);

    Set<StudentDtoResponse> viewAllStudentsInSchool(long schoolId);

    Set<StudentDtoResponse> viewAllStudentsInClass(long schoolClassId);

    void withdrawStudent(long studentId);

    void deleteStudent(long studentId);
}
