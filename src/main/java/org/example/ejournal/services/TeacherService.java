package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.models.Teacher;

import java.util.Set;

public interface TeacherService {

    TeacherDtoRequest createTeacher(TeacherDtoRequest teacherDto, SchoolDtoRequest schoolDto, Set<SubjectDtoRequest> subjectDtos);

    TeacherDtoRequest editTeacher(long teacherId, TeacherDtoRequest teacherDto);

    TeacherDtoRequest changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos);

    // TODO: трябва ли да е тук?
    TeacherDtoRequest removeHeadTeacherTitle(long teacherId);

    TeacherDtoResponse viewTeacher(long teacherId);

    Set<TeacherDtoResponse> viewAllTeachersInSchool(long schoolId);

    void deleteTeacher(long teacherId);
}
