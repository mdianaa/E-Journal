package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.*;
import org.example.ejournal.services.StudentService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<?> createStudent(@RequestBody StudentDtoRequest studentDto) {
        try {
            StudentDtoResponse createdStudent = studentService.createStudent(studentDto);
            return ResponseEntity.ok(createdStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating student: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/school-class/{schoolClassId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<?/*Set<StudentDtoResponse>*/> showAllStudentsInSchoolClass(@PathVariable long schoolClassId){
        try{
            Set<StudentDtoResponse> studentDtoResponses = studentService.showAllStudentsInClass(schoolClassId);
            return ResponseEntity.ok(studentDtoResponses);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/school")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<?> showAllStudentsInSchool() {
        try {
            Set<StudentDtoResponse> studentDtoResponses = studentService.showAllStudentsInSchoolAsHeadmaster();
            return ResponseEntity.ok(studentDtoResponses);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?/*Set<StudentDtoResponse>*/> showAllStudentsInSchool(@PathVariable long schoolId) {
        try {
            Set<StudentDtoResponse> students = studentService.showAllStudentsInSchool(schoolId);
            return ResponseEntity.ok(students);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    
    
    @PutMapping("/edit/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editStudent(@PathVariable("studentId") long studentId,
                                         @RequestBody StudentDtoRequest studentDto) {
        try {
            StudentDtoResponse editedStudent = studentService.editStudent(studentId, studentDto);
            return ResponseEntity.ok(editedStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error editing student: " + e.getMessage());
        }
    }

    @GetMapping("/{username}/grades")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<?> showAllGradesForStudent(@PathVariable("username") String username,
                                                     @RequestBody SubjectDtoRequest subjectDto) {
        if (isCurrentUser(username)) {
            try {
                List<GradeDtoResponse> grades = studentService.showAllGradesForSubject(username, subjectDto);
                return ResponseEntity.ok(grades);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching grades: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @GetMapping("/{username}/absences")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<?> showAllAbsencesForStudent(@PathVariable String username) {
        if (isCurrentUser(username)) {
            try {
                Set<AbsenceDtoResponse> absences = studentService.showAllAbsencesForStudent(username);
                return ResponseEntity.ok(absences);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching absences: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @GetMapping("/{username}/bad-notes")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<?> showAllBadNotesForStudent(@PathVariable String username) {
        if (isCurrentUser(username)) {
            try {
                List<BadNoteDtoResponse> badNotes = studentService.showAllBadNotesForStudent(username);
                return ResponseEntity.ok(badNotes);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching bad notes: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<StudentDtoResponse> viewStudent(@PathVariable String username) {
        StudentDtoResponse student = studentService.viewStudent(username);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }


    //todo
    // add parent login
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT')")
    public ResponseEntity<StudentDtoResponse> viewStudent() {
        try {
            // Extract the username from the security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();

            // Fetch the student details using the extracted username
            StudentDtoResponse student = studentService.viewStudent(loggedInUsername);
            return ResponseEntity.ok(student);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(null);
        }
    }

//    @GetMapping("schedule/{day}/{semester}/{schoolClass}")
//    public ResponseEntity<List<ScheduleDtoResponse>> viewScheduleForDay(@PathVariable String day,
//                                                                        @PathVariable String semester,
//                                                                        @PathVariable String schoolClass) {
//        List<ScheduleDtoResponse> schedule = studentService.viewScheduleForDay(day, semester, schoolClass);
//        return ResponseEntity.ok(schedule);
//    }


    
    
    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> withdrawStudent(@PathVariable long studentId) {
        studentService.withdrawStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    private boolean isCurrentUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername().equals(username);
        }
        return false;
    }
}
