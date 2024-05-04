package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.models.Absence;
import org.example.ejournal.models.School;
import org.example.ejournal.models.Subject;
import org.example.ejournal.models.Teacher;
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
        // throw exception
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

        // throw exception
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

        // throw exception
        return null;
    }

    @Override
    public Teacher viewTeacher(long teacherId) {
        return teacherRepository.findById(teacherId).get();
    }

    @Override
    public Set<Teacher> viewAllTeachersInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        return new HashSet<>(school.getTeachers());
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

        // throw exception
    }
}
