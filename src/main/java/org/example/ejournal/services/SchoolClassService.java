package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.SchoolClassDtoResponse;

import java.util.Set;

public interface SchoolClassService {

    SchoolClassDtoResponse createClass(SchoolClassDtoRequest schoolClassDto);

    SchoolClassDtoResponse changeHeadTeacher(long classId, long teacherId);

    SchoolClassDtoResponse showSchoolClass(long classId);

    Set<SchoolClassDtoResponse> showAllSchoolClassesInSchool(long schoolId);

    void deactivateClass(long classId);
}
