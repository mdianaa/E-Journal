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
@RequiredArgsConstructor
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    // Create a new subject
    // TODO only for a particular school
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<SubjectDtoResponse> create(@Valid @RequestBody SubjectDtoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.createSubject(req));
    }

    // Get all the subjects in a particular school
    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT', 'TEACHER')")
    public ResponseEntity<Set<SubjectDtoResponse>> listInSchool(@PathVariable long schoolId) {
        return ResponseEntity.ok(subjectService.viewAllSubjectsInSchool(schoolId));
    }

    // Delete subject in school
    @DeleteMapping("/school/{schoolId}/{subjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchool(authentication, #schoolId)")
    public void unlinkFromSchool(@PathVariable long schoolId, @PathVariable long subjectId) {
        subjectService.deleteSubjectInSchool(schoolId, subjectId);
    }
}