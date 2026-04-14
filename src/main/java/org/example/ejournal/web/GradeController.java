package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.services.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService service;

    // Create a new grade
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<GradeDtoResponse> create(@Valid @RequestBody GradeDtoRequest request) {
        GradeDtoResponse res = service.createGrade(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Show all grades of a student for a particular subject
    @GetMapping("/student/{studentId}/subject/{subjectId}")
    @PreAuthorize("(hasAuthority('STUDENT') and @authz.isStudent(authentication, #studentId)) " +
            "or (hasAuthority('PARENT') and @authz.isParentOfStudent(authentication, #studentId)) " +
            "or hasAuthority('ADMIN') " +
            "or hasAuthority('TEACHER') " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfStudent(authentication, #studentId))")
    public Set<GradeDtoResponse> byStudentAndSubject(@PathVariable long studentId,
                                                     @PathVariable long subjectId) {
        return service.showAllStudentGradesForSubject(studentId, subjectId);
    }

    // Show all grades of a student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("(hasAuthority('STUDENT') and @authz.isStudent(authentication, #studentId)) " +
            "or (hasAuthority('PARENT') and @authz.isParentOfStudent(authentication, #studentId)) " +
            "or hasAuthority('ADMIN') " +
            "or hasAuthority('TEACHER') " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfStudent(authentication, #studentId))")
    public Set<GradeDtoResponse> byStudent(@PathVariable long studentId) {
        return service.showAllStudentGrades(studentId);
    }

}