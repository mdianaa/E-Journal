package org.example.ejournal.services.impl;


import org.example.ejournal.dtos.response.TeacherPositionDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.TeacherPositionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherPositionServiceImpl implements TeacherPositionService {
	private final TeacherRepository teacherRepository;
	private final SchoolClassRepository schoolClassRepository;
	private final TeacherSubjectRepository teacherSubjectRepository;
	private final UserAuthenticationServiceImpl userAuthenticationService;
	private final SubjectRepository subjectRepository;
	private final TeacherPositionRepository teacherPositionRepository;
	private final ModelMapper mapper;
	
	public TeacherPositionServiceImpl(TeacherRepository teacherRepository, SchoolClassRepository schoolClassRepository, TeacherSubjectRepository teacherSubjectRepository, UserAuthenticationServiceImpl userAuthenticationService, SubjectRepository subjectRepository, TeacherPositionRepository teacherPositionRepository, ModelMapper mapper) {
		this.teacherRepository = teacherRepository;
		this.schoolClassRepository = schoolClassRepository;
		this.teacherSubjectRepository = teacherSubjectRepository;
		this.userAuthenticationService = userAuthenticationService;
		this.subjectRepository = subjectRepository;
		this.teacherPositionRepository = teacherPositionRepository;
		this.mapper = mapper;
	}
	
	@Transactional
	public TeacherPositionDtoResponse assignTeacherSubjectToClass(Long teacherSubjectId, Long classId) {
		// Fetch the TeacherSubject entity by its ID
		TeacherSubject teacherSubject = teacherSubjectRepository.findById(teacherSubjectId)
				.orElseThrow(() -> new NoSuchElementException("TeacherSubject not found with id: " + teacherSubjectId));
		
		// Fetch the SchoolClass by its ID
		SchoolClass schoolClass = schoolClassRepository.findById(classId)
				.orElseThrow(() -> new NoSuchElementException("Class not found with id: " + classId));
		
		// Ensure the TeacherSubject is not already assigned to the class
		Optional<TeacherPosition> existingPosition = teacherPositionRepository.findByTeacherSubjectAndSchoolClass(teacherSubject, schoolClass);
		if (existingPosition.isPresent()) {
			throw new IllegalArgumentException("This teacher-subject is already assigned to the class.");
		}
		
		// Create and save a new TeacherPosition
		TeacherPosition teacherPosition = new TeacherPosition();
		teacherPosition.setTeacherSubject(teacherSubject);
		teacherPosition.setSchoolClass(schoolClass);
		TeacherPosition savedPosition = teacherPositionRepository.save(teacherPosition);
		
		// Return the saved TeacherPosition as a DTO
		return mapper.map(savedPosition, TeacherPositionDtoResponse.class);
	}
	
	@Transactional
	@Override
	public List<TeacherPositionDtoResponse> assignMultipleTeacherSubjectsToClasses(Set<Long> teacherSubjectIds, Set<Long> classIds) {
		User authenticatedUser = userAuthenticationService.getAuthenticatedUser();
		
		List<TeacherPositionDtoResponse> positionDtoResponses = new ArrayList<>();
		List<String> errorMessages = new ArrayList<>();
		
		for (Long teacherSubjectId : teacherSubjectIds) {
			for (Long classId : classIds) {
				try {
					// Validate permissions for the user
					validatePermissionsForAssigning(authenticatedUser, teacherSubjectId, classId);
					
					// Assign teacher-subject to the class and collect the result DTO
					TeacherPositionDtoResponse response = assignTeacherSubjectToClass(teacherSubjectId, classId);
					positionDtoResponses.add(response);
					
				} catch (IllegalArgumentException e) {
					// Handle specific IllegalArgumentExceptions (e.g., duplicate assignment)
					errorMessages.add("Failed to assign TeacherSubjectId: " + teacherSubjectId + " to classId: " + classId + " - " + e.getMessage());
				} catch (NoSuchElementException e) {
					// Handle specific NoSuchElementExceptions (e.g., missing entities)
					errorMessages.add("Failed to assign TeacherSubjectId: " + teacherSubjectId + " to classId: " + classId + " - " + e.getMessage());
				} catch (Exception e) {
					// Handle any other exceptions
					errorMessages.add("Unexpected error assigning TeacherSubjectId: " + teacherSubjectId + " to classId: " + classId + " - " + e.getMessage());
				}
			}
		}
		
		// Optionally log or return the error messages
		if (!errorMessages.isEmpty()) {
			errorMessages.forEach(System.out::println); // You can replace this with proper logging
			throw new RuntimeException("Some assignments failed: " + String.join(", ", errorMessages));
		}
		
		return positionDtoResponses;  // Return the list of successful assignments
	}
	
	private void validatePermissionsForAssigning(User authenticatedUser, Long teacherSubjectId, Long classId) {
		RoleType userRole = authenticatedUser.getUserAuthentication().getRole();
		
		TeacherSubject teacherSubject = teacherSubjectRepository.findById(teacherSubjectId)
				.orElseThrow(() -> new NoSuchElementException("TeacherSubject not found with id: " + teacherSubjectId));
		
		SchoolClass schoolClass = schoolClassRepository.findById(classId)
				.orElseThrow(() -> new NoSuchElementException("Class not found with id: " + classId));
		
		//check if headmaster, check if admin, if not - break
		if (userRole.equals(RoleType.HEADMASTER)) {
			Headmaster headmaster = (Headmaster) authenticatedUser;
			School headmasterSchool = headmaster.getSchool();
			if (teacherSubject.getTeacher().getSchool().getId() != (headmasterSchool.getId()) ||
					teacherSubject.getSubject().getSchool().getId() != (headmasterSchool.getId()) ||
					schoolClass.getSchool().getId() != (headmasterSchool.getId())) {
				throw new IllegalArgumentException("Teacher, subject, or class does not belong to the headmaster's school.");
			}
		} else if (!userRole.equals(RoleType.ADMIN)) {
			throw new SecurityException("Only Admins or Headmasters can assign TeacherSubject entities to classes.");
		}
	}
	
	@Transactional
	@Override
	public List<TeacherPositionDtoResponse> getAllTeacherPositionsBySchoolClass(long schoolClassId) {
		// Fetch the school class by its ID
		SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId)
				.orElseThrow(() -> new NoSuchElementException("School class not found with id: " + schoolClassId));
		
		// Retrieve the authenticated user
		User authenticatedUser = userAuthenticationService.getAuthenticatedUser();
		RoleType userRole = authenticatedUser.getUserAuthentication().getRole();
		
		// If the user is a HEADMASTER, check if the school matches
		if (userRole.equals(RoleType.HEADMASTER)) {
			Headmaster headmaster = (Headmaster) authenticatedUser;
			School headmasterSchool = headmaster.getSchool();
			
			if (headmasterSchool == null || headmasterSchool.getId() != (schoolClass.getSchool().getId())) {
				throw new IllegalArgumentException("The headmaster does not have permission to view teacher positions for this class as it is not in their school.");
			}
		}
		// If the user is neither a HEADMASTER nor an ADMIN, throw an exception
		else if (!userRole.equals(RoleType.ADMIN)) {
			throw new SecurityException("Only Admins or Headmasters can access teacher positions.");
		}
		
		// Fetch all TeacherPositions for the given school class
		List<TeacherPosition> teacherPositions = teacherPositionRepository.findAllBySchoolClass(schoolClass);
		
		// Map each TeacherPosition entity to TeacherPositionDtoResponse using ModelMapper
		return teacherPositions.stream()
				.map(teacherPosition -> mapper.map(teacherPosition, TeacherPositionDtoResponse.class))
				.collect(Collectors.toList());
	}
}
