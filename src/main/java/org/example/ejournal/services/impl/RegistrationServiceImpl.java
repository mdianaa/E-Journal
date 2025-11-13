package org.example.ejournal.services.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.*;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.RegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository users;
    private final ParentRepository parents;
    private final StudentRepository students;
    private final TeacherRepository teachers;
    private final HeadmasterRepository headmasters;
    private final SchoolRepository schools;
    private final SchoolClassRepository classes;
    private final SubjectRepository subjects;
    private final PasswordEncoder passwordEncoder;

    // --- Parent ---

    @Override
    @Transactional
    public ParentDtoResponse createParent(ParentDtoRequest dto) {
        if (users.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new EntityExistsException("Email is already in use: " + dto.getEmail());
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setAuthorities(Set.of("PARENT"));
        user = users.save(user);
        user.setPhoneNumber(dto.getPhoneNumber());

        Parent parent = new Parent();
        parent.setUser(user);
        Parent savedParent = parents.save(parent);

        Set<Student> attachedStudents = new LinkedHashSet<>();
        if (dto.getChildIds() != null && !dto.getChildIds().isEmpty()) {
            for (Long childId : dto.getChildIds()) {
                Student s = students.findById(childId)
                        .orElseThrow(() -> new IllegalArgumentException("Student with id " + childId + " not found"));
                s.setParent(savedParent);
                students.save(s);
                attachedStudents.add(s);
            }
        }

        Set<StudentParentViewDtoResponse> childrenDtos = attachedStudents.stream()
                .map(this::toStudentParentViewDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        String fullName = Stream.of(user.getFirstName(), user.getLastName())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "));
        if (fullName.isBlank()) fullName = null;

        return new ParentDtoResponse(
                savedParent.getId(),
                user.getId(),
                fullName,
                user.getEmail(),
                user.getPhoneNumber(),
                childrenDtos
        );
    }

    // --- Student ---

    @Override
    @Transactional
    public StudentDtoResponse createStudent(StudentDtoRequest dto) {
        if (users.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new EntityExistsException("Email is already in use: " + dto.getEmail());
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());

        user.setAuthorities(Set.of("STUDENT"));
        user = users.save(user);

        School school = schools.findById(dto.getSchoolId())
                .orElseThrow(() -> new IllegalArgumentException("School with id " + dto.getSchoolId() + " not found"));

        SchoolClass schoolClass = classes.findById(dto.getSchoolClassId())
                .orElseThrow(() -> new IllegalArgumentException("School class with id " + dto.getSchoolClassId() + " not found"));

        // ensure class belongs to school
        if (schoolClass.getSchool() == null || !schoolClass.getSchool().getId().equals(school.getId())) {
            throw new IllegalArgumentException("SchoolClass " + dto.getSchoolClassId()
                    + " does not belong to School " + dto.getSchoolId());
        }

        Student student = new Student();
        student.setUser(user);
        student.setSchoolClass(schoolClass);

        Parent parent = null;
        if (dto.getParentId() != null) {
            parent = parents.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student with id " + dto.getParentId() + " not found"));
            student.setParent(parent);
        }

        students.save(student);

        StudentDtoResponse resp = new StudentDtoResponse();
        resp.setFullName(user.getFirstName() + " " + user.getLastName());
        resp.setEmail(user.getEmail());
        resp.setPhoneNumber(dto.getPhoneNumber());
        resp.setParentFullName(parent != null && parent.getUser() != null ? parent.getUser().getFirstName() + " " + parent.getUser().getLastName() : null);
        resp.setSchoolClassId(schoolClass.getId());
        resp.setSchoolClassName(schoolClass.getClassName());
        resp.setSchoolId(school.getId() != null ? school.getId() : null);
        resp.setSchoolName(school.getName());

        return resp;
    }


    // --- Teacher ---

    @Override
    @Transactional
    public TeacherDtoResponse createTeacher(TeacherDtoRequest dto) {
        if (users.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new EntityExistsException("Email is already in use: " + dto.getEmail());
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAuthorities(java.util.Set.of("TEACHER"));
        user = users.save(user);

        School school = schools.findById(dto.getSchoolId())
                .orElseThrow(() -> new IllegalArgumentException("School with id " + dto.getSchoolId() + " not found"));

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setSchool(school);
        teacher.setHeadTeacher(dto.isHeadTeacher());

        if (dto.getSubjectIds() != null && !dto.getSubjectIds().isEmpty()) {
            var subs = new java.util.LinkedHashSet<>(subjects.findAllById(dto.getSubjectIds()));
            if (subs.size() != dto.getSubjectIds().size()) {
                throw new EntityNotFoundException("One or more subjects not found.");
            }
            teacher.setSubjects(subs);
        } else {
            teacher.setSubjects(new LinkedHashSet<>());
        }

        Teacher saved = teachers.save(teacher);

        Long classId = null;
        String headClassName = null;
        if (dto.getHeadTeacherOfClassId() != null) {
            SchoolClass schoolClass = classes.findById(dto.getHeadTeacherOfClassId())
                    .orElseThrow(() -> new IllegalArgumentException("School class with id " + " not found"));

            if (schoolClass.getSchool() != null && !schoolClass.getSchool().getId().equals(school.getId())) {
                throw new IllegalArgumentException("Class " + dto.getHeadTeacherOfClassId()
                        + " does not belong to School " + dto.getSchoolId());
            }

            schoolClass.setHeadTeacher(saved);
            classes.save(schoolClass);
            classId = schoolClass.getId();
            headClassName = schoolClass.getClassName();
        }

        Set<Long> subjectIds = saved.getSubjects().stream()
                .map(Subject::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> subjectNames = saved.getSubjects().stream()
                .map(Subject::getName)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        TeacherDtoResponse resp = new TeacherDtoResponse();
        resp.setId(saved.getId());
        resp.setUserId(user.getId());
        resp.setFullName(user.getFirstName() + " " + user.getLastName());
        resp.setEmail(user.getEmail());
        resp.setPhoneNumber(dto.getPhoneNumber());
        resp.setSchoolId(school.getId());
        resp.setSchoolName(school.getName());
        resp.setHeadTeacher(saved.isHeadTeacher());
        resp.setHeadTeacherOfClassId(classId);
        resp.setHeadTeacherOfClassName(headClassName);
        resp.setSubjectIds(subjectIds);
        resp.setSubjectNames(subjectNames);
        return resp;
    }

    // --- Headmaster ---

    @Override
    @Transactional
    public HeadmasterDtoResponse createHeadmaster(HeadmasterDtoRequest dto) {
        if (users.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new jakarta.persistence.EntityExistsException("Email is already in use: " + dto.getEmail());
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAuthorities(java.util.Set.of("HEADMASTER"));
        user.setPhoneNumber(dto.getPhoneNumber());
        user = users.save(user);

        // 2) Resolve School (required)
        School school = schools.findById(dto.getSchoolId())
                .orElseThrow(() -> new IllegalArgumentException("School with id " + dto.getSchoolId() + " not found"));

        // 3) Create Headmaster profile
        Headmaster headmaster = new Headmaster();
        headmaster.setUser(user);
        headmaster.setSchool(school);
        Headmaster saved = headmasters.save(headmaster);

        // 4) Build response
        HeadmasterDtoResponse resp = new HeadmasterDtoResponse();
        resp.setId(saved.getId());
        resp.setUserId(user.getId());
        resp.setFullName(user.getFirstName() + " " + user.getLastName());
        resp.setEmail(user.getEmail());
        resp.setPhoneNumber(dto.getPhoneNumber());
        resp.setSchoolId(school.getId());
        resp.setSchoolName(school.getName());
        return resp;
    }

    private StudentParentViewDtoResponse toStudentParentViewDto(Student s) {
        String childFullName = Stream.of(s.getUser().getFirstName(), s.getUser().getLastName())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(t -> !t.isBlank())
                .collect(Collectors.joining(" "));
        if (childFullName.isBlank()) childFullName = null;

        Long schoolId = (s.getSchool() != null) ? s.getSchool().getId() : null;
        String schoolName = (s.getSchool() != null) ? s.getSchool().getName() : null;

        return new StudentParentViewDtoResponse(
                s.getId(),
                childFullName,
                schoolId,
                schoolName
        );
    }
}
