package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.entities.Absence;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.entities.Teacher;
import org.example.ejournal.repositories.AbsenceRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final AbsenceRepository absenceRepository;
    private final ModelMapper mapper;

    public TeacherServiceImpl(TeacherRepository teacherRepository, SchoolRepository schoolRepository, SubjectRepository subjectRepository, AbsenceRepository absenceRepository, ModelMapper mapper) {
        this.teacherRepository = teacherRepository;
        this.schoolRepository = schoolRepository;
        this.subjectRepository = subjectRepository;
        this.absenceRepository = absenceRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public TeacherDtoRequest createTeacher(TeacherDtoRequest teacherDto, SchoolDtoRequest schoolDto, Set<SubjectDtoRequest> subjectDtos) {
        // check if this teacher exists already

        // register teacher
        Teacher teacher = mapper.map(teacherDto, Teacher.class);
        School school = schoolRepository.findByName(schoolDto.getName()).get();

        Set<Subject> subjects = subjectDtos.stream().map(s -> subjectRepository.findBySubjectType(s.getSubjectType()).get()).collect(Collectors.toSet());

        teacher.setSubjects(subjects);
        teacher.setSchool(school);

        // persist to db
        teacherRepository.save(teacher);

        // return dto
        return teacherDto;
    }

    @Override
    public TeacherDtoRequest editTeacher(long teacherId, TeacherDtoRequest teacherDto) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            Teacher teacher = teacherRepository.findById(teacherId).get();

            mapper.map(teacherDto, teacher);

            // persist to db
            teacherRepository.save(teacher);

            // return dto
            return teacherDto;
        }

        return null;
    }

    @Override
    public TeacherDtoRequest changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            Teacher teacher = teacherRepository.findById(teacherId).get();

            Set<Subject> subjects = subjectDtos.stream().map(s -> mapper.map(s, Subject.class)).collect(Collectors.toSet());

            teacher.setSubjects(subjects);

            // persist to db
            teacherRepository.save(teacher);

            // return dto
            return mapper.map(teacher, TeacherDtoRequest.class);
        }

        return null;
    }

    @Override
    public TeacherDtoRequest removeHeadTeacherTitle(long teacherId) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            Teacher teacher = teacherRepository.findById(teacherId).get();

            teacher.setHeadTeacher(false);

            // persist to db
            teacherRepository.save(teacher);

            // return dto
            return mapper.map(teacher, TeacherDtoRequest.class);
        }

        return null;
    }

    @Override
    public TeacherDtoResponse viewTeacher(long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).get();

        return mapper.map(teacher, TeacherDtoResponse.class);
    }

    @Override
    public Set<TeacherDtoResponse> viewAllTeachersInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        Set<Teacher> teachers = school.getTeachers();
        Set<TeacherDtoResponse> teachersDto = new HashSet<>();

        for (Teacher teacher : teachers) {
            teachersDto.add(mapper.map(teacher, TeacherDtoResponse.class));
        }

        return teachersDto;
    }

    @Override
    public void deleteTeacher(long teacherId) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            Teacher teacher = teacherRepository.findById(teacherId).get();

            teacher.setSubjects(null);
            teacher.setSchool(null);
            teacher.setStudents(null);

            List<Absence> absences = absenceRepository.findAllByTeacher(teacher);
            for (Absence absence : absences) {
                absence.setTeacher(null);
                absenceRepository.save(absence);
            }

            teacherRepository.delete(teacher);
        }
    }
}
