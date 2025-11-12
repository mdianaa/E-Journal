package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.BadNoteDtoRequest;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;
import org.example.ejournal.services.BadNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/bad-note")
@RequiredArgsConstructor
public class BadNoteController {

    private final BadNoteService badNoteService;

    // Create a new bad note
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    public ResponseEntity<BadNoteDtoResponse> create(@Valid @RequestBody BadNoteDtoRequest request) {
        BadNoteDtoResponse res = badNoteService.createBadNote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Show all bad notes of a particular student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyAuthority('HEADMASTER','ADMIN','TEACHER')")
    public ResponseEntity<Set<BadNoteDtoResponse>> getForStudent(@PathVariable long studentId) {
        return ResponseEntity.ok(badNoteService.viewAllBadNotesForStudent(studentId));
    }

    // Show all bad notes logged by a particular teacher
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyAuthority('HEADMASTER','ADMIN','TEACHER')")
    public ResponseEntity<Set<BadNoteDtoResponse>> getForTeacher(@PathVariable long teacherId) {
        return ResponseEntity.ok(badNoteService.viewAllBadNotesGivenByTeacher(teacherId));
    }

    // Delete a bad not
    @DeleteMapping("/{badNoteId}")
    @PreAuthorize("hasAnyAuthority('HEADMASTER','ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable long badNoteId) {
        badNoteService.deleteBadNote(badNoteId);
        return ResponseEntity.noContent().build();
    }
}