package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.*;
import org.example.ejournal.enums.SubjectType;
import org.example.ejournal.models.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.GradeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final ModelMapper mapper;

    public GradeServiceImpl(GradeRepository gradeRepository, TeacherRepository teacherRepository, SubjectRepository subjectRepository, StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, ModelMapper mapper) {
        this.gradeRepository = gradeRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.schoolClassRepository = schoolClassRepository;
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
        if (canTeacherGradeSubject(teacher.getId(), subject.getSubjectType())) {
            grade.setGradedByTeacher(teacher);

            grade.setSubject(subject);
            grade.setStudent(student);

            // persist grade to db
            gradeRepository.save(grade);

            // return dto
            return gradeDto;
        }

       return null;
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

        return null;
    }

    @Override
    public BigDecimal viewAverageGradeForSubject(long schoolId, SubjectType subject, String classNumber) {
        return gradeRepository.findAverageGradeForSubject(schoolId, subject, classNumber);
    }

    @Override
    public BigDecimal viewAverageGradeForTeacher(long teacherId) {
        return gradeRepository.findAverageGradeForTeacher(teacherId);
    }

    @Override
    public BigDecimal viewAverageGradeForStudent(long studentId) {
        return gradeRepository.findAverageGradeForStudent(studentId);
    }

    @Override
    public BigDecimal viewAverageGradeForSchool(long schoolId) {
        return gradeRepository.findAverageGradeForSchool(schoolId);
    }

    @Override
    public int viewGradeCountInSchoolClass(BigDecimal grade, long schoolClassId) {
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId).get();

        return gradeRepository.findCountOfGradeBySchoolClass(grade, schoolClass.getClassName());
    }

    @Override
    public int viewGradeCountForSubject(BigDecimal grade, long subjectId) {
        SubjectType subjectType = subjectRepository.findById(subjectId).get().getSubjectType();

        return gradeRepository.findCountOfGradeBySubject(grade, subjectType);
    }

    @Override
    public int viewGradeCountForTeacher(BigDecimal grade, long teacherId) {
        return gradeRepository.findCountOfGradeByTeacher(grade, teacherId);
    }

    @Override
    public int viewGradeCountInSchool(BigDecimal grade, long schoolId) {
        return gradeRepository.findCountOfGradeBySchool(grade, schoolId);
    }

    private boolean canTeacherGradeSubject(long teacherId, SubjectType subjectType) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);

        return teacherOptional.map(t -> t.getSubjects().stream()
                        .anyMatch(s -> s.getSubjectType().equals(subjectType)))
                .orElse(false);
    }
}