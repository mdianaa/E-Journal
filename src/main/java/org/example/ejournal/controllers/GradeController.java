package org.example.ejournal.controllers;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.services.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/create")
    public ResponseEntity<GradeDtoRequest> createGrade(@Valid @RequestBody GradeDtoRequest gradeDto,
                                                       @Valid @RequestBody TeacherDtoRequest teacherDto,
                                                       @Valid @RequestBody SubjectDtoRequest subjectDto,
                                                       @Valid @RequestBody StudentDtoRequest studentDto) {
        GradeDtoRequest createdGradeDto = gradeService.createGrade(gradeDto, teacherDto, subjectDto, studentDto);
        return new ResponseEntity<>(createdGradeDto, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{gradeId}")
    public ResponseEntity<GradeDtoRequest> editGrade(@PathVariable long gradeId,
                                                     @Valid @RequestBody GradeDtoRequest gradeDto) {
        GradeDtoRequest editedGradeDto = gradeService.editGrade(gradeId, gradeDto);
        if (editedGradeDto != null) {
            return ResponseEntity.ok(editedGradeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
