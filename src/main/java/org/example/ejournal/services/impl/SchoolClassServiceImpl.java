package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Student;
import org.example.ejournal.entities.Teacher;
import org.example.ejournal.repositories.SchoolClassRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.SchoolClassService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public SchoolClassServiceImpl(SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository, SchoolRepository schoolRepository, StudentRepository studentRepository, ModelMapper mapper) {
        this.schoolClassRepository = schoolClassRepository;
        this.teacherRepository = teacherRepository;
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Override
    public SchoolClassDtoRequest createClass(SchoolClassDtoRequest schoolClassDto, TeacherDtoRequest headTeacherDto, SchoolDtoRequest schoolDto) {
        // check whether this class already exists

        // register class
        SchoolClass schoolClass = mapper.map(schoolClassDto, SchoolClass.class);
        Teacher headTeacher = teacherRepository.findByFirstNameAndLastName(headTeacherDto.getFirstName(), headTeacherDto.getLastName()).get();
        School school = schoolRepository.findByName(schoolDto.getName()).get();

        if (!headTeacher.isHeadTeacher()) {
            schoolClass.setHeadTeacher(headTeacher);
            headTeacher.setHeadTeacher(true);
        } else {
            // throw exception
            throw new IllegalArgumentException();
        }

        teacherRepository.save(headTeacher);
        schoolClass.setSchool(school);

        // persist to db
        schoolClassRepository.save(schoolClass);

        // return dto
        return schoolClassDto;
    }

    @Override
    public SchoolClassDtoRequest changeHeadTeacher(long classId, TeacherDtoRequest headTeacherDto) {
        if (schoolClassRepository.findById(classId).isPresent()) {
            SchoolClass schoolClass = schoolClassRepository.findById(classId).get();
            Teacher headTeacher = teacherRepository.findByFirstNameAndLastName(headTeacherDto.getFirstName(), headTeacherDto.getLastName()).get();

            schoolClass.setHeadTeacher(headTeacher);

            // persist to db
            schoolClassRepository.save(schoolClass);

            // return dto
            return mapper.map(schoolClass, SchoolClassDtoRequest.class);
        }

        return null;
    }

    @Override
    public void deleteClass(long classId) {
        if (schoolClassRepository.findById(classId).isPresent()) {
            SchoolClass schoolClass = schoolClassRepository.findById(classId).get();

            List<Student> students = studentRepository.findAllBySchoolClass(schoolClass);
            for (Student student : students) {
                student.setSchoolClass(null);
                studentRepository.save(student);
            }

            schoolClass.setHeadTeacher(null);
            schoolClass.setSchool(null);

            schoolClassRepository.delete(schoolClass);
        }
    }
}
