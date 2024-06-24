package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.AbsenceDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.services.AbsenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/absences")
public class AbsenceController {

    private final AbsenceService absenceService;

    public AbsenceController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }

    @PostMapping("/create")
    public ResponseEntity<AbsenceDtoRequest> createAbsence(@Valid @RequestBody AbsenceDtoRequest absenceDto,
                                                           @Valid @RequestBody TeacherDtoRequest teacherDto,
                                                           @Valid @RequestBody StudentDtoRequest studentDto,
                                                           @Valid @RequestBody SubjectDtoRequest subjectDto) {
        AbsenceDtoRequest createdAbsenceDto = absenceService.createAbsence(absenceDto, teacherDto, studentDto, subjectDto);
        return new ResponseEntity<>(createdAbsenceDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{absenceId}")
    public ResponseEntity<Void> deleteAbsence(@PathVariable long absenceId) {
        absenceService.deleteAbsence(absenceId);
        return ResponseEntity.noContent().build();
    }
}
