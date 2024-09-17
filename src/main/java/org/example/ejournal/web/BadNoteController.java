package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.BadNoteDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;
import org.example.ejournal.services.BadNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/bad-notes")
public class BadNoteController {

    private final BadNoteService badNoteService;

    public BadNoteController(BadNoteService badNoteService) {
        this.badNoteService = badNoteService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> showCreateBadNotePage() {
        return ResponseEntity.ok("create bad note");
    }

    @PostMapping("/create/student/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<BadNoteDtoResponse> createBadNote(@Valid @RequestBody BadNoteDtoRequest badNoteDto,
                                                            @PathVariable long studentId) {
        try {
            BadNoteDtoResponse badNoteResponse = badNoteService.createBadNote(badNoteDto, studentId);
            return new ResponseEntity<>(badNoteResponse, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{badNoteId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteBadNote(@PathVariable long badNoteId) {
        badNoteService.deleteBadNote(badNoteId);
        return ResponseEntity.noContent().build();
    }
}
