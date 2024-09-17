package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.*;

import java.util.List;
import java.util.Set;

public interface StudentService {

    StudentDtoResponse createStudent(StudentDtoRequest studentDto, SchoolDtoRequest schoolDto, SchoolClassDtoRequest schoolClassDto, ParentDtoRequest parentDto, UserRegisterDtoRequest userRegisterDtoRequest);

    StudentDtoResponse editStudent(long studentId, StudentDtoRequest studentDto);

    List<GradeDtoResponse> showAllGradesForSubject(String username, SubjectDtoRequest subjectDto);

    Set<AbsenceDtoResponse> showAllAbsencesForStudent(String username);

    List<BadNoteDtoResponse> showAllBadNotesForStudent(String username);

    StudentDtoResponse viewStudent(String username);

    //List<ScheduleDtoResponse> viewScheduleForDay(String day, String semester, String schoolClass);

    Set<StudentDtoResponse> showAllStudentsInSchool(long schoolId);
    
    @Transactional
    Set<StudentDtoResponse> showAllStudentsInClass(long schoolClassId);
    
    void withdrawStudent(long studentId);
}
