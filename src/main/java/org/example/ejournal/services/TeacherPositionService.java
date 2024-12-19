package org.example.ejournal.services;

import org.example.ejournal.dtos.response.TeacherPositionDtoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface TeacherPositionService {
	@Transactional
	List<TeacherPositionDtoResponse> assignMultipleTeacherSubjectsToClasses(Set<Long> teacherSubjectIds, Set<Long> classIds);
	
	@Transactional
	List<TeacherPositionDtoResponse> getAllTeacherPositionsBySchoolClass(long schoolClassId);
}
