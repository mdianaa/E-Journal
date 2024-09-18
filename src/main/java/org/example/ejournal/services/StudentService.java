package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.*;

import java.util.List;
import java.util.Set;

public interface StudentService {
    @Transactional
    StudentDtoResponse createStudent(StudentDtoRequest studentDtoRequest);
    
    StudentDtoResponse editStudent(long studentId, StudentDtoRequest studentDto);

    StudentDtoResponse viewStudent(String username);

    //List<ScheduleDtoResponse> viewScheduleForDay(String day, String semester, String schoolClass);
    
    @Transactional
    Set<StudentDtoResponse> showAllStudentsInSchoolAsHeadmaster();
    
    Set<StudentDtoResponse> showAllStudentsInSchool(long schoolId);
    
    @Transactional
    Set<StudentDtoResponse> showAllStudentsInClass(long schoolClassId);
    
    void withdrawStudent(long studentId);
}
