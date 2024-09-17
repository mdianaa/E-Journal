package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.AssignStudentToParentDto;
import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.dtos.response.StudentDtoResponse;
import org.example.ejournal.services.ParentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/parents")
public class ParentController {
	
	private final ParentService parentService;
	
	public ParentController(ParentService parentService) {
		this.parentService = parentService;
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('HEADMASTER')")
	public ResponseEntity<?> createParent(@Valid @RequestBody ParentDtoRequest parentDto) {
		try {
			ParentDtoResponse createdParentDto = parentService.createParent(parentDto);
			
			return new ResponseEntity<>(createdParentDto, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//showAllParentsInClassAsHeadmaster
	
	@GetMapping("/school-class/{schoolClassId}")
	@PreAuthorize("hasRole('HEADMASTER')")
	public ResponseEntity<?> viewAllParentsInClassAsHeadmaster(@PathVariable long schoolClassId) {
		try {
			Set<ParentDtoResponse> parentDtoResponses = parentService.showAllParentsInClassAsHeadmaster(schoolClassId);
			return ResponseEntity.ok(parentDtoResponses);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{parentId}")
	@PreAuthorize("hasAnyRole('HEADMASTER','ADMIN')")
	public ResponseEntity<?> editParent(@PathVariable long parentId, @RequestBody ParentDtoRequest parentDto) {
		try {
			ParentDtoResponse updatedParent = parentService.editParent(parentId, parentDto);
			return ResponseEntity.ok(updatedParent);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{parentId}/children")
	@PreAuthorize("hasAnyRole('ADMIN', 'HEADMASTER')")
	public ResponseEntity<?> viewParentsChildren(@PathVariable long parentId) {
		try {
			Set<StudentDtoResponse> children = parentService.viewParentsChildren(parentId);
			return ResponseEntity.ok(children);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/assign-student")
	@PreAuthorize("hasAnyRole('ADMIN', 'HEADMASTER')")
	public ResponseEntity<?> assignStudentToParent(@RequestBody AssignStudentToParentDto assignStudentToParentDto) {
		try {
			ParentDtoResponse updatedParent = parentService.assignStudentToParent(assignStudentToParentDto.getParentId(), assignStudentToParentDto.getStudentId());
			return ResponseEntity.ok(updatedParent);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	*
	*
	* NOT IMPLEMENTED?
	*
	*
	* */
	@GetMapping("/{parentId}")
	@PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
	public ResponseEntity<ParentDtoResponse> viewParent(@PathVariable long parentId) {
		ParentDtoResponse parent = parentService.viewParent(parentId);
		return parent != null ? ResponseEntity.ok(parent) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/viewAll/{schoolId}")
	@PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
	public ResponseEntity<Set<ParentDtoResponse>> viewAllParentsInSchool(@PathVariable long schoolId) {
		Set<ParentDtoResponse> parents = parentService.viewAllParentsInSchool(schoolId);
		return ResponseEntity.ok(parents);
	}
	
	@DeleteMapping("/delete/{parentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteParent(@PathVariable long parentId) {
		parentService.deleteParent(parentId);
		return ResponseEntity.noContent().build();
	}
}
