package org.example.ejournal.services.impl;

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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BadNoteServiceImpl implements BadNoteService {

    private final BadNoteRepository badNoteRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public BadNoteServiceImpl(BadNoteRepository badNoteRepository, TeacherRepository teacherRepository, SubjectRepository subjectRepository, StudentRepository studentRepository, ModelMapper mapper) {
        this.badNoteRepository = badNoteRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Override
    public BadNoteDtoResponse createBadNote(BadNoteDtoRequest badNoteDtoRequest, TeacherDtoRequest teacherDto, StudentDtoRequest studentDto) {
        // create a new bad note
        BadNote badNote = mapper.map(badNoteDtoRequest, BadNote.class);
        Teacher teacher = teacherRepository.findByFirstNameAndLastName(teacherDto.getFirstName(), teacherDto.getLastName()).get();
        Student student = studentRepository.findByFirstNameAndLastName(studentDto.getFirstName(), studentDto.getLastName()).get();

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
