package org.example.ejournal.services;

import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.models.Grade;

public interface GradeService {

    GradeDtoRequest createGrade(GradeDtoRequest grade, TeacherDtoRequest teacherDto, SubjectDtoRequest subjectDto, StudentDtoRequest studentDto);

    GradeDtoRequest editGrade(long gradeId, GradeDtoRequest gradeDto);
}
