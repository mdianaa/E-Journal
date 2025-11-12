package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public GradeDtoResponse createGrade(GradeDtoRequest dto) {

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + dto.getStudentId() + " cannot be found"));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Subject with id " + dto.getSubjectId() + " cannot be found"));

        Teacher teacher = teacherRepository.findById(dto.getGradedById())
                .orElseThrow(() -> new IllegalArgumentException("Grade with id " + dto.getStudentId() + " cannot be found"));

        Grade g = new Grade();
        g.setStudent(student);
        g.setSubject(subject);
        g.setGradedBy(teacher);
        g.setValue(ensureScale(dto.getValue()));

        Grade saved = gradeRepository.save(g);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<GradeDtoResponse> showAllStudentGradesForSubject(long studentId, long subjectId) {

        studentRepository.findById(studentId).
                orElseThrow(() -> new IllegalArgumentException("Student with id " + studentId + " cannot be found"));

        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject with id " + subjectId+ " cannot be found"));

        List<Grade> list = gradeRepository.findAllByStudent_IdAndSubject_IdOrderByIdDesc(studentId, subjectId);
        Set<GradeDtoResponse> out = new LinkedHashSet<>(Math.max(16, list.size()));

        list.forEach(g -> out.add(toDto(g)));
        return out;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<GradeDtoResponse> showAllStudentGrades(long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + studentId + " cannot be found"));

        List<Grade> list = gradeRepository.findAllByStudent_IdOrderByIdDesc(studentId);
        Set<GradeDtoResponse> out = new LinkedHashSet<>(Math.max(16, list.size()));

        list.forEach(g -> out.add(toDto(g)));
        return out;
    }

    public GradeDtoResponse toDto(Grade g) {
        String studentName = null;
        if (g.getStudent() != null && g.getStudent().getUser() != null) {
            studentName = g.getStudent().getUser().getFirstName() + " " + g.getStudent().getUser().getLastName();
        }
        String teacherName = null;
        if (g.getGradedBy() != null && g.getGradedBy().getUser() != null) {
            teacherName = g.getGradedBy().getUser().getFirstName() + " " + g.getGradedBy().getUser().getLastName();
        }
        String subjectName = g.getSubject() != null ? g.getSubject().getName() : null;

        return new GradeDtoResponse(
                g.getId(),
                g.getValue(),
                g.getStudent() != null ? g.getStudent().getId() : null,
                studentName,
                g.getSubject() != null ? g.getSubject().getId() : null,
                subjectName,
                g.getGradedBy() != null ? g.getGradedBy().getId() : null,
                teacherName
        );
    }

    private static BigDecimal ensureScale(BigDecimal value) {
        // Normalize to 2 decimal places to match column (precision=3, scale=2)
        return value.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}