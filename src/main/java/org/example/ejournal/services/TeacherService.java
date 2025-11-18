package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.TeacherDtoResponse;

import java.util.Set;

public interface TeacherService {

    TeacherDtoResponse changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos);

    // remove that a teacher can be a head teacher of a class
    TeacherDtoResponse removeHeadTeacherTitle(long teacherId);

    TeacherDtoResponse viewTeacher(long teacherId);

    TeacherDtoResponse viewHeadTeacher(long teacherId, long schoolClassId);

    Set<TeacherDtoResponse> viewAllHeadTeachersInSchool(long schoolId);

    Set<TeacherDtoResponse> viewAllTeachersInSchool(long schoolId);

    void deleteTeacher(long teacherId);
}
