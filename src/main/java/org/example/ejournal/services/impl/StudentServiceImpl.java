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

import java.util.*;

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
        //todo
        // dto update?
        //SchoolClass schoolClass = schoolClassRepository.findById(schoolClassDto.getClassId()).get();

        Parent parent = parentRepository.findByFirstNameAndLastName(parentDto.getFirstName(), parentDto.getLastName()).get();

        student.setSchool(school);
        //student.setCurrentSchoolClass(schoolClass);
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
            Subject subject = subjectRepository.findByName(subjectDto.getName()).get();

            List<Grade> grades = student.getGrades().stream().filter(g -> g.getSubject().getName().equals(subject.getName())).toList();
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
//todo
//    @Override
//    public List<ScheduleDtoResponse> viewScheduleForDay(String day, String semester, String schoolClass) {
//        WeekDay weekDay = WeekDay.valueOf(day.toUpperCase());
//        SemesterType semesterType = SemesterType.valueOf(semester.toUpperCase());
//        SchoolClass schoolClassEntity = schoolClassRepository.findByGradeLevelAndClassSection(schoolClass).get();
//
//        return scheduleRepository.findScheduleForDayAndClassAndSemester(weekDay, schoolClassEntity, semesterType);
//    }

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

    @Transactional
    @Override
    public Set<StudentDtoResponse> showAllStudentsInClass(long schoolClassId){
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId)
                .orElseThrow(()-> new NoSuchElementException("No such class was found with id "+schoolClassId));
        
        Set<Student> students = schoolClass.getStudents();
        Set<StudentDtoResponse> studentsDto = new HashSet<>();
        
        for(Student student:students){
            studentsDto.add(mapper.map(student,StudentDtoResponse.class));
        }
        return studentsDto;
    }
    //todo
    // boilerplate code above
    @Override
    public void withdrawStudent(long studentId) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();

            student.setCurrentSchoolClass(null);
            student.setParent(null);
            student.setSchool(null);
            //student.setTeachers(null);
            List<Absence> absences = absenceRepository.findAllByStudent(student);
            absenceRepository.deleteAll(absences);

            student.setAbsences(null);
            student.setGrades(null);

            studentRepository.delete(student);
        }
    }
}
