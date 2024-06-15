package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.dtos.response.StudentDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        return null;
    }

    @Override
    public List<GradeDtoResponse> showAllGradesForSubject(long studentId, SubjectDtoRequest subjectDto) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();
            Subject subject = subjectRepository.findBySubjectType(subjectDto.getSubjectType()).get();

            List<Grade> grades = student.getGrades().stream().filter(g -> g.getSubject().getSubjectType().equals(subject.getSubjectType())).toList();
            List<GradeDtoResponse> gradesDto = new ArrayList<>();

            for (Grade grade : grades) {
                gradesDto.add(mapper.map(grade, GradeDtoResponse.class));
            }

            return gradesDto;
        }

        return null;
    }

    @Override
    public Set<AbsenceDtoResponse> showAllAbsencesForStudent(long studentId) {
        if (studentRepository.findById(studentId).isPresent()) {
            Student student = studentRepository.findById(studentId).get();

            Set<Absence> absences = student.getAbsences();
            Set<AbsenceDtoResponse> absencesDto = new HashSet<>();

            for (Absence absence : absences) {
                absencesDto.add(mapper.map(absence, AbsenceDtoResponse.class));
            }

            return absencesDto;
        }

        return null;
    }

    @Override
    public StudentDtoResponse viewStudent(long studentId) {
        Student student = studentRepository.findById(studentId).get();

        return mapper.map(student, StudentDtoResponse.class);
    }

    @Transactional
    @Override
    public Set<StudentDtoResponse> showAllStudentsInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        Set<Student> students = school.getStudents();
        Set<StudentDtoResponse> studentsDto = new HashSet<>();

        for (Student student : students) {
            studentsDto.add(mapper.map(student, StudentDtoResponse.class));
        }

        return studentsDto;
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
    }
}
