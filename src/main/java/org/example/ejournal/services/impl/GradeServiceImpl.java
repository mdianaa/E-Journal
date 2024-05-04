package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.models.Grade;
import org.example.ejournal.models.Student;
import org.example.ejournal.models.Subject;
import org.example.ejournal.models.Teacher;
import org.example.ejournal.repositories.GradeRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.GradeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public GradeServiceImpl(GradeRepository gradeRepository, TeacherRepository teacherRepository, SubjectRepository subjectRepository, StudentRepository studentRepository, ModelMapper mapper) {
        this.gradeRepository = gradeRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Override
    public GradeDtoRequest createGrade(GradeDtoRequest gradeDto, TeacherDtoRequest teacherDto, SubjectDtoRequest subjectDto, StudentDtoRequest studentDto) {
        // create grade
        Grade grade = mapper.map(gradeDto, Grade.class);
        Teacher teacher = teacherRepository.findByFirstNameAndLastName(teacherDto.getFirstName(), teacherDto.getLastName()).get();
        Subject subject = subjectRepository.findBySubjectType(subjectDto.getSubjectType()).get();
        Student student = studentRepository.findByFirstNameAndLastName(studentDto.getFirstName(), studentDto.getLastName()).get();

        // check if the teacher can grade this subject
        Optional<Subject> subjectToBeFound = teacher.getSubjects().stream().filter(s -> s.getSubjectType().equals(subject.getSubjectType())).findFirst();
        if (subjectToBeFound.isPresent()) {
            grade.setGradedByTeacher(teacher);
        } else {
            throw new IllegalArgumentException();
        }

        grade.setSubject(subject);
        grade.setStudent(student);

        // persist grade to db
        gradeRepository.save(grade);

        // return dto
        return gradeDto;
    }

    @Override
    public GradeDtoRequest editGrade(long gradeId, GradeDtoRequest gradeDto) {
        // find grade in the db
        if (gradeRepository.findById(gradeId).isPresent()) {
            Grade grade = gradeRepository.findById(gradeId).get();

            // update grade
            mapper.map(gradeDto, grade);

            // persist to db
            gradeRepository.save(grade);

            // return dto
            return gradeDto;
        }

        // throw exception
        return null;
    }
}
