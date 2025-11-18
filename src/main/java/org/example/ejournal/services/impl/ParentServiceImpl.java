package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.dtos.response.StudentParentViewDtoResponse;
import org.example.ejournal.entities.Parent;
import org.example.ejournal.entities.Student;
import org.example.ejournal.repositories.ParentRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.UserRepository;
import org.example.ejournal.services.ParentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.ejournal.util.CheckExistsUtil.checkIfSchoolExists;
import static org.example.ejournal.util.CheckExistsUtil.checkIfStudentExists;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    @Override
    public ParentDtoResponse viewParentOfStudent(long studentId) {
        checkIfStudentExists(studentRepository, studentId);

        Parent parent = parentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("No parent for child with id " + studentId + " found"));
        return toDto(parent);
    }

    @Override
    public Set<ParentDtoResponse> viewAllParentsInSchool(long schoolId) {
        checkIfSchoolExists(schoolRepository, schoolId);

        Set<Parent> parents = parentRepository.findAllByChildSchoolId(schoolId);
        return parents.stream().map(this::toDto).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void deleteParent(long parentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent with id " + parentId + " not found"));

        if (parent.getChildren() != null) {
            for (Student child : parent.getChildren()) {
                child.setParent(null);
            }
            parent.getChildren().clear();
        }

        userRepository.delete(parent.getUser());
        parentRepository.delete(parent);
    }

    private ParentDtoResponse toDto(Parent p) {
        Long userId = p.getUser() != null ? p.getUser().getId() : null;
        String fullName = p.getUser() != null ? p.getUser().getFirstName() + " " + p.getUser().getLastName() : null;
        String email = p.getUser() != null ? p.getUser().getEmail() : null;
        String phone = p.getUser() != null ? p.getUser().getPhoneNumber() : null;

        Set<StudentParentViewDtoResponse> children = (p.getChildren() == null) ? Set.of()
                : p.getChildren().stream().map(this::toDto).collect(Collectors.toCollection(LinkedHashSet::new));

        ParentDtoResponse dto = new ParentDtoResponse();
        dto.setId(p.getId());
        dto.setUserId(userId);
        dto.setFullName(fullName);
        dto.setEmail(email);
        dto.setPhoneNumber(phone);
        dto.setChildren(children);
        return dto;
    }

    private StudentParentViewDtoResponse toDto(Student s) {
        String fullName = ((s.getUser().getFirstName() != null ? s.getUser().getFirstName() : "") + " " +
                (s.getUser().getLastName() != null ? s.getUser().getLastName() : "")).trim();
        Long schoolId = (s.getSchool() != null) ? s.getSchool().getId() : null;
        String schoolName = (s.getSchool() != null) ? s.getSchool().getName() : null;

        StudentParentViewDtoResponse dto = new StudentParentViewDtoResponse();
        dto.setId(s.getId());
        dto.setFullName(fullName);
        dto.setSchoolId(schoolId);
        dto.setSchoolName(schoolName);
        return dto;
    }
}
