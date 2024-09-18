package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.entities.Student;
import org.example.ejournal.enums.SubjectType;
import org.example.ejournal.services.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> showCreateGradePage() {
        return ResponseEntity.ok("create grade");
    }

    @PostMapping("/create/subject/{subjectId}/student/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<GradeDtoResponse> createGrade(@PathVariable long subjectId,
                                                        @PathVariable long studentId,
                                                        @Valid @RequestBody GradeDtoRequest gradeDto) {
        try {
            GradeDtoResponse gradeResponse = gradeService.createGrade(gradeDto, subjectId, studentId);
            return new ResponseEntity<>(gradeResponse, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException.Unauthorized e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{gradeId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<GradeDtoResponse> editGrade(@PathVariable long gradeId,
                                                      @Valid @RequestBody GradeDtoRequest gradeDto) {
        try {
            GradeDtoResponse updatedGrade = gradeService.editGrade(gradeId, gradeDto);
            return new ResponseEntity<>(updatedGrade, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException.Unauthorized e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getGradesForStudentAsAdmin(@PathVariable long studentId) {
        try {
            List<GradeDtoResponse> grades = gradeService.getGradesForStudent(studentId);
            return new ResponseEntity<>(grades, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> showAllGradesAsStudent() {
        try {
            List<GradeDtoResponse> grades = gradeService.showAllGradesAsStudent();
            return new ResponseEntity<>(grades, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/parent/student/{studentId}")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> showAllGradesAsParent(@PathVariable long studentId) {
        try {
            List<GradeDtoResponse> grades = gradeService.showAllGradesAsParent(studentId);
            return new ResponseEntity<>(grades, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teacher/student/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> showAllGradesAsTeacher(@PathVariable long studentId) {
        try {
            List<GradeDtoResponse> grades = gradeService.showAllGradesAsTeacher(studentId);
            return new ResponseEntity<>(grades, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/headmaster/student/{studentId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<?> showAllGradesAsHeadmaster(@PathVariable long studentId) {
        try {
            List<GradeDtoResponse> grades = gradeService.showAllGradesAsHeadmaster(studentId);
            return new ResponseEntity<>(grades, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/average-for-school/{schoolId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<BigDecimal> viewAverageGradeForSchool(@PathVariable long schoolId) {
        BigDecimal averageGradeForSchool = gradeService.viewAverageGradeForSchool(schoolId);
        return new ResponseEntity<>(averageGradeForSchool, HttpStatus.OK);
    }

    @GetMapping("/average-for-subject/{schoolId}/{subjectName}/{classNumber}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<BigDecimal> viewAverageGradeForSubject(@PathVariable long schoolId,
                                                                 @PathVariable String subjectName,
                                                                 @PathVariable String classNumber) {
        BigDecimal averageGradeForSubject = gradeService.viewAverageGradeForSubject(schoolId, subjectName, classNumber);
        return new ResponseEntity<>(averageGradeForSubject, HttpStatus.OK);
    }

    @GetMapping("/average-for-teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<BigDecimal> viewAverageGradeForTeacher(@PathVariable long teacherId) {
        BigDecimal averageGradePerTeacher = gradeService.viewAverageGradeForTeacher(teacherId);
        return new ResponseEntity<>(averageGradePerTeacher, HttpStatus.OK);
    }

    @GetMapping("/average-for-student/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT')")
    public ResponseEntity<BigDecimal> viewAverageGradeForStudent(@PathVariable long studentId) {
        BigDecimal averageGradePerStudent = gradeService.viewAverageGradeForStudent(studentId);
        return new ResponseEntity<>(averageGradePerStudent, HttpStatus.OK);
    }

    @GetMapping("/grade-count-in-school-class/{grade}/{schoolClassId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<Integer> viewGradeCountInSchoolClass(@PathVariable BigDecimal grade,
                                                               @PathVariable long schoolClassId) {
        int gradeCount = gradeService.viewGradeCountInSchoolClass(grade, schoolClassId);
        return new ResponseEntity<>(gradeCount, HttpStatus.OK);
    }

    @GetMapping("/grade-count-for-subject/{grade}/{subjectId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<Integer> viewGradeCountForSubject(@PathVariable BigDecimal grade,
                                                            @PathVariable long subjectId) {
        int gradeCount = gradeService.viewGradeCountForSubject(grade, subjectId);
        return new ResponseEntity<>(gradeCount, HttpStatus.OK);
    }

    @GetMapping("/grade-count-for-teacher/{grade}/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<Integer> viewGradeCountForTeacher(@PathVariable BigDecimal grade,
                                                            @PathVariable long teacherId) {
        int gradeCount = gradeService.viewGradeCountForTeacher(grade, teacherId);
        return new ResponseEntity<>(gradeCount, HttpStatus.OK);
    }

    @GetMapping("/grade-count-in-school/{grade}/{schoolId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<Integer> viewGradeCountInSchool(@PathVariable BigDecimal grade,
                                                          @PathVariable long schoolId) {
        int gradeCount = gradeService.viewGradeCountInSchool(grade, schoolId);
        return new ResponseEntity<>(gradeCount, HttpStatus.OK);
    }
}
