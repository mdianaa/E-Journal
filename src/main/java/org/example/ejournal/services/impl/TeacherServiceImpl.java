package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.ejournal.util.CheckExistsUtil.checkIfSchoolExists;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolClassRepository classRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TeacherDtoResponse changeSubjects(long teacherId, Set<SubjectDtoRequest> subjectDtos) {
        Teacher t = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher with id " + teacherId + " not found"));

        if (subjectDtos == null || subjectDtos.isEmpty()) {
            if (t.getSubjects() != null) t.getSubjects().clear();
            return toDto(teacherRepository.save(t));
        }

        Set<String> requestedNamesLower = subjectDtos.stream()
                .map(SubjectDtoRequest::getName)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (requestedNamesLower.isEmpty()) {
            if (t.getSubjects() != null) t.getSubjects().clear();
            return toDto(teacherRepository.save(t));
        }

        List<Subject> existing = subjectRepository.findByNamesIgnoreCase(requestedNamesLower);
        Map<String, Subject> byLowerName = existing.stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(Locale.ROOT), Function.identity()));

        for (String nameLower : requestedNamesLower) {
            if (!byLowerName.containsKey(nameLower)) {
                Subject s = new Subject();
                s.setName(nameLower);
                s.setTeachers(new LinkedHashSet<>());
                s = subjectRepository.save(s);
                byLowerName.put(nameLower, s);
            }
        }

        Set<Subject> newSet = requestedNamesLower.stream()
                .map(byLowerName::get)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (t.getSubjects() == null) t.setSubjects(new LinkedHashSet<>());
        else t.getSubjects().clear();
        t.getSubjects().addAll(newSet);

        return toDto(teacherRepository.save(t));
    }

    @Override
    @Transactional
    public TeacherDtoResponse removeHeadTeacherTitle(long teacherId) {
        Teacher t = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher with id " + teacherId + " not found"));

        if (t.getHeadTeacherOf() != null) {
            throw new IllegalStateException(
                    "Teacher %d is head of class %d. Reassign that class's head teacher first."
                            .formatted(t.getId(), t.getHeadTeacherOf().getId())
            );
        }

        t.setHeadTeacher(false);

        return toDto(teacherRepository.save(t));
    }

    @Override
    @Transactional
    public TeacherDtoResponse viewTeacher(long teacherId) {
        Teacher t = teacherRepository.fetchByIdWithUser(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher with id " + teacherId + " not found"));

        return toDto(t);
    }

    @Override
    @Transactional
    public TeacherDtoResponse viewHeadTeacher(long teacherId, long schoolClassId) {
        classRepository.findByIdAndHeadTeacher(schoolClassId, teacherId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Teacher %d is not the head teacher of class %d".formatted(teacherId, schoolClassId)
                ));

        Teacher t = teacherRepository.fetchByIdWithUser(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher with id " + teacherId + " not found"));

        return toDto(t);
    }

    @Override
    @Transactional
    public Set<TeacherDtoResponse> viewAllHeadTeachersInSchool(long schoolId) {
        checkIfSchoolExists(schoolRepository, schoolId);

        return teacherRepository.findAllBySchool_IdAndHeadTeacherTrue(schoolId).stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public Set<TeacherDtoResponse> viewAllTeachersInSchool(long schoolId) {
        checkIfSchoolExists(schoolRepository, schoolId);

        return teacherRepository.findAllBySchool_Id(schoolId).stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void deleteTeacher(long teacherId) {
        Teacher t = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher with id " + teacherId + " not found"));

        if (t.getHeadTeacherOf() != null) {
            throw new IllegalStateException(
                    "Cannot delete teacher %d: they are head teacher of class %d. Reassign first."
                            .formatted(t.getId(), t.getHeadTeacherOf().getId())
            );
        }

        if (t.getSubjects() != null) t.getSubjects().clear();
        if (t.getStudents() != null) {
            for (Student s : new ArrayList<>(t.getStudents())) {
                if (s.getTeachers() != null) s.getTeachers().remove(t);
            }
            t.getStudents().clear();
        }

        userRepository.delete(t.getUser());
        teacherRepository.delete(t);

    }

    private TeacherDtoResponse toDto(Teacher t) {
        User u = t.getUser();

        String fullName = null;
        if (u != null) {
            String fn = Optional.ofNullable(u.getFirstName()).orElse("");
            String ln = Optional.ofNullable(u.getLastName()).orElse("");
            String full = (fn + " " + ln).trim();
            fullName = full.isBlank() ? null : full;
        }

        Long headClassId = null;
        String headClassName = null;
        if (t.getHeadTeacherOf() != null) {
            headClassId = t.getHeadTeacherOf().getId();
            headClassName = t.getHeadTeacherOf().getClassName();
        }

        Set<Long> subjectIds = (t.getSubjects() == null) ? Set.of()
                : t.getSubjects().stream().map(Subject::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Set<String> subjectNames = (t.getSubjects() == null) ? Set.of()
                : t.getSubjects().stream().map(Subject::getName)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        TeacherDtoResponse dto = new TeacherDtoResponse();
        dto.setId(t.getId());
        dto.setUserId(u != null ? u.getId() : null);
        dto.setFullName(fullName);
        dto.setEmail(u != null ? u.getEmail() : null);
        dto.setPhoneNumber(u != null ? u.getPhoneNumber() : null);
        dto.setSchoolId(t.getSchool() != null ? t.getSchool().getId() : null);
        dto.setSchoolName(t.getSchool() != null ? t.getSchool().getName() : null);
        dto.setHeadTeacher(t.isHeadTeacher());
        dto.setHeadTeacherOfClassId(headClassId);
        dto.setHeadTeacherOfClassName(headClassName);
        dto.setSubjectIds(subjectIds);
        dto.setSubjectNames(subjectNames);

        return dto;
    }
}