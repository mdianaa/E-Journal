package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.response.SchoolClassDtoResponse;
import org.example.ejournal.services.SchoolClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/school-class")
@RequiredArgsConstructor
public class SchoolClassController {
    private final SchoolClassService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<SchoolClassDtoResponse> create(@Valid @RequestBody SchoolClassDtoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createClass(req));
    }

    @PatchMapping("/{classId}/head/{teacherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<SchoolClassDtoResponse> changeHead(@PathVariable long classId, @PathVariable long teacherId) {
        return ResponseEntity.ok(service.changeHeadTeacher(classId, teacherId));
    }

    @GetMapping("/{classId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'TEACHER')")
    public ResponseEntity<SchoolClassDtoResponse> show(@PathVariable long classId) {
        return ResponseEntity.ok(service.showSchoolClass(classId));
    }

    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<Set<SchoolClassDtoResponse>> listActive(@PathVariable long schoolId) {
        return ResponseEntity.ok(service.showAllSchoolClassesInSchool(schoolId));
    }

    @PostMapping("/{classId}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public void deactivate(@PathVariable long classId) {
        service.deactivateClass(classId);
    }
}