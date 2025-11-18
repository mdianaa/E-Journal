package org.example.ejournal.services.impl;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.entities.Teacher;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;
    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public SubjectDtoResponse createSubject(SubjectDtoRequest subjectDto) {
        String name = subjectDto.getName().trim();
        if (subjectRepository.existsByNameIgnoreCase(name)) {
            throw new EntityExistsException("Subject name already exists: " + name);
        }
        Subject s = new Subject();
        s.setName(name);
        s.setTeachers(new LinkedHashSet<>());
        s = subjectRepository.save(s);

        return toDto(s);
    }

    @Override
    @Transactional
    public Set<SubjectDtoResponse> viewAllSubjectsInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("School with id " + schoolId + " not found"));

        Set<Subject> subs = school.getSubjects() == null ? Set.of() : school.getSubjects();

        return subs.stream()
                .sorted(Comparator.comparing(Subject::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .map(this::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void deleteSubjectInSchool(long schoolId, long subjectId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("School with id " +schoolId + " not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject with id " + subjectId + " not found"));

        if (school.getSubjects() == null || !school.getSubjects().contains(subject)) {
            throw new IllegalStateException("Subject %d is not linked to school %d".formatted(subjectId, schoolId));
        }

        school.getSubjects().remove(subject);
        schoolRepository.save(school);

        List<Teacher> affectedTeachers = teacherRepository.findBySchoolAndSubject(schoolId, subjectId);
        for (Teacher t : affectedTeachers) {
            if (t.getSubjects() != null) {
                t.getSubjects().remove(subject);
            }
        }

        teacherRepository.saveAll(affectedTeachers);

        long schoolCount = schoolRepository.countSchoolsUsingSubject(subjectId);
        long teacherCount = teacherRepository.countTeachersUsingSubject(subjectId);
        if (schoolCount == 0 && teacherCount == 0) {
            subjectRepository.delete(subject);
        }
    }

    private SubjectDtoResponse toDto(Subject s) {
        SubjectDtoResponse dto = new SubjectDtoResponse();
        dto.setId(s.getId());
        dto.setName(s.getName());
        return dto;
    }
}