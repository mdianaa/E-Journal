package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.WeekDay;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.TeacherService;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    

    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final AbsenceRepository absenceRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    private final UserAuthenticationService userAuthenticationService;
    public TeacherServiceImpl(TeacherRepository teacherRepository, SchoolRepository schoolRepository, SubjectRepository subjectRepository, AbsenceRepository absenceRepository, SchoolClassRepository schoolClassRepository, ScheduleRepository scheduleRepository, UserAuthenticationRepository userAuthenticationRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, UserAuthenticationService userAuthenticationService) {
        this.teacherRepository = teacherRepository;
        this.schoolRepository = schoolRepository;
        this.subjectRepository = subjectRepository;
        this.absenceRepository = absenceRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.scheduleRepository = scheduleRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
	    this.userAuthenticationService = userAuthenticationService;
    }
    
    @Transactional
    @Override
    public TeacherDtoResponse createTeacher(AdminRegisterDtoRequest registerDtoRequest) {
        // Set the role to TEACHER
        registerDtoRequest.setRole(RoleType.TEACHER);
        
        // Register user credentials via the UserAuthentication service
        UserAuthentication userAuthentication = userAuthenticationService.register(registerDtoRequest);
        
        // Find the school by name
        School school = schoolRepository.findByName(registerDtoRequest.getSchool())
                .orElseThrow(() -> new NoSuchElementException("School was not found."));
        
        // Create Teacher entity and map the inherited User fields
        Teacher teacher = new Teacher();
        teacher.setFirstName(registerDtoRequest.getFirstName()); // inherited from User
        teacher.setLastName(registerDtoRequest.getLastName()); // inherited from User
        teacher.setPhoneNumber(registerDtoRequest.getPhoneNumber()); // inherited from User
        
        // Set school and link UserAuthentication to the teacher
        teacher.setSchool(school);
        teacher.setUserAuthentication(userAuthentication);
        
        // Persist Teacher entity to the database
        Teacher resultTeacher = teacherRepository.save(teacher);
        
        // Return a TeacherDtoResponse object using the mapper
        return mapper.map(resultTeacher, TeacherDtoResponse.class);
    }

    @Override
    public TeacherDtoResponse editTeacher(long teacherId, TeacherDtoRequest teacherDto) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            Teacher teacher = teacherRepository.findById(teacherId).get();

            mapper.map(teacherDto, teacher);

            // persist to db
            teacherRepository.save(teacher);

            // return dto
            return mapper.map(teacher, TeacherDtoResponse.class);
        }

        return null;
    }

    //todo
//    @Override
//    public TeacherDtoResponse changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos) {
//        if (teacherRepository.findById(teacherId).isPresent()) {
//            Teacher teacher = teacherRepository.findById(teacherId).get();
//
//            Set<Subject> subjects = subjectDtos.stream().map(s -> mapper.map(s, Subject.class)).collect(Collectors.toSet());
//
//            teacher.setSubjects(subjects);
//
//            // persist to db
//            teacherRepository.save(teacher);
//
//            // return dto
//            return mapper.map(teacher, TeacherDtoResponse.class);
//        }
//
//        return null;
//    }

    @Override
    public TeacherDtoResponse removeHeadTeacherTitle(long teacherId) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            Teacher teacher = teacherRepository.findById(teacherId).get();

            teacher.setHeadTeacher(false);

            // persist to db
            teacherRepository.save(teacher);

            // return dto
            return mapper.map(teacher, TeacherDtoResponse.class);
        }

        return null;
    }

    @Transactional
    @Override
    public TeacherDtoResponse viewTeacher(long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).get();
        
        return mapper.map(teacher, TeacherDtoResponse.class);
    }
    
    @Override
    public List<TeacherDtoResponse> viewHeadTeachers(long schoolId){
         List<Teacher> headTeachers = teacherRepository.findByHeadTeacherTrue();
         List<TeacherDtoResponse> teacherDtoResponse = new ArrayList<>();
         for(Teacher headTeacher : headTeachers){
             teacherDtoResponse.add(mapper.map(headTeacher, TeacherDtoResponse.class));
         }
         return teacherDtoResponse;
    }
    @Override
    public List<ScheduleDtoResponse> viewScheduleForDay(String day, String semester, String schoolClass) {
        WeekDay weekDay = WeekDay.valueOf(day.toUpperCase());
        SemesterType semesterType = SemesterType.valueOf(semester.toUpperCase());
        SchoolClass schoolClassEntity = schoolClassRepository.findByClassName(schoolClass).get();

        return scheduleRepository.findScheduleForDayAndClassAndSemester(weekDay, schoolClassEntity, semesterType);
    }

    @Transactional
    @Override
    public Set<TeacherDtoResponse> viewAllTeachersInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        Set<Teacher> teachers = school.getTeachers();
        Set<TeacherDtoResponse> teachersDto = new HashSet<>();

        for (Teacher teacher : teachers) {

            teachersDto.add(mapper.map(teacher, TeacherDtoResponse.class));
        }

        return teachersDto;
    }

    @Override
    public void deleteTeacher(long teacherId) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            Teacher teacher = teacherRepository.findById(teacherId).get();

            teacher.setSchool(null);

            List<Absence> absences = absenceRepository.findAllByTeacher(teacher);
            for (Absence absence : absences) {
                absence.setTeacher(null);
                absenceRepository.save(absence);
            }

            teacherRepository.delete(teacher);
        }
    }
}
