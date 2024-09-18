package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.BadNoteDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.BadNoteRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.BadNoteService;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BadNoteServiceImpl implements BadNoteService {

    private final BadNoteRepository badNoteRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final UserAuthenticationService userAuthenticationService;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public BadNoteServiceImpl(BadNoteRepository badNoteRepository, TeacherRepository teacherRepository, SubjectRepository subjectRepository, UserAuthenticationService userAuthenticationService, StudentRepository studentRepository, ModelMapper mapper) {
        this.badNoteRepository = badNoteRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.userAuthenticationService = userAuthenticationService;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public BadNoteDtoResponse createBadNote(BadNoteDtoRequest badNoteDtoRequest, long studentId) {

        // get the authenticated teacher
        Teacher teacher = (Teacher) userAuthenticationService.getAuthenticatedUser();

        // ensure the teacher has a school associated with them
        if (teacher.getSchool() == null) {
            throw new NoSuchElementException("No school found for the authenticated teacher");
        }

        // create a new bad note
        BadNote badNote = mapper.map(badNoteDtoRequest, BadNote.class);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student with ID " + studentId + " not found"));


        badNote.setTeacher(teacher);
        badNote.setStudent(student);

        // persist to db
        badNoteRepository.save(badNote);

        // return dto
        return mapper.map(badNote, BadNoteDtoResponse.class);
    }

    // Base method to fetch and map bad notes
    @Transactional
    @Override
    public List<BadNoteDtoResponse> getBadNotesForStudent(long studentId) {
        // Fetch student by ID
        Student student = getStudentById(studentId);

        // Fetch all bad notes for the student
        List<BadNote> badNotes = badNoteRepository.findAllByStudent(student);

        // Map bad notes to BadNoteDtoResponse
        return badNotes.stream()
                .map(badNote -> mapper.map(badNote, BadNoteDtoResponse.class))
                .collect(Collectors.toList());
    }

    // Base method to fetch a student by ID
    private Student getStudentById(long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student with ID " + studentId + " not found"));
    }

    @Transactional
    @Override
    public List<BadNoteDtoResponse> showAllBadNotesForStudentAsStudent() {
        // Get authenticated student
        Student authenticatedStudent = (Student) userAuthenticationService.getAuthenticatedUser();

        // Return bad notes for the authenticated student
        return getBadNotesForStudent(authenticatedStudent.getId());
    }

    @Transactional
    @Override
    public List<BadNoteDtoResponse> showAllBadNotesForStudentAsParent(long studentId) {
        Parent parent = (Parent) userAuthenticationService.getAuthenticatedUser();

        // Get the student and verify parent-child relationship
        Student student = getStudentById(studentId);

        if (!parent.getChildren().contains(student)) {
            throw new AccessDeniedException("You are not allowed to view bad notes for this student");
        }

        // Return bad notes for the child
        return getBadNotesForStudent(studentId);
    }

    @Transactional
    @Override
    public List<BadNoteDtoResponse> showAllBadNotesForStudentAsTeacher(long studentId) {
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
            throw new AccessDeniedException("You are not allowed to view bad notes for this student");
        }

        // Return bad notes for the student
        return getBadNotesForStudent(studentId);
    }

    @Transactional
    @Override
    public List<BadNoteDtoResponse> showAllBadNotesForStudentAsHeadmaster(long studentId) {
        Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();

        // Ensure the headmaster has a school
        if (headmaster.getSchool() == null) {
            throw new NoSuchElementException("No school found for the authenticated headmaster");
        }

        // Get the student and check if they're in the headmaster's school
        Student student = getStudentById(studentId);

        if (!headmaster.getSchool().equals(student.getSchool())) {
            throw new AccessDeniedException("You are not allowed to view bad notes for this student");
        }

        // Return bad notes for the student
        return getBadNotesForStudent(studentId);
    }


    @Override
    public void deleteBadNote(long badNoteId) {
        if (badNoteRepository.findById(badNoteId).isPresent()) {
            BadNote badNote = badNoteRepository.findById(badNoteId).get();

            badNote.setTeacher(null);
            badNote.setStudent(null);

            badNoteRepository.delete(badNote);
        }
    }
}
