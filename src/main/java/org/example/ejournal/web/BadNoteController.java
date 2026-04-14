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
@RequiredArgsConstructor
@RequestMapping("/bad-note")
public class BadNoteController {

    private final BadNoteService badNoteService;

    // Create a new bad note
    @PostMapping
    @PreAuthorize("(hasAuthority('TEACHER') and @authz.isTeacher(authentication, #request.teacherId)) " +
            "or hasAuthority('ADMIN')")
    public ResponseEntity<BadNoteDtoResponse> create(@Valid @RequestBody BadNoteDtoRequest request) {
        BadNoteDtoResponse res = badNoteService.createBadNote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Show all bad notes of a particular student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("(hasAuthority('STUDENT') and @authz.isStudent(authentication, #studentId)) " +
            "or (hasAuthority('PARENT') and @authz.isParentOfStudent(authentication, #studentId)) " +
            "or hasAuthority('ADMIN') " +
            "or hasAuthority('TEACHER') " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfStudent(authentication, #studentId))")
    public ResponseEntity<Set<BadNoteDtoResponse>> getForStudent(@PathVariable long studentId) {
        return ResponseEntity.ok(badNoteService.viewAllBadNotesForStudent(studentId));
    }

    // Show all bad notes logged by a particular teacher
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAuthority('ADMIN') " +
            "or (hasAuthority('TEACHER') and @authz.isTeacher(authentication, #teacherId)) " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfTeacher(authentication, #teacherId))")
    public ResponseEntity<Set<BadNoteDtoResponse>> getForTeacher(@PathVariable long teacherId) {
        return ResponseEntity.ok(badNoteService.viewAllBadNotesGivenByTeacher(teacherId));
    }

    // Delete a bad not
    @DeleteMapping("/{badNoteId}")
    @PreAuthorize("hasAuthority('ADMIN') " +
            "or (hasAuthority('HEADMASTER') and @authz.canHeadmasterManageBadNote(authentication, #badNoteId))")
    public ResponseEntity<Void> delete(@PathVariable long badNoteId) {
        badNoteService.deleteBadNote(badNoteId);
        return ResponseEntity.noContent().build();
    }
}