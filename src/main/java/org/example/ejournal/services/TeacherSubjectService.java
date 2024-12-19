package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.response.TeacherSubjectDtoResponse;

import java.util.List;
import java.util.Set;

public interface TeacherSubjectService {
	@Transactional
	TeacherSubjectDtoResponse assignTeacherToSubject(long teacherId, long subjectId);
	
	
	
	void assignMultipleTeachersToMultipleSubjects(Set<Long> teacherIds, Set<Long> subjectIds);
	
	// Method for Admin: Fetch subjects and teachers by schoolId
	@Transactional
	List<TeacherSubjectDtoResponse> getAllSubjectsAndTeachersBySchool(long schoolId);
	
	// Method for Headmaster: Fetch subjects and teachers for the headmaster's school
	@Transactional
	List<TeacherSubjectDtoResponse> getAllSubjectsAndTeachersForHeadmaster();
}
