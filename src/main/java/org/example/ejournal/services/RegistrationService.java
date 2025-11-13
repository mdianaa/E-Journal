package org.example.ejournal.services;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.dtos.response.StudentDtoResponse;
import org.example.ejournal.dtos.response.TeacherDtoResponse;

public interface RegistrationService {

    ParentDtoResponse createParent(ParentDtoRequest parentDto);

    StudentDtoResponse createStudent(StudentDtoRequest studentDto);

    TeacherDtoResponse createTeacher(TeacherDtoRequest teacherDto);

    HeadmasterDtoResponse createHeadmaster(HeadmasterDtoRequest headmasterDto);

}
