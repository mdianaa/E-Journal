package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.TeacherDtoResponse;

import java.util.List;
import java.util.Set;

public interface TeacherService {
	
	@Transactional
	TeacherDtoResponse createTeacher(TeacherDtoRequest teacherDtoRequest);
	
	TeacherDtoResponse editTeacher(long teacherId, TeacherDtoRequest teacherDto);

//    TeacherDtoResponse changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos);

    TeacherDtoResponse removeHeadTeacherTitle(long teacherId);

    TeacherDtoResponse viewTeacher(long teacherId);
    
   // List<TeacherDtoResponse> viewHeadTeachers(long schoolId);
    
    //List<ScheduleDtoResponse> viewScheduleForDay(String day, String semester, String schoolClass);
	
	//List<TeacherDtoResponse> viewHeadTeachers(long schoolId);
	
	Set<TeacherDtoResponse> viewAllTeachersInSchool(long schoolId);
	
	Set<TeacherDtoResponse> viewHeadTeachersAsHeadmaster();
	
	@Transactional
	Set<TeacherDtoResponse> viewAllHeadTeachersInSchool(long schoolId);
	
	@Transactional
	Set<TeacherDtoResponse> viewAllTeachersAsHeadmaster();
	
	void deleteTeacher(long teacherId);
}
