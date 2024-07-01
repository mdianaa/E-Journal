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

@RestController
@RequestMapping("/bad-notes")
public class BadNoteController {

    private final BadNoteService badNoteService;

    public BadNoteController(BadNoteService badNoteService) {
        this.badNoteService = badNoteService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<String> showCreateBadNotePage() {
        return ResponseEntity.ok("create bad note");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<BadNoteDtoResponse> createBadNote(@Valid @RequestBody BadNoteDtoRequest badNoteDto,
                                                            @Valid @RequestBody TeacherDtoRequest teacherDto,
                                                            @Valid @RequestBody StudentDtoRequest studentDto) {
        BadNoteDtoResponse createdBadNote = badNoteService.createBadNote(badNoteDto, teacherDto, studentDto);
        return new ResponseEntity<>(createdBadNote, HttpStatus.CREATED);
    }

    @DeleteMapping("/{badNoteId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Void> deleteBadNote(@PathVariable long badNoteId) {
        badNoteService.deleteBadNote(badNoteId);
        return ResponseEntity.noContent().build();
    }
}
