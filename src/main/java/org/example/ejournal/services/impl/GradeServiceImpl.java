package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.enums.SubjectType;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.GradeService;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final TeacherRepository teacherRepository;
    private final UserAuthenticationService userAuthenticationService;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final ModelMapper mapper;

    public GradeServiceImpl(GradeRepository gradeRepository, TeacherRepository teacherRepository, UserAuthenticationService userAuthenticationService, SubjectRepository subjectRepository, StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, TeacherSubjectRepository teacherSubjectRepository, ModelMapper mapper) {
        this.gradeRepository = gradeRepository;
        this.teacherRepository = teacherRepository;
        this.userAuthenticationService = userAuthenticationService;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.schoolClassRepository = schoolClassRepository;
	    this.teacherSubjectRepository = teacherSubjectRepository;
	    this.mapper = mapper;
    }

    @Transactional
    @Override
    public GradeDtoResponse createGrade(GradeDtoRequest gradeDto, long subjectId, long studentId) {
        // get the authenticated teacher
        Teacher teacher = (Teacher) userAuthenticationService.getAuthenticatedUser();

        // ensure the teacher has a school associated with them
        if (teacher.getSchool() == null) {
            throw new NoSuchElementException("No school found for the authenticated teacher");
        }

        // create grade
        Grade grade = mapper.map(gradeDto, Grade.class);
        Subject subject = subjectRepository.findById(subjectId).get();
        Student student = studentRepository.findById(studentId).get();

        // check if the teacher can grade this subject
        boolean isTeacherAssignedProperly = teacherSubjectRepository.existsByTeacherAndSubject(teacher,subject);
        if (!isTeacherAssignedProperly) {
            // todo: throw
        }
        grade.setGradedByTeacher(teacher);

        grade.setSubject(subject);
        grade.setStudent(student);

        // persist grade to db
        gradeRepository.save(grade);

        return mapper.map(grade, GradeDtoResponse.class);
    }

    @Transactional
    @Override
    public GradeDtoResponse editGrade(long gradeId, GradeDtoRequest gradeDto) {
        // get the authenticated teacher
        Teacher teacher = (Teacher) userAuthenticationService.getAuthenticatedUser();

        // ensure the teacher has a school associated with them
        if (teacher.getSchool() == null) {
            throw new NoSuchElementException("No school found for the authenticated teacher");
        }

        // find grade in the db
        if (gradeRepository.findById(gradeId).isEmpty()) {
            throw new NoSuchElementException("Grade with ID " + gradeId + " not found");
        }

        Grade grade = gradeRepository.findById(gradeId).get();

        // update grade
        mapper.map(gradeDto, grade);

        // persist to db
        gradeRepository.save(grade);

        // return dto
        return mapper.map(grade, GradeDtoResponse.class);
    }

    @Override
    public BigDecimal viewAverageGradeForSubject(long schoolId, String subjectName, String classNumber) {
        return gradeRepository.findAverageGradeForSubject(schoolId, subjectName, classNumber);
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

        return gradeRepository.findCountOfGradeBySchoolClass(grade, schoolClass.getId());
    }

    @Override
    public int viewGradeCountForSubject(BigDecimal grade, long subjectId) {
        String subjectName = subjectRepository.findById(subjectId).get().getName();

        return gradeRepository.findCountOfGradeBySubject(grade, subjectName);
    }

    @Override
    public int viewGradeCountForTeacher(BigDecimal grade, long teacherId) {
        return gradeRepository.findCountOfGradeByTeacher(grade, teacherId);
    }

    @Override
    public int viewGradeCountInSchool(BigDecimal grade, long schoolId) {
        return gradeRepository.findCountOfGradeBySchool(grade, schoolId);
    }

//    private boolean canTeacherGradeSubject(long teacherId, long subjectId) {
//        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
//
//        return teacherOptional.map(t -> t.getSubjects().stream()
//                        .anyMatch(s -> s.getSubjectType().equals(subjectType)))
//                .orElse(false);
//    }
}