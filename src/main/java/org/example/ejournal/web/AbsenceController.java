package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;
import org.example.ejournal.services.AbsenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/absence")
@RequiredArgsConstructor
public class AbsenceController {

    private final AbsenceService service;

    // Create a new absence
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    public ResponseEntity<AbsenceDtoResponse> create(@Valid @RequestBody AbsenceDtoRequest request) {
        AbsenceDtoResponse res = service.createAbsence(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Show all absences logged by a particular teacher
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER', 'HEADMASTER')")
    public Set<AbsenceDtoResponse> getForTeacher(@PathVariable long teacherId) {
        return service.viewAllAbsencesGivenByTeacher(teacherId);
    }

    // Show all absences of a particular student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER', 'HEADMASTER')")
    public Set<AbsenceDtoResponse> getForStudent(@PathVariable long studentId) {
        return service.viewAllAbsencesForStudent(studentId);
    }

    // Excuse an absence
    @PatchMapping("/{absenceId}/excuse")
    @PreAuthorize("hasAnyAuthority('ADMIN','HEADMASTER')")
    public void excuse(@PathVariable long absenceId) {
        service.excuseAbsence(absenceId);
    }
}