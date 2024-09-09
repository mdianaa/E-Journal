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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final ModelMapper mapper;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository, TeacherRepository teacherRepository, SubjectRepository subjectRepository, StudentRepository studentRepository, TeacherSubjectRepository teacherSubjectRepository, ModelMapper mapper) {
        this.absenceRepository = absenceRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
	    this.teacherSubjectRepository = teacherSubjectRepository;
	    this.mapper = mapper;
    }

    @Transactional
    @Override
    public AbsenceDtoResponse createAbsence(AbsenceDtoRequest absenceDto, TeacherDtoRequest teacherDto, StudentDtoRequest studentDto, SubjectDtoRequest subjectDto) {
        // check whether this absence already exists

        // create a new absence
        Absence absence = mapper.map(absenceDto, Absence.class);
        Teacher teacher = teacherRepository.findByFirstNameAndLastName(teacherDto.getFirstName(), teacherDto.getLastName()).get();
        Student student = studentRepository.findByFirstNameAndLastName(studentDto.getFirstName(), studentDto.getLastName()).get();
        Subject subject = subjectRepository.findByName(subjectDto.getName()).get();

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

    @Override
    public void excuseAbsence(long absenceId) {
        // check whether this absence exists
        if (absenceRepository.findById(absenceId).isPresent()) {
            Absence absence = absenceRepository.findById(absenceId).get();

            absence.setExcused(true);
        }
    }
}
