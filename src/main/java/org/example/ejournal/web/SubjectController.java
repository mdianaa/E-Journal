package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.services.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/create")
    public ResponseEntity<SubjectDtoRequest> createSubject(@Valid @RequestBody SubjectDtoRequest subjectDto,
                                                           @Valid @RequestBody SchoolDtoRequest schoolDto) {
        SubjectDtoRequest createdSubjectDto = subjectService.createSubject(subjectDto, schoolDto);
        return new ResponseEntity<>(createdSubjectDto, HttpStatus.CREATED);
    }

    @GetMapping("/viewAll/{schoolId}")
    public ResponseEntity<Set<SubjectDtoResponse>> viewAllSubjectsInSchool(@PathVariable long schoolId) {
        Set<SubjectDtoResponse> subjects = subjectService.viewAllSubjectsInSchool(schoolId);
        return ResponseEntity.ok(subjects);
    }

    @DeleteMapping("/delete/{schoolId}/{subjectId}")
    public ResponseEntity<Void> deleteSubject(@PathVariable long schoolId, @PathVariable long subjectId) {
        subjectService.deleteSubject(schoolId, subjectId);
        return ResponseEntity.noContent().build();
    }
}
