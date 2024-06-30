package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.AbsenceDtoResponse;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.dtos.response.StudentDtoResponse;
import org.example.ejournal.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
                                           @RequestBody ParentDtoRequest parentDto,
                                           @Valid @RequestBody UserRegisterDtoRequest userRegisterDtoRequest) {
        try {
            SchoolDtoRequest schoolDto = new SchoolDtoRequest();
            schoolDto.setName(schoolName);

            SchoolClassDtoRequest schoolClassDto = new SchoolClassDtoRequest();
            schoolClassDto.setClassName(className);

            StudentDtoRequest createdStudent = studentService.createStudent(studentDto, schoolDto, schoolClassDto, parentDto, userRegisterDtoRequest);

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
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<?> showAllGradesForStudent(@PathVariable("studentId") long studentId,
                                                     @RequestBody SubjectDtoRequest subjectDto) {
        try {
            List<GradeDtoResponse> grades = studentService.showAllGradesForSubject(studentId, subjectDto);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching grades: " + e.getMessage());
        }

    }

    @GetMapping("/{studentId}/absences")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public Set<AbsenceDtoResponse> showAllAbsencesForStudent(@PathVariable long studentId) {
        return studentService.showAllAbsencesForStudent(studentId);
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public StudentDtoResponse viewStudent(@PathVariable long studentId) {
        return studentService.viewStudent(studentId);
    }

    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public Set<StudentDtoResponse> showAllStudentsInSchool(@PathVariable long schoolId) {
        return studentService.showAllStudentsInSchool(schoolId);
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void withdrawStudent(@PathVariable long studentId) {
        studentService.withdrawStudent(studentId);
    }
}
