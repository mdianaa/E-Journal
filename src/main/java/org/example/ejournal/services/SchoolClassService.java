package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;

public interface SchoolClassService {
	
	SchoolClassDtoRequest createClass(SchoolClassDtoRequest schoolClassDto);
	
	SchoolClassDtoRequest changeHeadTeacher(long classId, TeacherDtoRequest headTeacherDto);

    void deleteClass(long classId);
}
