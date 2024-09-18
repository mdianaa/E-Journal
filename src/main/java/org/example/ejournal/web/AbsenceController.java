package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;
import org.example.ejournal.services.AbsenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/absences")
public class AbsenceController {

    private final AbsenceService absenceService;

    public AbsenceController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<String> showCreateAbsencePage() {
        return ResponseEntity.ok("create absence");
    }

    @PostMapping("/create/student/{studentId}/subject/{subjectId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AbsenceDtoResponse> createAbsence(@Valid @RequestBody AbsenceDtoRequest absenceDto,
                                                            @PathVariable long studentId,
                                                            @PathVariable long subjectId) {
        try {
            AbsenceDtoResponse absenceResponse = absenceService.createAbsence(absenceDto, studentId, subjectId);
            return new ResponseEntity<>(absenceResponse, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> showAllAbsencesForStudentAsAdmin(@PathVariable long studentId) {
        try {
            Set<AbsenceDtoResponse> absences = absenceService.getAbsencesForStudent(studentId);
            return new ResponseEntity<>(absences, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> showAllAbsencesForStudentAsStudent() {
        try {
            Set<AbsenceDtoResponse> absences = absenceService.showAllAbsencesForStudentAsStudent();
            return new ResponseEntity<>(absences, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/parent/{studentId}")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> showAllAbsencesForStudentAsParent(@PathVariable long studentId) {
        try {
            Set<AbsenceDtoResponse> absences = absenceService.showAllAbsencesForStudentAsParent(studentId);
            return new ResponseEntity<>(absences, HttpStatus.OK);
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
    public ResponseEntity<?> showAllAbsencesForStudentAsTeacher(@PathVariable long studentId) {
        try {
            Set<AbsenceDtoResponse> absences = absenceService.showAllAbsencesForStudentAsTeacher(studentId);
            return new ResponseEntity<>(absences, HttpStatus.OK);
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
    public ResponseEntity<?> showAllAbsencesForStudentAsHeadmaster(@PathVariable long studentId) {
        try {
            Set<AbsenceDtoResponse> absences = absenceService.showAllAbsencesForStudentAsHeadmaster(studentId);
            return new ResponseEntity<>(absences, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{absenceId}/excuse")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> excuseAbsence(@PathVariable long absenceId) {
        try {
            absenceService.excuseAbsence(absenceId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
