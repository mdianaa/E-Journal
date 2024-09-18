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
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<?> createSubject(@Valid @RequestBody SubjectDtoRequest subjectDto) {
        try {
            SubjectDtoResponse createdSubject = subjectService.createSubject(subjectDto);
            return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/viewAll/{schoolId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> viewAllSubjectsInSchool(@PathVariable long schoolId) {
        try {
            Set<SubjectDtoResponse> subjects = subjectService.viewAllSubjectsInSchool(schoolId);
            return new ResponseEntity<>(subjects, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/viewAll")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<?> viewAllSubjectsInSchoolAsHeadmaster() {
        try {
            Set<SubjectDtoResponse> subjects = subjectService.viewAllSubjectsInSchool();
            return new ResponseEntity<>(subjects, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
