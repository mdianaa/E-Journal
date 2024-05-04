package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.models.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final ParentRepository parentRepository;
    private final AbsenceRepository absenceRepository;
    private final ModelMapper mapper;

    public StudentServiceImpl(StudentRepository studentRepository, SchoolRepository schoolRepository, SchoolClassRepository schoolClassRepository, SubjectRepository subjectRepository, ParentRepository parentRepository, AbsenceRepository absenceRepository, ModelMapper mapper) {
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
        this.parentRepository = parentRepository;
        this.absenceRepository = absenceRepository;
        this.mapper = mapper;
    }

    @Override
    public StudentDtoRequest createStudent(StudentDtoRequest studentDto, SchoolDtoRequest schoolDto, SchoolClassDtoRequest schoolClassDto, ParentDtoRequest parentDto) {
        // check if student exists already

        // register student
        Student student = mapper.map(studentDto, Student.class);
        School school = schoolRepository.findByName(schoolDto.getName()).get();
        SchoolClass schoolClass = schoolClassRepository.findByClassName(schoolClassDto.getClassName()).get();

        // TODO: find by ID, not names
        Parent parent = parentRepository.findByFirstNameAndLastName(parentDto.getFirstName(), parentDto.getLastName()).get();

        student.setSchool(school);
        student.setSchoolClass(schoolClass);
        student.setParent(parent);

        // persist to db
        studentRepository.save(student);

        // return dto
        return studentDto;
    }

    @Override
    public StudentDtoRequest editStudent(long studentId, StudentDtoRequest studentDto) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();

            mapper.map(studentDto, student);

            return studentDto;
        }

        // throw exception
        return null;
    }

    @Override
    public Set<Grade> showAllGradesForSubject(long studentId, SubjectDtoRequest subjectDto) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();
            Subject subject = subjectRepository.findBySubjectType(subjectDto.getSubjectType()).get();

            return student.getGrades().stream().filter(g -> g.getSubject().equals(subject)).collect(Collectors.toSet());
        }

        // throw exception
        return null;
    }

    @Override
    public Set<Absence> showAllAbsencesForStudent(long studentId) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();

            return new HashSet<>(student.getAbsences());
        }

        // throw exception
        return null;
    }

    @Override
    public Set<Student> showAllStudentsInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        return new HashSet<>(school.getStudents());
    }

    @Override
    public void withdrawStudent(long studentId) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();

            student.setSchoolClass(null);
            student.setParent(null);
            student.setSchool(null);
            student.setTeachers(null);
            List<Absence> absences = absenceRepository.findAllByStudent(student);
            absenceRepository.deleteAll(absences);

            student.setAbsences(null);
            student.setGrades(null);

            studentRepository.delete(student);
        }

        // throw exception
    }
}
