package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.models.Absence;
import org.example.ejournal.models.Student;
import org.example.ejournal.models.Subject;
import org.example.ejournal.models.Teacher;
import org.example.ejournal.repositories.AbsenceRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.AbsenceService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository, TeacherRepository teacherRepository, SubjectRepository subjectRepository, StudentRepository studentRepository, ModelMapper mapper) {
        this.absenceRepository = absenceRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Override
    public AbsenceDtoRequest createAbsence(AbsenceDtoRequest absenceDto, TeacherDtoRequest teacherDto, StudentDtoRequest studentDto, SubjectDtoRequest subjectDto) {
        // check whether this absence already exists

        // create a new absence
        Absence absence = mapper.map(absenceDto, Absence.class);
        Teacher teacher = teacherRepository.findByFirstNameAndLastName(teacherDto.getFirstName(), teacherDto.getLastName()).get();
        Student student = studentRepository.findByFirstNameAndLastName(studentDto.getFirstName(), studentDto.getLastName()).get();
        Subject subject = subjectRepository.findBySubjectType(subjectDto.getSubjectType()).get();

        // create absence only by teachers that has qualification for this subject
        Optional<Subject> subjectToBeFound = teacher.getSubjects().stream().filter(s -> s.getSubjectType().equals(subject.getSubjectType())).findFirst();
        if (subjectToBeFound.isPresent()) {
            absence.setTeacher(teacher);
        } else {
            throw new IllegalArgumentException();
        }

        absence.setStudent(student);

//        if (absence.getSubject() == null) {
//            absence.setSubject(new ArrayList<>());
//        }

        absence.setSubject(subject);

        // persist to db
        absenceRepository.save(absence);

        // return dto
        return absenceDto;
    }

    @Override
    public void deleteAbsence(long absenceId) {
        // check whether this absence exists
        if (absenceRepository.findById(absenceId).isPresent()) {
            Absence absence = absenceRepository.findById(absenceId).get();

            absence.setTeacher(null);
            absence.setStudent(null);
            absence.setSubject(null);

            absenceRepository.delete(absence);
        }

        // throw exception
    }
}
