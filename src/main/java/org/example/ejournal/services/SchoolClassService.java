package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.SchoolClassDtoResponse;

import java.util.List;

public interface SchoolClassService {
	
	SchoolClassDtoRequest createClass(SchoolClassDtoRequest schoolClassDto);
	
	
	
	@Transactional
	List<SchoolClassDtoResponse> viewAllClassesByAcademicYearAndSchoolId(long academicYearId, long schoolId);
	
	@Transactional
	List<SchoolClassDtoResponse> viewAllCurrentClassesBySchoolId(long schoolId);
	
	@Transactional
	List<SchoolClassDtoResponse> viewAllClassesAsHeadMaster(long academicYear);
	
	
	@Transactional
	List<SchoolClassDtoResponse> viewAllCurrentClassesAsHeadMaster();
	
	SchoolClassDtoRequest changeHeadTeacher(long classId, TeacherDtoRequest headTeacherDto);

    void deleteClass(long classId);
}
