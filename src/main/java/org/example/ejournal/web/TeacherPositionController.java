package org.example.ejournal.web;


import org.example.ejournal.dtos.request.TeacherPositionDtoRequest;
import org.example.ejournal.dtos.response.TeacherPositionDtoResponse;
import org.example.ejournal.services.TeacherPositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@ControllerAdvice
@RequestMapping("/teacher-position")
public class TeacherPositionController {
	private final TeacherPositionService teacherPositionService;
	
	public TeacherPositionController(TeacherPositionService teacherPositionService) {
		this.teacherPositionService = teacherPositionService;
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasAnyRole('HEADMASTER')")
	public ResponseEntity<?> assignMultipleTeachersSubjectsToMultipleClasses(@RequestBody TeacherPositionDtoRequest teacherPositionDtoRequest){
		try {
			List<TeacherPositionDtoResponse> subjectsWithTeachers = teacherPositionService
					.assignMultipleTeacherSubjectsToClasses(teacherPositionDtoRequest.getTeacherSubjectIds(),teacherPositionDtoRequest.getSchoolClassIds());
			return ResponseEntity.ok(subjectsWithTeachers);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/school-class/{schoolClassId}")
	@PreAuthorize("hasAnyRole('HEADMASTER', 'ADMIN')")
	public ResponseEntity<?> getAllTeacherPositionsBySchoolClass(@PathVariable long schoolClassId) {
		try {
			List<TeacherPositionDtoResponse> result = teacherPositionService.getAllTeacherPositionsBySchoolClass(schoolClassId);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
