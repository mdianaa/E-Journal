package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.enums.SubjectType;
import org.example.ejournal.services.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String showCreateGradePage() {
        return "create grade";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<GradeDtoRequest> createGrade(@Valid @RequestBody GradeDtoRequest gradeDto,
                                                       @Valid @RequestBody TeacherDtoRequest teacherDto,
                                                       @Valid @RequestBody SubjectDtoRequest subjectDto,
                                                       @Valid @RequestBody StudentDtoRequest studentDto) {
        GradeDtoRequest createdGradeDto = gradeService.createGrade(gradeDto, teacherDto, subjectDto, studentDto);
        return new ResponseEntity<>(createdGradeDto, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{gradeId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<GradeDtoRequest> editGrade(@PathVariable long gradeId,
                                                     @Valid @RequestBody GradeDtoRequest gradeDto) {
        GradeDtoRequest editedGradeDto = gradeService.editGrade(gradeId, gradeDto);
        if (editedGradeDto != null) {
            return ResponseEntity.ok(editedGradeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/average-for-school/{schoolId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<BigDecimal> viewAverageGradeForSchool(@PathVariable long schoolId) {
        BigDecimal averageGradeForSchool = gradeService.viewAverageGradeForSchool(schoolId);
        return new ResponseEntity<>(averageGradeForSchool, HttpStatus.OK);
    }

    @GetMapping("/average-for-subject/{schoolId}/{subjectType}/{classNumber}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<BigDecimal> viewAverageGradeForSubject(@PathVariable long schoolId, @PathVariable SubjectType subjectType, @PathVariable String classNumber) {
        BigDecimal averageGradeForSubject = gradeService.viewAverageGradeForSubject(schoolId, subjectType, classNumber);
        return new ResponseEntity<>(averageGradeForSubject, HttpStatus.OK);
    }

    @GetMapping("/average-for-teacher/{teacherId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<BigDecimal> viewAverageGradeForTeacher(@PathVariable long teacherId) {
        BigDecimal averageGradePerTeacher = gradeService.viewAverageGradeForTeacher(teacherId);
        return new ResponseEntity<>(averageGradePerTeacher, HttpStatus.OK);
    }

    @GetMapping("/average-for-student/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<BigDecimal> viewAverageGradeForStudent(@PathVariable long studentId) {
        BigDecimal averageGradePerStudent = gradeService.viewAverageGradeForStudent(studentId);
        return new ResponseEntity<>(averageGradePerStudent, HttpStatus.OK);
    }

    @GetMapping("/grade-count-in-school-class/{grade}/{schoolClassId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Integer> viewGradeCountInSchoolClass(@PathVariable BigDecimal grade, @PathVariable long schoolClassId) {
        int gradeCount = gradeService.viewGradeCountInSchoolClass(grade, schoolClassId);
        return new ResponseEntity<>(gradeCount, HttpStatus.OK);
    }

    @GetMapping("/grade-count-for-subject/{grade}/{subjectId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Integer> viewGradeCountForSubject(@PathVariable BigDecimal grade, @PathVariable long subjectId) {
        int gradeCount = gradeService.viewGradeCountForSubject(grade, subjectId);
        return new ResponseEntity<>(gradeCount, HttpStatus.OK);
    }

    @GetMapping("/grade-count-for-teacher/{grade}/{teacherId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Integer> viewGradeCountForTeacher(@PathVariable BigDecimal grade, @PathVariable long teacherId) {
        int gradeCount = gradeService.viewGradeCountForTeacher(grade, teacherId);
        return new ResponseEntity<>(gradeCount, HttpStatus.OK);
    }

    @GetMapping("/grade-count-in-school/{grade}/{schoolId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Integer> viewGradeCountInSchool(@PathVariable BigDecimal grade, @PathVariable long schoolId) {
        int gradeCount = gradeService.viewGradeCountInSchool(grade, schoolId);
        return new ResponseEntity<>(gradeCount, HttpStatus.OK);
    }
}
