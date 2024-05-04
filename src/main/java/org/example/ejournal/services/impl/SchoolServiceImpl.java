package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.models.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.ScheduleService;
import org.example.ejournal.services.SchoolService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    public SchoolDtoRequest createSchool(SchoolDtoRequest schoolDto) {
        // check whether this school already exists

        // register school
        School school = mapper.map(schoolDto, School.class);

        // persist to db
        schoolRepository.save(school);

        // return dto
        return schoolDto;
    }

    @Override
    public School viewSchoolInfo(long schoolId) {
        return schoolRepository.findById(schoolId).get();
    }

    @Override
    public Map<Subject, Grade> viewAverageGradePerSubject(long schoolId, long schoolClassId) {
        School school = schoolRepository.findById(schoolId).get();

        Set<Subject> subjects = new HashSet<>(school.getSubjects());
        HashMap<Subject, Grade> gradesBySubject = new HashMap<>();



        return null;
    }

    @Override
    public Map<Teacher, Grade> viewAverageGradePerTeacher(long schoolId, long teacherId) {
        return null;
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

        // throw exception
    }
}
