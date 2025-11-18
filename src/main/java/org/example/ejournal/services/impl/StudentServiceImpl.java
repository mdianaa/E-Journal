package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.response.*;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.ejournal.util.CheckExistsUtil.checkIfSchoolClassExists;
import static org.example.ejournal.util.CheckExistsUtil.checkIfSchoolExists;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final GradeRepository gradeRepository;
    private final AbsenceRepository absenceRepository;
    private final BadNoteRepository badNoteRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Override
    @Transactional(readOnly = true)
    public StudentDtoResponse viewStudent(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User with this email " + username + " cannot be found"));


        Student s = studentRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for user id %d".formatted(user.getId())));

        return toDto(s);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<StudentDtoResponse> viewAllStudentsInSchool(long schoolId) {
        checkIfSchoolExists(schoolRepository, schoolId);

        Set<Student> all = studentRepository.findAllBySchoolId(schoolId);

        return all.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<StudentDtoResponse> viewAllStudentsInClass(long schoolClassId) {
        checkIfSchoolClassExists(schoolClassRepository, schoolClassId);

        SchoolClass sc = schoolClassRepository.findById(schoolClassId)
                .orElseThrow(() -> new IllegalArgumentException("School class with id " + schoolClassId + " not found"));

        Set<Student> list = studentRepository.findAllBySchoolClassId(sc.getId());

        return list.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void withdrawStudent(long studentId) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + studentId + " not found"));

        s.setSchoolClass(null);
        s.setSchool(null);

        s.setParent(null);

        if (s.getTeachers() != null && !s.getTeachers().isEmpty()) {
            for (Teacher t : new ArrayList<>(s.getTeachers())) {
                if (t.getStudents() != null) {
                    t.getStudents().remove(s);
                }
            }
            s.getTeachers().clear();
        }

        studentRepository.save(s);
    }

    @Override
    @Transactional
    public void deleteStudent(long studentId) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + studentId + " not found"));

        long g = gradeRepository.countByStudent_Id(studentId);
        long a = absenceRepository.countByStudent_Id(studentId);
        long b = badNoteRepository.countByStudent_Id(studentId);
        if (g > 0 || a > 0 || b > 0) {
            throw new IllegalStateException(
                    ("Cannot delete student %d: existing history (grades=%d, absences=%d, badNotes=%d). " +
                            "Use withdrawStudent(...) instead to preserve records.")
                            .formatted(studentId, g, a, b)
            );
        }

        if (s.getTeachers() != null && !s.getTeachers().isEmpty()) {
            for (Teacher t : new java.util.ArrayList<>(s.getTeachers())) {
                if (t.getStudents() != null) t.getStudents().remove(s);
            }
            s.getTeachers().clear();
        }
        s.setParent(null);
        s.setSchoolClass(null);
        s.setSchool(null);

        userRepository.delete(s.getUser());
        studentRepository.delete(s);
    }

    private StudentDtoResponse toDto(Student s) {
        User u = s.getUser();
        String fullName = null;
        if (u != null) {
            String fn = Optional.ofNullable(u.getFirstName()).orElse("");
            String ln = Optional.ofNullable(u.getLastName()).orElse("");
            String full = (fn + " " + ln).trim();
            fullName = full.isBlank() ? null : full;
        }

        String parentFullName = null;
        if (s.getParent() != null && s.getParent().getUser() != null) {
            String fn = Optional.ofNullable(s.getParent().getUser().getFirstName()).orElse("");
            String ln = Optional.ofNullable(s.getParent().getUser().getLastName()).orElse("");
            String full = (fn + " " + ln).trim();
            parentFullName = full.isBlank() ? null : full;
        }

        StudentDtoResponse dto = new StudentDtoResponse();
        dto.setId(s.getId());
        dto.setFullName(fullName);
        dto.setEmail(u != null ? u.getEmail() : null);
        dto.setPhoneNumber(u != null ? u.getPhoneNumber() : null);
        dto.setParentFullName(parentFullName);
        dto.setSchoolClassId(s.getSchoolClass() != null ? s.getSchoolClass().getId() : null);
        dto.setSchoolClassName(s.getSchoolClass() != null ? s.getSchoolClass().getClassName() : null);
        dto.setSchoolId(s.getSchool() != null ? s.getSchool().getId() : null);
        dto.setSchoolName(s.getSchool() != null ? s.getSchool().getName() : null);
        return dto;
    }
}