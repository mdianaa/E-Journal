package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.response.*;
import org.example.ejournal.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    // TODO use id?
    // Get a particular student
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PARENT', 'TEACHER', 'STUDENT', 'HEADMASTER')")
    public ResponseEntity<StudentDtoResponse> me(@Valid @PathVariable String username) {
        return ResponseEntity.ok(studentService.viewStudent(username));
    }

    // Get all students in a particular school
    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<Set<StudentDtoResponse>> listBySchool(@PathVariable long schoolId) {
        return ResponseEntity.ok(studentService.viewAllStudentsInSchool(schoolId));
    }

    // Get all students in a particular class
    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'TEACHER')")
    public ResponseEntity<Set<StudentDtoResponse>> listByClass(@PathVariable long classId) {
        return ResponseEntity.ok(studentService.viewAllStudentsInClass(classId));
    }

    // Withdraw a student from school without deleting him/her from db
    @PostMapping("/{studentId}/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public void withdraw(@PathVariable long studentId) {
        studentService.withdrawStudent(studentId);
    }

    // Delete a student
    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void delete(@PathVariable long studentId) {
        studentService.deleteStudent(studentId);
    }
}