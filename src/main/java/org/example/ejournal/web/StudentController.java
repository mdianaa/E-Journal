package org.example.ejournal.web;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateStudentPage() {
        return "create student";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditStudentPage() {
        return "edit student";
    }

    @PutMapping("/edit/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
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
            List<GradeDtoResponse> grades = studentService.showAllGradesForSubject(studentId, subjectDto);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching grades: " + e.getMessage());
        }

    }
}
