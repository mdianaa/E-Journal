package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.response.*;
import org.example.ejournal.entities.*;
import org.example.ejournal.entities.TeacherSubject;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.TeacherService;
import org.example.ejournal.services.TeacherSubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherSubjectServiceImpl implements TeacherSubjectService {
	private final TeacherRepository teacherRepository;
	private final SubjectRepository subjectRepository;
	private final TeacherSubjectRepository teacherSubjectRepository;
	private final UserAuthenticationServiceImpl userAuthenticationService;
	private final ModelMapper mapper;
	private final SchoolRepository schoolRepository;
	
	public TeacherSubjectServiceImpl(TeacherService teacherService, TeacherRepository teacherRepository, SchoolRepository schoolRepository, SubjectRepository subjectRepository, AbsenceRepository absenceRepository, SchoolClassRepository schoolClassRepository, ScheduleRepository scheduleRepository, UserAuthenticationRepository userAuthenticationRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, TeacherSubjectRepository teacherSubjectRepository, UserAuthenticationServiceImpl userAuthenticationService, ModelMapper modelMapper, AcademicYearRepository schoolRepository1, SchoolRepository schoolRepository2) {
		this.teacherRepository = teacherRepository;
		this.subjectRepository = subjectRepository;
		this.teacherSubjectRepository = teacherSubjectRepository;
		this.userAuthenticationService = userAuthenticationService;
		this.mapper = modelMapper;
		this.schoolRepository = schoolRepository2;
	}
	
	@Transactional
	@Override
	public TeacherSubjectDtoResponse assignTeacherToSubject(long teacherId, long subjectId) {
		// Retrieve the authenticated user
		User authenticatedUser = userAuthenticationService.getAuthenticatedUser();
		
		// Fetch the teacher by ID
		Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new NoSuchElementException("Teacher not found with id: " + teacherId));
		
		// Fetch the subject by ID
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new NoSuchElementException("Subject not found with id: " + subjectId));
		
		// Check if the user is either an ADMIN or HEADMASTER and if the pair is from the same school
		RoleType userRole = authenticatedUser.getUserAuthentication().getRole();
		if (userRole.equals(RoleType.HEADMASTER)) {
			// Ensure that the headmaster, teacher, and subject are from the same school
			Headmaster headmaster = (Headmaster) authenticatedUser;
			School headmasterSchool = headmaster.getSchool();
			
			if (headmasterSchool == null) {
				throw new IllegalArgumentException("Headmaster does not have an associated school.");
			}
			
			// Check if the teacher and subject are from the same school as the headmaster
			if (teacher.getSchool().getId() != (headmasterSchool.getId())) {
				throw new IllegalArgumentException("The teacher does not belong to the headmaster's school.");
			}
			
			if (subject.getSchool().getId() != (headmasterSchool.getId())) {
				throw new IllegalArgumentException("The subject does not belong to the headmaster's school.");
			}
		} else if (!userRole.equals(RoleType.ADMIN)) {
			throw new SecurityException("Only Admins or Headmasters can assign teachers to subjects.");
		} else if (subject.getSchool().getId() != teacher.getSchool().getId()) {
			throw new IllegalArgumentException("The subject and teacher are not from the same school");
		}
		
		// Check if the Teacher is already assigned to the Subject
		Optional<TeacherSubject> existingTeacherSubject = teacherSubjectRepository.findByTeacherAndSubject(teacher, subject);
		if (existingTeacherSubject.isPresent()) {
			throw new IllegalArgumentException("This teacher is already assigned to the subject.");
		}
		
		// Create a new TeacherSubject entity and map the teacher and subject
		TeacherSubject teacherSubject = new TeacherSubject();
		teacherSubject.setActive(true);  // Assuming the position is active by default
		teacherSubject.setTeacher(teacher);
		teacherSubject.setSubject(subject);
		
		// Save the TeacherSubject entity
		TeacherSubject savedTeacherSubject = teacherSubjectRepository.save(teacherSubject);
		
		// Return the saved TeacherSubject entity as a DTO
		return mapper.map(savedTeacherSubject, TeacherSubjectDtoResponse.class);
	}
	
	@Transactional
	@Override
	public void assignMultipleTeachersToMultipleSubjects(Set<Long> teacherIds, Set<Long> subjectIds) {
		for (Long teacherId : teacherIds) {
			for (Long subjectId : subjectIds) {
				try {
					// If any assignment fails, throw an exception to trigger rollback
					assignTeacherToSubject(teacherId, subjectId);
				} catch (Exception e) {
					// Log the specific failure and rethrow the exception
					throw new RuntimeException("Failed to assign teacherId: " + teacherId + " to subjectId: " + subjectId, e);
				}
			}
		}
	}
	
	@Transactional
	@Override
	public List<TeacherSubjectDtoResponse> getAllSubjectsAndTeachersBySchool(long schoolId) {
		// Fetch the school by ID
		School school = schoolRepository.findById(schoolId)
				.orElseThrow(() -> new NoSuchElementException("School not found with id: " + schoolId));
		
		// Fetch all TeacherSubject relations for subjects in the school
		List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findAllBySchool(schoolId);
		
		// Stream through teacherSubjects and group by subjects, collecting corresponding teachers
		return teacherSubjects.stream()
				.map(teacherSubject -> mapper.map(teacherSubject, TeacherSubjectDtoResponse.class)) // Map each TeacherSubject to TeacherSubjectDtoResponse
				.collect(Collectors.toList()); // Collect the results into a List
	}
	
	// Method for Admin: Fetch subjects and teachers by schoolId
