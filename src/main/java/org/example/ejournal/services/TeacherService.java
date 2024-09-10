package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.dtos.response.TeacherDtoResponse;

import java.util.List;
import java.util.Set;

public interface TeacherService {
    TeacherDtoResponse createTeacher(AdminRegisterDtoRequest registerDtoRequest);
    
    TeacherDtoResponse editTeacher(long teacherId, TeacherDtoRequest teacherDto);

//    TeacherDtoResponse changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos);

    TeacherDtoResponse removeHeadTeacherTitle(long teacherId);

    TeacherDtoResponse viewTeacher(long teacherId);

    List<ScheduleDtoResponse> viewScheduleForDay(String day, String semester, String schoolClass);

    Set<TeacherDtoResponse> viewAllTeachersInSchool(long schoolId);

    void deleteTeacher(long teacherId);
}
