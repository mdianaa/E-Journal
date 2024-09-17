package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.*;
import org.example.ejournal.entities.*;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.StudentService;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private final UserAuthenticationService userAuthenticationService;
    
    public StudentServiceImpl(StudentRepository studentRepository, SchoolRepository schoolRepository, SchoolClassRepository schoolClassRepository, SubjectRepository subjectRepository, ParentRepository parentRepository, AbsenceRepository absenceRepository, BadNoteRepository badNoteRepository, ScheduleRepository scheduleRepository, UserAuthenticationRepository userAuthenticationRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, UserAuthenticationService userAuthenticationService) {
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
	    this.userAuthenticationService = userAuthenticationService;
    }
    
    @Transactional
    @Override
    public StudentDtoResponse createStudent(StudentDtoRequest studentDtoRequest) {
        // Fetch the school class by its ID (provided in the request)
        SchoolClass schoolClass = schoolClassRepository.findById(studentDtoRequest.getSchoolClassId())
                .orElseThrow(() -> new NoSuchElementException("School class not found with id: " + studentDtoRequest.getSchoolClassId()));
        
        // Retrieve the currently authenticated headmaster
        Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
        School headmasterSchool = headmaster.getSchool();
        
        // Check if the class belongs to the same school as the headmaster
        if (schoolClass.getSchool().getId() != (headmasterSchool.getId())) {
            throw new IllegalArgumentException("The headmaster does not have permission to create students for this class.");
        }
        
        // Set the role to STUDENT
        studentDtoRequest.getUserRegisterDtoRequest().setRole(RoleType.STUDENT);
        
        // Register user credentials via the UserAuthentication service
        UserAuthentication userAuthentication = userAuthenticationService.register(studentDtoRequest.getUserRegisterDtoRequest());
        
        // Create Student entity and map the inherited User fields
        Student student = new Student();
        student.setFirstName(studentDtoRequest.getFirstName()); // inherited from User
        student.setLastName(studentDtoRequest.getLastName()); // inherited from User
        student.setPhoneNumber(studentDtoRequest.getPhoneNumber()); // inherited from User
        student.setAddress(studentDtoRequest.getAddress()); // specific to Student
        
        // Set school and school class, and link UserAuthentication to the student
        student.setSchool(headmasterSchool);
        student.setCurrentSchoolClass(schoolClass);
        student.setUserAuthentication(userAuthentication);
        
        // Persist Student entity to the database
        Student resultStudent = studentRepository.save(student);
        
        // Return a StudentDtoResponse object using the mapper
        return mapper.map(resultStudent, StudentDtoResponse.class);
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
    public Set<StudentDtoResponse> showAllStudentsInSchoolAsHeadmaster() {
        Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
        
        if (headmaster.getSchool() == null) {
            throw new NoSuchElementException("Headmaster does not have an assigned school.");
        }
        
        // Handle potential null if getStudents() returns null
        Set<Student> students = headmaster.getSchool().getStudents();
        if (students == null) {
            return Collections.emptySet();
        }
        
        // Reuse the private method for mapping students to DTOs
        return getStudentDtoResponses(students);
    }
    
    @Transactional
    @Override
    public Set<StudentDtoResponse> showAllStudentsInSchool(long schoolId) {
        // Fetch the school by its ID
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new NoSuchElementException("No school found with id: " + schoolId));
        
        // Handle potential null if getStudents() returns null
        Set<Student> students = school.getStudents();
        if (students == null) {
            return Collections.emptySet();
        }
        
        // Reuse the private method for mapping students to DTOs
        return getStudentDtoResponses(students);
    }
    
    @Transactional
    @Override
    public Set<StudentDtoResponse> showAllStudentsInClass(long schoolClassId) {
        // Fetch the school class by its ID
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId)
                .orElseThrow(() -> new NoSuchElementException("No class found with id: " + schoolClassId));
        
        // Handle potential null if getStudents() returns null
        Set<Student> students = schoolClass.getStudents();
        if (students == null) {
            return Collections.emptySet();
        }
        
        // Reuse the private method for mapping students to DTOs
        return getStudentDtoResponses(students);
    }
    
    private Set<StudentDtoResponse> getStudentDtoResponses(Set<Student> students) {
        // Use stream to map students to DTO responses
        return students.stream()
                .map(student -> mapper.map(student, StudentDtoResponse.class))
                .collect(Collectors.toSet());
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
