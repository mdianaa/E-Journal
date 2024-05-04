package org.example.ejournal.controllers;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.models.Grade;
import org.example.ejournal.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@RequestBody StudentDtoRequest studentDto,
                                           @RequestParam("schoolName") String schoolName,
                                           @RequestParam("className") String className,
                                           @RequestBody ParentDtoRequest parentDto) {
        try {
            SchoolDtoRequest schoolDto = new SchoolDtoRequest();
            schoolDto.setName(schoolName);

            SchoolClassDtoRequest schoolClassDto = new SchoolClassDtoRequest();
            schoolClassDto.setClassName(className);

            StudentDtoRequest createdStudent = studentService.createStudent(studentDto, schoolDto, schoolClassDto, parentDto);

            return ResponseEntity.ok(createdStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating student: " + e.getMessage());
        }
    }

    @PutMapping("/{studentId}/edit")
    public ResponseEntity<?> editStudent(@PathVariable("studentId") long studentId,
                                         @RequestBody StudentDtoRequest studentDto) {
        try {
            StudentDtoRequest editedStudent = studentService.editStudent(studentId, studentDto);
            return ResponseEntity.ok(editedStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error editing student: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/grades")
    public ResponseEntity<?> showAllGradesForStudent(@PathVariable("studentId") long studentId,
                                                     @RequestBody SubjectDtoRequest subjectDto) {
        try {
            Set<Grade> grades = studentService.showAllGradesForSubject(studentId, subjectDto);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching grades: " + e.getMessage());
        }

    }
}
