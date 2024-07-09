package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.*;
import org.example.ejournal.entities.*;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.WeekDay;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final ParentRepository parentRepository;
    private final AbsenceRepository absenceRepository;
    private final BadNoteRepository badNoteRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public StudentServiceImpl(StudentRepository studentRepository, SchoolRepository schoolRepository, SchoolClassRepository schoolClassRepository, SubjectRepository subjectRepository, ParentRepository parentRepository, AbsenceRepository absenceRepository, BadNoteRepository badNoteRepository, ScheduleRepository scheduleRepository, UserAuthenticationRepository userAuthenticationRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
        this.parentRepository = parentRepository;
        this.absenceRepository = absenceRepository;
        this.badNoteRepository = badNoteRepository;
        this.scheduleRepository = scheduleRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public StudentDtoResponse createStudent(StudentDtoRequest studentDto, SchoolDtoRequest schoolDto, SchoolClassDtoRequest schoolClassDto, ParentDtoRequest parentDto, UserRegisterDtoRequest userRegisterDtoRequest) {
        // check if student exists already
        if (userAuthenticationRepository.findByUsername(userRegisterDtoRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists!");
        }

        // register student
        Student student = mapper.map(studentDto, Student.class);
        School school = schoolRepository.findByName(schoolDto.getName()).get();
        SchoolClass schoolClass = schoolClassRepository.findByClassName(schoolClassDto.getClassName()).get();

        Parent parent = parentRepository.findByFirstNameAndLastName(parentDto.getFirstName(), parentDto.getLastName()).get();

        student.setSchool(school);
        student.setSchoolClass(schoolClass);
        student.setParent(parent);

        // map the user credentials
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUsername(userRegisterDtoRequest.getUsername());
        userAuthentication.setPassword(passwordEncoder.encode(userRegisterDtoRequest.getPassword()));
        userAuthentication.setRole(userRegisterDtoRequest.getRole());

        student.setUserAuthentication(userAuthentication);

        // persist to db
        userAuthenticationRepository.save(userAuthentication);
        studentRepository.save(student);

        // return dto
        return mapper.map(student, StudentDtoResponse.class);
    }

    @Override
    public StudentDtoResponse editStudent(long studentId, StudentDtoRequest studentDto) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();

            mapper.map(studentDto, student);

            return mapper.map(student, StudentDtoResponse.class);
        }

        return null;
    }

    @Override
    public List<GradeDtoResponse> showAllGradesForSubject(String username, SubjectDtoRequest subjectDto) {
        if (studentRepository.findByUserAuthenticationUsername(username).isPresent()){
            Student student = studentRepository.findByUserAuthenticationUsername(username).get();
            Subject subject = subjectRepository.findBySubjectType(subjectDto.getSubjectType()).get();

            List<Grade> grades = student.getGrades().stream().filter(g -> g.getSubject().getSubjectType().equals(subject.getSubjectType())).toList();
            List<GradeDtoResponse> gradesDto = new ArrayList<>();

            for (Grade grade : grades) {
                gradesDto.add(mapper.map(grade, GradeDtoResponse.class));
            }

            return gradesDto;
        }

        return null;
    }

    @Override
    public Set<AbsenceDtoResponse> showAllAbsencesForStudent(String username) {
        if (studentRepository.findByUserAuthenticationUsername(username).isPresent()) {
            Student student = studentRepository.findByUserAuthenticationUsername(username).get();

            Set<Absence> absences = student.getAbsences();
            Set<AbsenceDtoResponse> absencesDto = new HashSet<>();

            for (Absence absence : absences) {
                absencesDto.add(mapper.map(absence, AbsenceDtoResponse.class));
            }

            return absencesDto;
        }

        return null;
    }

    @Override
    public List<BadNoteDtoResponse> showAllBadNotesForStudent(String username) {
        if (studentRepository.findByUserAuthenticationUsername(username).isPresent()) {
            Student student = studentRepository.findByUserAuthenticationUsername(username).get();

            List<BadNote> badNotes = badNoteRepository.findAllByStudent(student);
            List<BadNoteDtoResponse> badNotesDto = new ArrayList<>();

            for (BadNote badNote : badNotes) {
                badNotesDto.add(mapper.map(badNote, BadNoteDtoResponse.class));
            }

            return badNotesDto;
        }

        return null;
    }

    @Override
    public StudentDtoResponse viewStudent(String username) {
        Student student = studentRepository.findByUserAuthenticationUsername(username).get();

        return mapper.map(student, StudentDtoResponse.class);
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
    public Set<StudentDtoResponse> showAllStudentsInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        Set<Student> students = school.getStudents();
        Set<StudentDtoResponse> studentsDto = new HashSet<>();

        for (Student student : students) {
            studentsDto.add(mapper.map(student, StudentDtoResponse.class));
        }

        return studentsDto;
    }

    @Override
    public void withdrawStudent(long studentId) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();

            student.setSchoolClass(null);
            student.setParent(null);
            student.setSchool(null);
            student.setTeachers(null);
            List<Absence> absences = absenceRepository.findAllByStudent(student);
            absenceRepository.deleteAll(absences);

            student.setAbsences(null);
            student.setGrades(null);

            studentRepository.delete(student);
        }
    }
}
