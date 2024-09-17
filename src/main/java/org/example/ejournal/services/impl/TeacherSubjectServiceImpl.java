package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.TeacherService;
import org.example.ejournal.services.TeacherSubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class TeacherSubjectServiceImpl implements TeacherSubjectService {
	private final TeacherService teacherService;
	private final TeacherRepository teacherRepository;
	private final SchoolRepository schoolRepository;
	private final SubjectRepository subjectRepository;
	private final AbsenceRepository absenceRepository;
	private final SchoolClassRepository schoolClassRepository;
	private final ScheduleRepository scheduleRepository;
	private final UserAuthenticationRepository userAuthenticationRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper mapper;
	private final TeacherSubjectRepository teacherSubjectRepository;
	
	public TeacherSubjectServiceImpl(TeacherService teacherService, TeacherRepository teacherRepository, SchoolRepository schoolRepository, SubjectRepository subjectRepository, AbsenceRepository absenceRepository, SchoolClassRepository schoolClassRepository, ScheduleRepository scheduleRepository, UserAuthenticationRepository userAuthenticationRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, TeacherSubjectRepository teacherSubjectRepository) {
		this.teacherService = teacherService;
		this.teacherRepository = teacherRepository;
		this.schoolRepository = schoolRepository;
		this.subjectRepository = subjectRepository;
		this.absenceRepository = absenceRepository;
		this.schoolClassRepository = schoolClassRepository;
		this.scheduleRepository = scheduleRepository;
		this.userAuthenticationRepository = userAuthenticationRepository;
		this.passwordEncoder = passwordEncoder;
		this.mapper = mapper;
		this.teacherSubjectRepository = teacherSubjectRepository;
	}
	
	@Transactional
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
	@Transactional
	@Override
	public Set<TeacherSubject> assignMultipleTeacherToSubjectClass(Set<Long> teacherIds, Long subjectId) {
		Set<TeacherSubject> teacherSubjectSet = new HashSet<>();
		
		for (Long teacherId : teacherIds) {
			// Assign each teacher to the subject and collect the results
			TeacherSubject teacherSubject = assignTeacherToSubjectClass(teacherId, subjectId);
			teacherSubjectSet.add(teacherSubject);
		}
		
		return teacherSubjectSet; // Return the set of assigned teacher-subject mappings
	}
	
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
}
