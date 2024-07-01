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
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String showCreateAbsencePage() {
        return "create absence";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<AbsenceDtoResponse> createAbsence(@RequestBody AbsenceDtoRequest absence,
                                                           @RequestBody TeacherDtoRequest teacherDto,
                                                           @RequestBody StudentDtoRequest studentDto,
                                                           @RequestBody SubjectDtoRequest subjectDto) {
        AbsenceDtoResponse createdAbsence = absenceService.createAbsence(absence, teacherDto, studentDto, subjectDto);
        return ResponseEntity.ok(createdAbsence);
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @PatchMapping("/{absenceId}/excuse")
    public ResponseEntity<Void> excuseAbsence(@PathVariable long absenceId) {
        absenceService.excuseAbsence(absenceId);
        return ResponseEntity.noContent().build();
    }
}

