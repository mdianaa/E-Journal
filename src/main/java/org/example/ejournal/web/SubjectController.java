package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.services.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<SubjectDtoResponse> create(@Valid @RequestBody SubjectDtoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.createSubject(req));
    }

    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT', 'TEACHER')")
    public ResponseEntity<Set<SubjectDtoResponse>> listInSchool(@PathVariable long schoolId) {
        return ResponseEntity.ok(subjectService.viewAllSubjectsInSchool(schoolId));
    }

    @DeleteMapping("/school/{schoolId}/{subjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public void unlinkFromSchool(@PathVariable long schoolId, @PathVariable long subjectId) {
        subjectService.deleteSubjectInSchool(schoolId, subjectId);
    }
}