//	@Transactional
//	@Override
//	public List<TeacherSubjectDtoResponse> getAllSubjectsAndTeachersBySchool(long schoolId) {
//		// Fetch the school by ID
//		School school = schoolRepository.findById(schoolId)
//				.orElseThrow(() -> new NoSuchElementException("School not found with id: " + schoolId));
//
//		// Fetch all TeacherSubject relations for the school's subjects
//		List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findAllBySchool(schoolId);
//
//		// Map each TeacherSubject entity to TeacherSubjectDtoResponse
//		return teacherSubjects.stream().map(teacherSubject -> {
//			TeacherSubjectDtoResponse dto = new TeacherSubjectDtoResponse();
//			dto.setTeacherId(teacherSubject.getTeacher().getId());
//			dto.setTeacherSubjectId(teacherSubject.getId());
//			dto.setTeacherFirstName(teacherSubject.getTeacher().getFirstName());
//			dto.setTeacherLastName(teacherSubject.getTeacher().getLastName());
//			dto.setSubjectId(teacherSubject.getSubject().getId());
//			dto.setSubjectName(teacherSubject.getSubject().getName());
//			return dto;
//		}).collect(Collectors.toList());
//	}
//
	
	// Method for Headmaster: Fetch subjects and teachers for the headmaster's school
	@Transactional
	@Override
	public List<TeacherSubjectDtoResponse> getAllSubjectsAndTeachersForHeadmaster() {
		// Get the authenticated headmaster
		Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
		
		// Get the headmaster's school and call the admin method
		return getAllSubjectsAndTeachersBySchool(headmaster.getSchool().getId());
	}
}
	/*@Transactional
	@Override
	public TeacherSubject assignTeacherToSubjectClass(Long teacherId, Long subjectId) {
		Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new NoSuchElementException("No teacher with id '" + teacherId + "' was found."));
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new NoSuchElementException("No subject with id '" + subjectId + "' was found."));

		TeacherSubject teacherSubject = new TeacherSubject();
		teacherSubject.setTeacher(teacher);
		teacherSubject.setSubject(subject);

		return teacherSubjectRepository.save(teacherSubject);
	}
*/

	
	//todo
	// finish this service class


//
//	@Override
//	public TeacherDtoResponse editTeacher(long teacherId, TeacherDtoRequest teacherDto) {
//		if (teacherRepository.findById(teacherId).isPresent()) {
//			Teacher teacher = teacherRepository.findById(teacherId).get();
//
//			mapper.map(teacherDto, teacher);
//
//			// persist to db
//			teacherRepository.save(teacher);
//
//			// return dto
//			return mapper.map(teacher, TeacherDtoResponse.class);
//		}
//
//		return null;
//	}
//
//	@Override
//	public TeacherDtoResponse changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos) {
//		if (teacherRepository.findById(teacherId).isPresent()) {
//			Teacher teacher = teacherRepository.findById(teacherId).get();
//
//			Set<Subject> subjects = subjectDtos.stream().map(s -> mapper.map(s, Subject.class)).collect(Collectors.toSet());
//
//			teacher.setSubjects(subjects);
//
//			// persist to db
//			teacherRepository.save(teacher);
//
//			// return dto
//			return mapper.map(teacher, TeacherDtoResponse.class);
//		}
//
//		return null;
//	}
//
//	@Override
//	public TeacherDtoResponse removeHeadTeacherTitle(long teacherId) {
//		if (teacherRepository.findById(teacherId).isPresent()) {
//			Teacher teacher = teacherRepository.findById(teacherId).get();
//
//			teacher.setHeadTeacher(false);
//
//			// persist to db
//			teacherRepository.save(teacher);
//
//			// return dto
//			return mapper.map(teacher, TeacherDtoResponse.class);
//		}
//
//		return null;
//	}
//
//	@Transactional
//	@Override
//	public TeacherDtoResponse viewTeacher(long teacherId) {
//		Teacher teacher = teacherRepository.findById(teacherId).get();
//
//		Hibernate.initialize(teacher.getSubjects());
//
//		return mapper.map(teacher, TeacherDtoResponse.class);
//	}
//
//	@Override
//	public List<ScheduleDtoResponse> viewScheduleForDay(String day, String semester, String schoolClass) {
//		WeekDay weekDay = WeekDay.valueOf(day.toUpperCase());
//		SemesterType semesterType = SemesterType.valueOf(semester.toUpperCase());
//		SchoolClass schoolClassEntity = schoolClassRepository.findByClassName(schoolClass).get();
//
//		return scheduleRepository.findScheduleForDayAndClassAndSemester(weekDay, schoolClassEntity, semesterType);
//	}
//
//	@Transactional
//	@Override
//	public Set<TeacherDtoResponse> viewAllTeachersInSchool(long schoolId) {
//		School school = schoolRepository.findById(schoolId).get();
//
//		Set<Teacher> teachers = school.getTeachers();
//		Set<TeacherDtoResponse> teachersDto = new HashSet<>();
//
//		for (Teacher teacher : teachers) {
//
//			teachersDto.add(mapper.map(teacher, TeacherDtoResponse.class));
//		}
//
//		return teachersDto;
//	}
//
//	@Override
//	public void deleteTeacher(long teacherId) {
//		if (teacherRepository.findById(teacherId).isPresent()) {
//			Teacher teacher = teacherRepository.findById(teacherId).get();
//
//			teacher.setSubjects(null);
//			teacher.setSchool(null);
//			teacher.setStudents(null);
//
//			List<Absence> absences = absenceRepository.findAllByTeacher(teacher);
//			for (Absence absence : absences) {
//				absence.setTeacher(null);
//				absenceRepository.save(absence);
//			}
//
//			teacherRepository.delete(teacher);
//		}
//	}

