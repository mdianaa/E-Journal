package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.WeekDay;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.TeacherService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public TeacherServiceImpl(TeacherRepository teacherRepository, SchoolRepository schoolRepository, SubjectRepository subjectRepository, AbsenceRepository absenceRepository, SchoolClassRepository schoolClassRepository, ScheduleRepository scheduleRepository, UserAuthenticationRepository userAuthenticationRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.teacherRepository = teacherRepository;
        this.schoolRepository = schoolRepository;
        this.subjectRepository = subjectRepository;
        this.absenceRepository = absenceRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.scheduleRepository = scheduleRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public TeacherDtoResponse createTeacher(TeacherDtoRequest teacherDto, SchoolDtoRequest schoolDto, Set<SubjectDtoRequest> subjectDtos, UserRegisterDtoRequest userRegisterDtoRequest) {
        // check if this teacher exists already
        if (userAuthenticationRepository.findByUsername(userRegisterDtoRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists!");
        }

        // register teacher
        Teacher teacher = mapper.map(teacherDto, Teacher.class);
        School school = schoolRepository.findByName(schoolDto.getName()).get();

        Set<Subject> subjects = subjectDtos.stream().map(s -> subjectRepository.findBySubjectType(s.getSubjectType()).get()).collect(Collectors.toSet());

        teacher.setSubjects(subjects);
        teacher.setSchool(school);

        // map the user credentials
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUsername(userRegisterDtoRequest.getUsername());
        userAuthentication.setPassword(passwordEncoder.encode(userRegisterDtoRequest.getPassword()));
        userAuthentication.setRole(userRegisterDtoRequest.getRole());

        // persist to db
        userAuthenticationRepository.save(userAuthentication);
        teacherRepository.save(teacher);

        // return dto
        return mapper.map(teacher, TeacherDtoResponse.class);
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

    @Override
    public TeacherDtoResponse changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            Teacher teacher = teacherRepository.findById(teacherId).get();

            Set<Subject> subjects = subjectDtos.stream().map(s -> mapper.map(s, Subject.class)).collect(Collectors.toSet());

            teacher.setSubjects(subjects);

            // persist to db
            teacherRepository.save(teacher);

            // return dto
            return mapper.map(teacher, TeacherDtoResponse.class);
        }

        return null;
    }

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

        Hibernate.initialize(teacher.getSubjects());

        return mapper.map(teacher, TeacherDtoResponse.class);
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

            teacher.setSubjects(null);
            teacher.setSchool(null);
            teacher.setStudents(null);

            List<Absence> absences = absenceRepository.findAllByTeacher(teacher);
            for (Absence absence : absences) {
                absence.setTeacher(null);
                absenceRepository.save(absence);
            }

            teacherRepository.delete(teacher);
        }
    }
}
