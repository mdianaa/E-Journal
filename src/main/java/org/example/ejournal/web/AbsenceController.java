package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.services.AbsenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/absences")
public class AbsenceController {

    private final AbsenceService absenceService;

    public AbsenceController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String showCreateAbsencePage() {
        return "create absence";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AbsenceDtoRequest> createAbsence(@RequestBody AbsenceDtoRequest absence,
                                                           @RequestBody TeacherDtoRequest teacherDto,
                                                           @RequestBody StudentDtoRequest studentDto,
                                                           @RequestBody SubjectDtoRequest subjectDto) {
        AbsenceDtoRequest createdAbsence = absenceService.createAbsence(absence, teacherDto, studentDto, subjectDto);
        return ResponseEntity.ok(createdAbsence);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteAbsence(@PathVariable long id) {
        absenceService.deleteAbsence(id);
        return ResponseEntity.noContent().build();
    }
}

