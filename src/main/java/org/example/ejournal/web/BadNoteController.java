package org.example.ejournal.web;

import org.example.ejournal.dtos.request.BadNoteDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;
import org.example.ejournal.services.BadNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bad-notes")
public class BadNoteController {

    private final BadNoteService badNoteService;

    @Autowired
    public BadNoteController(BadNoteService badNoteService) {
        this.badNoteService = badNoteService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String showCreateBadNotePage() {
        return "create bad note";
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<BadNoteDtoResponse> createBadNote(
            @RequestBody BadNoteDtoRequest badNoteDto,
            @RequestBody TeacherDtoRequest teacherDto,
            @RequestBody StudentDtoRequest studentDto) {
        BadNoteDtoResponse createdBadNote = badNoteService.createBadNote(badNoteDto, teacherDto, studentDto);
        return ResponseEntity.ok(createdBadNote);
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @DeleteMapping("/{badNoteId}")
    public ResponseEntity<Void> deleteBadNote(@PathVariable long badNoteId) {
        badNoteService.deleteBadNote(badNoteId);
        return ResponseEntity.noContent().build();
    }
}
