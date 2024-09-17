package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.services.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<String> showCreateSubjectPage() {
        return ResponseEntity.ok("create subject");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<SubjectDtoResponse> createSubject(@Valid @RequestBody SubjectDtoRequest subjectDto) {
        try {
            SubjectDtoResponse createdSubject = subjectService.createSubject(subjectDto);
            return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/viewAll/{schoolId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<Set<SubjectDtoResponse>> viewAllSubjectsInSchool(@PathVariable long schoolId) {
        try {
            Set<SubjectDtoResponse> subjects = subjectService.viewAllSubjectsInSchool(schoolId);
            return new ResponseEntity<>(subjects, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
