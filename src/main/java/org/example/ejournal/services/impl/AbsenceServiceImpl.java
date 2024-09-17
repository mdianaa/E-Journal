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
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

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
