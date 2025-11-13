package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;
import org.example.ejournal.entities.School;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.services.SchoolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
    @Transactional
    public SchoolDtoResponse createSchool(SchoolDtoRequest schoolDto) {
        if (schoolRepository.existsByNameIgnoreCase(schoolDto.getName())) {
            throw new IllegalArgumentException("Parent with name " + schoolDto.getName() + " already exists");
        }

        School s = new School();
        s.setName(schoolDto.getName().trim());
        s.setAddress(schoolDto.getAddress().trim());

        School saved = schoolRepository.save(s);
        return toDto(saved);
    }

    @Override
    @Transactional
    public SchoolDtoResponse viewSchool(long schoolId) {
        School s = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("School with id " + schoolId + " not found"));

        return toDto(s);
    }

    @Override
    public Set<SchoolDtoResponse> viewAllSchools() {
        return schoolRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void deleteSchool(long schoolId) {
        School s = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("School with id " + schoolId + " not found"));

        int teachers = (s.getTeachers() == null) ? 0 : s.getTeachers().size();
        int students = (s.getStudents() == null) ? 0 : s.getStudents().size();
        int parents  = (s.getParents()  == null) ? 0 : s.getParents().size();
        if (teachers > 0 || students > 0 || parents > 0 || s.getHeadmaster() != null) {
            throw new IllegalStateException("""
                Cannot delete school %s (id=%d): detach or move related entities first.
                teachers=%d, students=%d, parents=%d, headmaster=%s
                """.formatted(s.getName(), s.getId(), teachers, students, parents,
                    s.getHeadmaster() != null ? s.getHeadmaster().getId() : "none"));
        }

        schoolRepository.delete(s);
    }

    private SchoolDtoResponse toDto(School s) {
        Long headmasterId = (s.getHeadmaster() != null) ? s.getHeadmaster().getId() : null;
        String headmasterName = null;
        if (s.getHeadmaster() != null && s.getHeadmaster().getUser() != null) {
            String fn = Optional.ofNullable(s.getHeadmaster().getUser().getFirstName()).orElse("");
            String ln = Optional.ofNullable(s.getHeadmaster().getUser().getLastName()).orElse("");
            String full = (fn + " " + ln).trim();
            headmasterName = full.isBlank() ? null : full;
        }

        return new SchoolDtoResponse(
                s.getId(),
                s.getName(),
                s.getAddress(),
                headmasterId,
                headmasterName,
                (s.getTeachers() == null) ? 0 : s.getTeachers().size(),
                (s.getStudents() == null) ? 0 : s.getStudents().size(),
                (s.getParents()  == null) ? 0 : s.getParents().size()
        );
    }
}