package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.AbsenceService;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final UserAuthenticationService userAuthenticationService;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final ModelMapper mapper;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository, TeacherRepository teacherRepository, SubjectRepository subjectRepository, StudentRepository studentRepository, UserAuthenticationService userAuthenticationService, TeacherSubjectRepository teacherSubjectRepository, ModelMapper mapper) {
        this.absenceRepository = absenceRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.userAuthenticationService = userAuthenticationService;
        this.teacherSubjectRepository = teacherSubjectRepository;
	    this.mapper = mapper;
    }

    @Transactional
    @Override
    public AbsenceDtoResponse createAbsence(AbsenceDtoRequest absenceDto, long studentId, long subjectId) {

        // get the authenticated teacher
        Teacher teacher = (Teacher) userAuthenticationService.getAuthenticatedUser();

        // ensure the teacher has a school associated with them
        if (teacher.getSchool() == null) {
            throw new NoSuchElementException("No school found for the authenticated teacher");
        }

        // create a new absence
        Absence absence = mapper.map(absenceDto, Absence.class);

        // find the student by ID or throw a ResourceNotFoundException
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student with ID " + studentId + " not found"));

        // find the subject by ID or throw a ResourceNotFoundException
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new NoSuchElementException("Subject with ID " + subjectId + " not found"));


        // create absence only by teachers that has qualification for this subject
        boolean isTeacherAssignedProperly = teacherSubjectRepository.existsByTeacherAndSubject(teacher,subject);
        if (isTeacherAssignedProperly) {
            absence.setTeacher(teacher);
        } else {
            throw new IllegalArgumentException();
        }

        absence.setStudent(student);
        absence.setSubject(subject);

        // persist to db
        absenceRepository.save(absence);

        // return dto
        return mapper.map(absence, AbsenceDtoResponse.class);
    }

    // Base method to fetch and map absences
    @Transactional
    @Override
    public Set<AbsenceDtoResponse> getAbsencesForStudent(long studentId) {
        // Fetch student by ID
        Student student = getStudentById(studentId);

        // Fetch all absences for the student
        Set<Absence> absences = student.getAbsences();

        // Map absences to AbsenceDtoResponse
        return absences.stream()
                .map(absence -> mapper.map(absence, AbsenceDtoResponse.class))
                .collect(Collectors.toSet());
    }

    // Base method to fetch a student by ID
    private Student getStudentById(long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student with ID " + studentId + " not found"));
    }

    @Transactional
    @Override
    public Set<AbsenceDtoResponse> showAllAbsencesForStudentAsStudent() {
        // Get authenticated student
        Student authenticatedStudent = (Student) userAuthenticationService.getAuthenticatedUser();

        // Return absences for the authenticated student
        return getAbsencesForStudent(authenticatedStudent.getId());
    }

    @Transactional
    @Override
    public Set<AbsenceDtoResponse> showAllAbsencesForStudentAsParent(long studentId) {
        Parent parent = (Parent) userAuthenticationService.getAuthenticatedUser();

        // Get the student and verify parent-child relationship
        Student student = getStudentById(studentId);

        if (!parent.getChildren().contains(student)) {
            throw new AccessDeniedException("You are not allowed to view absences for this student");
        }

        // Return absences for the child
        return getAbsencesForStudent(studentId);
    }

    @Transactional
    @Override
    public Set<AbsenceDtoResponse> showAllAbsencesForStudentAsTeacher(long studentId) {
        Teacher teacher = (Teacher) userAuthenticationService.getAuthenticatedUser();

        // Ensure the teacher is assigned to the school
        if (teacher.getSchool() == null) {
            throw new NoSuchElementException("No school found for the authenticated teacher");
        }

        // Get the student and check if they're in the same school
        Student student = getStudentById(studentId);

        // todo:
        //  check whether the teacher teaches the particular subject

        if (!teacher.getSchool().equals(student.getSchool())) {
            throw new AccessDeniedException("You are not allowed to view absences for this student");
        }

        // Return absences for the student
        return getAbsencesForStudent(studentId);
    }

    @Transactional
    @Override
    public Set<AbsenceDtoResponse> showAllAbsencesForStudentAsHeadmaster(long studentId) {
        Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();

        // Ensure the headmaster has a school
        if (headmaster.getSchool() == null) {
            throw new NoSuchElementException("No school found for the authenticated headmaster");
        }

        // Get the student and check if they're in the headmaster's school
        Student student = getStudentById(studentId);

        if (!headmaster.getSchool().equals(student.getSchool())) {
            throw new AccessDeniedException("You are not allowed to view absences for this student");
        }

        // Return absences for the student
        return getAbsencesForStudent(studentId);
    }


    @Transactional
    @Override
    public void excuseAbsence(long absenceId) {
        // check whether this absence exists
        if (absenceRepository.findById(absenceId).isEmpty()) {
            throw new NoSuchElementException("No absence with ID " + absenceId + " was found");
        }

        Absence absence = absenceRepository.findById(absenceId).get();
        absence.setExcused(true);
    }
}
