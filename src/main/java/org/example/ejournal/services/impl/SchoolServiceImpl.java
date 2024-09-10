package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.SchoolService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final HeadmasterRepository headmasterRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public SchoolServiceImpl(SchoolRepository schoolRepository, TeacherRepository teacherRepository, SchoolClassRepository schoolClassRepository, HeadmasterRepository headmasterRepository, ParentRepository parentRepository, StudentRepository studentRepository, ModelMapper mapper) {
        this.schoolRepository = schoolRepository;
        this.teacherRepository = teacherRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.headmasterRepository = headmasterRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Override
    public SchoolDtoResponse createSchool(SchoolDtoRequest schoolDto) {
        // check whether this school already exists
        if(schoolRepository.findByName(schoolDto.getName()).isPresent()){
            throw new IllegalArgumentException("School already exists with name " + schoolDto.getName());
        }
        
        // register school
        School school = mapper.map(schoolDto, School.class);

        // persist to db
        schoolRepository.save(school);

        // return dto
        return mapper.map(school, SchoolDtoResponse.class);
    }

    @Transactional
    @Override
    public SchoolDtoResponse viewSchoolInfo(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        return mapper.map(school, SchoolDtoResponse.class);
    }

    @Transactional
    @Override
    public List<SchoolDtoResponse> viewAllSchoolsInfo(){
        List<School> schoolSet = schoolRepository.findAll();
        List<SchoolDtoResponse> schoolDtoResponses = new ArrayList<>();
        for(School school:schoolSet){
            schoolDtoResponses.add(mapper.map(school,SchoolDtoResponse.class));
        }
        return schoolDtoResponses;
    }
    @Override
    public void deleteSchool(long schoolId) {
        if (schoolRepository.findById(schoolId).isPresent()) {
            School school = schoolRepository.findById(schoolId).get();

            school.setSubjects(null);

            List<Headmaster> headmasters = headmasterRepository.findBySchool(school);
            for (Headmaster headmaster : headmasters) {
                headmaster.setSchool(null);
                headmasterRepository.save(headmaster);
            }

            school.setHeadmaster(null);

            List<Parent> parents = parentRepository.findBySchool(school);
            for (Parent parent : parents) {
                parent.setSchool(null);
                parentRepository.save(parent);
            }

            school.setParents(null);

            List<Student> students = studentRepository.findBySchool(school);
            for (Student student : students) {
                student.setSchool(null);
                studentRepository.save(student);
            }

            school.setStudents(null);

            List<Teacher> teachers = teacherRepository.findBySchool(school);
            for (Teacher teacher : teachers) {
                teacher.setSchool(null);
                teacherRepository.save(teacher);
            }

            List<SchoolClass> schoolClasses = schoolClassRepository.findBySchool(school);
            for (SchoolClass schoolClass : schoolClasses) {
                schoolClass.setSchool(null);
                schoolClassRepository.save(schoolClass);
            }

            school.setTeachers(null);

            schoolRepository.delete(school);
        }
    }
}
