package org.example.ejournal.web;

import org.example.ejournal.dtos.request.AssignTeachersToSubjectsRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.dtos.response.TeacherSubjectDtoResponse;
import org.example.ejournal.services.TeacherSubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@ControllerAdvice
@RequestMapping("/teacher-subject")
public class TeacherSubjectController {
	
	public final TeacherSubjectService teacherSubjectService;
	
	
	public TeacherSubjectController(TeacherSubjectService teacherSubjectService) {
		this.teacherSubjectService = teacherSubjectService;
	}
	
	@PostMapping("/assign-multiple-teachers-subjects")
	@PreAuthorize("hasAnyRole('ADMIN', 'HEADMASTER')")
	public ResponseEntity<?> assignMultipleTeachersToMultipleSubjects(
			@RequestBody AssignTeachersToSubjectsRequest request) {
		try {
			teacherSubjectService.assignMultipleTeachersToMultipleSubjects(request.getTeacherIds(), request.getSubjectIds());
			return ResponseEntity.ok("Assignments successfully completed.");
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping()
	@PreAuthorize("hasRole('HEADMASTER')")
	public ResponseEntity<?> getAllSubjectsAndTeachersForHeadmaster() {
		try {
			List<TeacherSubjectDtoResponse> subjectsWithTeachers = teacherSubjectService.getAllSubjectsAndTeachersForHeadmaster();
			return ResponseEntity.ok(subjectsWithTeachers);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/{schoolId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllSubjectsAndTeachersAsAdmin(@PathVariable long schoolId) {
		try {
			List<TeacherSubjectDtoResponse> subjectsWithTeachers = teacherSubjectService.getAllSubjectsAndTeachersBySchool(schoolId);
			return ResponseEntity.ok(subjectsWithTeachers);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
