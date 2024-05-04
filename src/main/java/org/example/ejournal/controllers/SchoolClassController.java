package org.example.ejournal.controllers;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.services.SchoolClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/school-classes")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    public SchoolClassController(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }

    @PostMapping("/create")
    public ResponseEntity<SchoolClassDtoRequest> createClass(@Valid @RequestBody SchoolClassDtoRequest schoolClassDto,
                                                             @Valid @RequestBody TeacherDtoRequest headTeacherDto,
                                                             @Valid @RequestBody SchoolDtoRequest schoolDto) {
        SchoolClassDtoRequest createdClassDto = schoolClassService.createClass(schoolClassDto, headTeacherDto, schoolDto);
        return new ResponseEntity<>(createdClassDto, HttpStatus.CREATED);
    }

    @PutMapping("/changeHeadTeacher/{classId}")
    public ResponseEntity<SchoolClassDtoRequest> changeHeadTeacher(@PathVariable long classId,
                                                                   @Valid @RequestBody TeacherDtoRequest headTeacherDto) {
        SchoolClassDtoRequest changedClassDto = schoolClassService.changeHeadTeacher(classId, headTeacherDto);
        if (changedClassDto != null) {
            return ResponseEntity.ok(changedClassDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{classId}")
    public ResponseEntity<Void> deleteClass(@PathVariable long classId) {
        schoolClassService.deleteClass(classId);
        return ResponseEntity.noContent().build();
    }
}
