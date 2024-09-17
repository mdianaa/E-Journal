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
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

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
