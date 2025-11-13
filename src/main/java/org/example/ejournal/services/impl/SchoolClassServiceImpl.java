package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.SchoolClassDtoResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository classRepository;
    private final SchoolRepository schoolRepository;
    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public SchoolClassDtoResponse createClass(SchoolClassDtoRequest req) {
        School school = schoolRepository.findById(req.getSchoolId())
                .orElseThrow(() -> new IllegalArgumentException("School with id " + req.getSchoolId() + " not found"));

        Teacher head = teacherRepository.findById(req.getHeadTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("School class with id " + req.getHeadTeacherId() + " not found"));

        if (head.getSchool() == null || !head.getSchool().getId().equals(school.getId())) {
            throw new IllegalStateException("Head teacher must belong to the same school.");
        }

        if (classRepository.existsBySchool_IdAndClassNameIgnoreCaseAndDeactivatedFalse(school.getId(), req.getClassName())) {
            throw new IllegalStateException("Active class '%s' already exists in school %s"
                    .formatted(req.getClassName(), school.getName()));
        }
        if (classRepository.countActiveByHeadTeacher(head.getId()) > 0) {
            throw new IllegalStateException("Teacher %d is already head of another active class.".formatted(head.getId()));
        }

        SchoolClass sc = new SchoolClass();
        sc.setClassName(req.getClassName().trim());
        sc.setHeadTeacher(head);
        sc.setSchool(school);
        sc.setDeactivated(false);

        sc = classRepository.save(sc);
        return toResponse(sc);
    }

    @Override
    @Transactional
    public SchoolClassDtoResponse changeHeadTeacher(long classId, long teacherId) {
        SchoolClass sc = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class with id " + classId + " not found"));

        Teacher newHead = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher with id " + teacherId + " not found"));

        if (newHead.getSchool() == null || sc.getSchool() == null
                || !newHead.getSchool().getId().equals(sc.getSchool().getId())) {
            throw new IllegalStateException("New head teacher must belong to the same school.");
        }
        if (!sc.isDeactivated()) {
            long count = classRepository.countActiveByHeadTeacher(newHead.getId());
            boolean alreadyHeadElsewhere = count > 0 && (sc.getHeadTeacher() == null
                    || !Objects.equals(sc.getHeadTeacher().getId(), newHead.getId()));
            if (alreadyHeadElsewhere) {
                throw new IllegalStateException("Teacher %d is already head of another active class.".formatted(teacherId));
            }
        }

        sc.setHeadTeacher(newHead);
        sc = classRepository.save(sc);
        return toResponse(sc);
    }

    @Override
    @Transactional
    public SchoolClassDtoResponse showSchoolClass(long classId) {
        SchoolClass sc = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("School class with id " + classId + " not found"));

        return toResponse(sc);
    }

    @Override
    @Transactional
    public Set<SchoolClassDtoResponse> showAllSchoolClassesInSchool(long schoolId) {
        schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("School with id " + schoolId + " not found"));

        Set<SchoolClass> classEntities = classRepository.findAllBySchoolId(schoolId, true);
        return classEntities.stream()
                .map(this::toResponse)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void deactivateClass(long classId) {
        SchoolClass sc = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("School class with id " + classId + " not found"));

        if (sc.isDeactivated()) {
            throw new IllegalStateException("Class with id " + classId + " is already deactivated.");
        }

        sc.setDeactivated(true);
        classRepository.save(sc);
    }

    private SchoolClassDtoResponse toResponse(SchoolClass sc) {
        String headName = null;
        if (sc.getHeadTeacher() != null && sc.getHeadTeacher().getUser() != null) {
            String fn = Optional.ofNullable(sc.getHeadTeacher().getUser().getFirstName()).orElse("");
            String ln = Optional.ofNullable(sc.getHeadTeacher().getUser().getLastName()).orElse("");
            String full = (fn + " " + ln).trim();
            headName = full.isBlank() ? null : full;
        }

        return new SchoolClassDtoResponse(
                sc.getId(),
                sc.getClassName(),
                sc.isDeactivated(),
                sc.getSchool() != null ? sc.getSchool().getId() : null,
                sc.getSchool() != null ? sc.getSchool().getName() : null,
                sc.getHeadTeacher() != null ? sc.getHeadTeacher().getId() : null,
                headName,
                sc.getStudents() == null ? 0 : sc.getStudents().size()
        );
    }
}