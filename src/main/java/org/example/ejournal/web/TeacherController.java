package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.services.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showCreateTeacherPage() {
        return ResponseEntity.ok("create teacher");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherDtoResponse> createTeacher(@Valid @RequestBody TeacherDtoRequest teacherDto,
                                                            @Valid @RequestBody SchoolDtoRequest schoolDto,
                                                            @Valid @RequestBody Set<SubjectDtoRequest> subjectDtos,
                                                            @Valid @RequestBody UserRegisterDtoRequest userRegisterDtoRequest) {
        TeacherDtoResponse createdTeacherDto = teacherService.createTeacher(teacherDto, schoolDto, subjectDtos, userRegisterDtoRequest);
        return new ResponseEntity<>(createdTeacherDto, HttpStatus.CREATED);
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showEditTeacherPage() {
        return ResponseEntity.ok("edit teacher");
    }

    @PutMapping("/edit/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherDtoResponse> editTeacher(@PathVariable long teacherId,
                                                          @Valid @RequestBody TeacherDtoRequest teacherDto) {
        TeacherDtoResponse editedTeacherDto = teacherService.editTeacher(teacherId, teacherDto);
        if (editedTeacherDto != null) {
            return ResponseEntity.ok(editedTeacherDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/changeSubjects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showChangeSubjectsPage() {
        return ResponseEntity.ok("change subjects");
    }

    @PutMapping("/changeSubjects/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherDtoResponse> changeSubjects(@PathVariable long teacherId,
                                                             @Valid @RequestBody Set<SubjectDtoRequest> subjectDtos) {
        TeacherDtoResponse updatedTeacherDto = teacherService.changeSubjects(teacherId, subjectDtos);
        if (updatedTeacherDto != null) {
            return ResponseEntity.ok(updatedTeacherDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/removeHeadTeacherTitle/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherDtoResponse> removeHeadTeacherTitle(@PathVariable long teacherId) {
        TeacherDtoResponse updatedTeacherDto = teacherService.removeHeadTeacherTitle(teacherId);
        if (updatedTeacherDto != null) {
            return ResponseEntity.ok(updatedTeacherDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<TeacherDtoResponse> viewTeacher(@PathVariable long teacherId) {
        TeacherDtoResponse teacher = teacherService.viewTeacher(teacherId);
        if (teacher != null) {
            return ResponseEntity.ok(teacher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/viewAll/{schoolId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<Set<TeacherDtoResponse>> viewAllTeachersInSchool(@PathVariable long schoolId) {
        Set<TeacherDtoResponse> teachers = teacherService.viewAllTeachersInSchool(schoolId);
        return ResponseEntity.ok(teachers);
    }

    @DeleteMapping("/delete/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.noContent().build();
    }
}
