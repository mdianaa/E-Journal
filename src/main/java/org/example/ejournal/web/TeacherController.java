package org.example.ejournal.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
import org.example.ejournal.services.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@ControllerAdvice
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','HEADMASTER')")
    public ResponseEntity<?> createTeacher(@RequestBody @NotNull TeacherDtoRequest teacherDtoRequest) {
        try {
            TeacherDtoResponse createdTeacherDto = teacherService.createTeacher(teacherDtoRequest);
       
            return new ResponseEntity<>(createdTeacherDto, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("School not found.", HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Username already exists.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

//    @GetMapping("schedule/{day}/{semester}/{schoolClass}")
//    public ResponseEntity<List<ScheduleDtoResponse>> viewScheduleForDay(@PathVariable String day,
//                                                                       @PathVariable String semester,
//                                                                       @PathVariable String schoolClass) {
//        List<ScheduleDtoResponse> schedule = teacherService.viewScheduleForDay(day, semester, schoolClass);
//        return ResponseEntity.ok(schedule);
//    }
    
    @GetMapping("/viewAll")
    @PreAuthorize("hasAnyRole('HEADMASTER')")
    public ResponseEntity<?/*Set<TeacherDtoResponse>*/> viewAllTeachersInSchoolAsHeadmaster() {
        try{
            Set<TeacherDtoResponse> teachers = teacherService.viewAllTeachersAsHeadmaster();
            return ResponseEntity.ok(teachers);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/viewAllHeadTeacher")
    @PreAuthorize("hasAnyRole('HEADMASTER')")
    public ResponseEntity<?/*Set<TeacherDtoResponse>*/> viewAllHeadTeachersInSchoolAsHeadmaster() {
        try{
            Set<TeacherDtoResponse> teachers = teacherService.viewHeadTeachersAsHeadmaster();
            return ResponseEntity.ok(teachers);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/viewAll/{schoolId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?/*Set<TeacherDtoResponse>*/> viewAllTeachersInSchool(@PathVariable long schoolId) {
        try{
            Set<TeacherDtoResponse> teachers = teacherService.viewAllTeachersInSchool(schoolId);
            return ResponseEntity.ok(teachers);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/viewAllHeadTeacher/{schoolId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?/*Set<TeacherDtoResponse>*/> viewAllHeadTeachersInSchool(@PathVariable long schoolId) {
        try{
            Set<TeacherDtoResponse> teachers = teacherService.viewAllHeadTeachersInSchool(schoolId);
            return ResponseEntity.ok(teachers);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/delete/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.noContent().build();
    }
    
    
    
    //    @PutMapping("/changeSubjects/{teacherId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<TeacherDtoResponse> changeSubjects(@PathVariable long teacherId,
//                                                             @Valid @RequestBody Set<SubjectDtoRequest> subjectDtos) {
//        TeacherDtoResponse updatedTeacherDto = teacherService.changeSubjects(teacherId, subjectDtos);
//        if (updatedTeacherDto != null) {
//            return Respon seEntity.ok(updatedTeacherDto);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}

