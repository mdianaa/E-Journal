package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.BadNoteDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;
import org.example.ejournal.services.BadNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/admin/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> showAllBadNotesForStudentAsAdmin(@PathVariable long studentId) {
        try {
            List<BadNoteDtoResponse> badNotes = badNoteService.getBadNotesForStudent(studentId);
            return new ResponseEntity<>(badNotes, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> showAllBadNotesForStudentAsStudent() {
        try {
            List<BadNoteDtoResponse> badNotes = badNoteService.showAllBadNotesForStudentAsStudent();
            return new ResponseEntity<>(badNotes, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/parent/{studentId}")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> showAllBadNotesForStudentAsParent(@PathVariable long studentId) {
        try {
            List<BadNoteDtoResponse> badNotes = badNoteService.showAllBadNotesForStudentAsParent(studentId);
            return new ResponseEntity<>(badNotes, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teacher/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> showAllBadNotesForStudentAsTeacher(@PathVariable long studentId) {
        try {
            List<BadNoteDtoResponse> badNotes = badNoteService.showAllBadNotesForStudentAsTeacher(studentId);
            return new ResponseEntity<>(badNotes, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/headmaster/{studentId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<?> showAllBadNotesForStudentAsHeadmaster(@PathVariable long studentId) {
        try {
            List<BadNoteDtoResponse> badNotes = badNoteService.showAllBadNotesForStudentAsHeadmaster(studentId);
            return new ResponseEntity<>(badNotes, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{badNoteId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteBadNote(@PathVariable long badNoteId) {
        badNoteService.deleteBadNote(badNoteId);
        return ResponseEntity.noContent().build();
    }
}
