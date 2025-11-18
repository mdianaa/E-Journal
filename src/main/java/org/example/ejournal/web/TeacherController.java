package org.example.ejournal.web;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.services.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // Change subjects of a particular teacher
    @PatchMapping("/{teacherId}/subjects")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<TeacherDtoResponse> changeSubjects(
            @PathVariable long teacherId,
            @RequestBody Set<SubjectDtoRequest> subjectDtos) {
        return ResponseEntity.ok(teacherService.changeSubjects(teacherId, subjectDtos));
    }

    // Remove the headteacher title of a particular headteacher
    @PostMapping("/{teacherId}/demote-head")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<TeacherDtoResponse> removeHeadTitle(@PathVariable long teacherId) {
        return ResponseEntity.ok(teacherService.removeHeadTeacherTitle(teacherId));
    }

    // Get a particular teacher
    @GetMapping("/{teacherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'PARENT', 'STUDENT', 'TEACHER')")
    public ResponseEntity<TeacherDtoResponse> view(@PathVariable long teacherId) {
        return ResponseEntity.ok(teacherService.viewTeacher(teacherId));
    }

    // Get a particular headteacher
    @GetMapping("/{teacherId}/head-of/{classId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'PARENT', 'STUDENT', 'TEACHER')")
    public ResponseEntity<TeacherDtoResponse> viewHeadOf(@PathVariable long teacherId, @PathVariable long classId) {
        return ResponseEntity.ok(teacherService.viewHeadTeacher(teacherId, classId));
    }

    // Get all the headteachers in a particular school
    @GetMapping("/school/{schoolId}/heads")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<Set<TeacherDtoResponse>> listHeads(@PathVariable long schoolId) {
        return ResponseEntity.ok(teacherService.viewAllHeadTeachersInSchool(schoolId));
    }

    // Get all the teachers in a particular school
    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'PARENT', 'STUDENT', 'TEACHER')")
    public ResponseEntity<Set<TeacherDtoResponse>> listAll(@PathVariable long schoolId) {
        return ResponseEntity.ok(teacherService.viewAllTeachersInSchool(schoolId));
    }

    // Delete a teacher
    @DeleteMapping("/{teacherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void delete(@PathVariable long teacherId) {
        teacherService.deleteTeacher(teacherId);
    }
}
