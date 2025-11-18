package org.example.ejournal.services.impl;


import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;
import org.example.ejournal.entities.Absence;
import org.example.ejournal.entities.Student;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.entities.Teacher;
import org.example.ejournal.repositories.AbsenceRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.AbsenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.example.ejournal.util.CheckExistsUtil.checkIfStudentExists;
import static org.example.ejournal.util.CheckExistsUtil.checkIfTeacherExists;

@Service
@RequiredArgsConstructor
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public AbsenceDtoResponse createAbsence(AbsenceDtoRequest dto) {

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + dto.getStudentId() + " cannot be found"));
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + dto.getTeacherId() + " cannot be found"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Subject with id " + dto.getSubjectId() + " cannot be found"));

        if (absenceRepository.existsByStudent_IdAndDayAndSubject_Id(dto.getStudentId(), dto.getDay(), dto.getSubjectId())) {
            throw new IllegalArgumentException("An absence already exists for this student, day, and subject.");
        }

        var a = new Absence();
        a.setStudent(student);
        a.setTeacher(teacher);
        a.setSubject(subject);
        a.setDay(dto.getDay());
        a.setExcused(false);

        var saved = absenceRepository.save(a);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AbsenceDtoResponse> viewAllAbsencesForStudent(long studentId) {
        checkIfStudentExists(studentRepository, studentId);

        List<Absence> list = absenceRepository.findAllByStudent_IdOrderByDayDesc(studentId);
        Set<AbsenceDtoResponse> out = new LinkedHashSet<>(Math.max(16, list.size()));
        list.forEach(a -> out.add(toDto(a)));

        return out;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AbsenceDtoResponse> viewAllAbsencesGivenByTeacher(long teacherId) {
        checkIfTeacherExists(teacherRepository, teacherId);

        List<Absence> list = absenceRepository.findAllByTeacher_IdOrderByDayDesc(teacherId);
        Set<AbsenceDtoResponse> out = new LinkedHashSet<>(Math.max(16, list.size()));
        list.forEach(a -> out.add(toDto(a)));

        return out;
    }

    @Override
    public void excuseAbsence(long absenceId) {
        Absence a = absenceRepository.findById(absenceId)
                .orElseThrow(() -> new IllegalArgumentException("Absence with id " + absenceId + " cannot be found"));

        if (!a.isExcused()) {
            a.setExcused(true);
            absenceRepository.save(a);
        }
    }

    private AbsenceDtoResponse toDto(Absence a) {
        String studentName = null;
        if (a.getStudent() != null && a.getStudent().getUser() != null) {
            studentName = a.getStudent().getUser().getFirstName() + " " + a.getStudent().getUser().getLastName();
        }
        String teacherName = null;
        if (a.getTeacher() != null && a.getTeacher().getUser() != null) {
            teacherName = a.getTeacher().getUser().getFirstName() + " " + a.getTeacher().getUser().getLastName();
        }
        String subjectName = a.getSubject() != null ? a.getSubject().getName() : null;

        return new AbsenceDtoResponse(
                a.getId(),
                a.getDay(),
                a.isExcused(),
                a.getStudent() != null ? a.getStudent().getId() : null,
                studentName,
                a.getTeacher() != null ? a.getTeacher().getId() : null,
                teacherName,
                a.getSubject() != null ? a.getSubject().getId() : null,
                subjectName
        );
    }
}