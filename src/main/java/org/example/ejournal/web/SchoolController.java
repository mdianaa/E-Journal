package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;
import org.example.ejournal.services.SchoolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/school")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    // Create a new school
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<SchoolDtoResponse> create(@Valid @RequestBody SchoolDtoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolService.createSchool(req));
    }

    // Get a particular school
    @GetMapping("/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'PARENT', 'STUDENT', 'TEACHER')")
    public ResponseEntity<SchoolDtoResponse> view(@PathVariable long schoolId) {
        return ResponseEntity.ok(schoolService.viewSchool(schoolId));
    }

    // Get all the schools
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PARENT', 'STUDENT')")
    public ResponseEntity<Set<SchoolDtoResponse>> list() {
        return ResponseEntity.ok(schoolService.viewAllSchools());
    }

    // Delete school
    @DeleteMapping("/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long schoolId) {
        schoolService.deleteSchool(schoolId);
    }
}