package org.example.ejournal.web;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.services.GradeAnalyticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/grade/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
public class GradeAnalyticsController {

    private final GradeAnalyticsService analytics;

    // Get average grade for a specific subject in a specific school for a specific class
    @GetMapping("/avg/subject")
    public BigDecimal avgForSubjectType(@RequestParam long schoolId,
                                        @RequestParam String subject,
                                        @RequestParam String classNumber) {
        return analytics.viewAverageGradeForSubject(schoolId, subject, classNumber);
    }

    // Get average grade given by a specific teacher
    @GetMapping("/avg/teacher/{teacherId}")
    public BigDecimal avgForTeacher(@PathVariable long teacherId) {
        return analytics.viewAverageGradeForTeacher(teacherId);
    }

    // Get average grade for a specific student
    @GetMapping("/avg/student/{studentId}")
    public BigDecimal avgForStudent(@PathVariable long studentId) {
        return analytics.viewAverageGradeForStudent(studentId);
    }

    // Get average grade in a specific school
    @GetMapping("/avg/school/{schoolId}")
    public BigDecimal avgForSchool(@PathVariable long schoolId) {
        return analytics.viewAverageGradeForSchool(schoolId);
    }

    // Get the count of a specific grade in a specific class
    // TODO state school
    @GetMapping("/count/class/{schoolClassId}")
    public int countInClass(@RequestParam BigDecimal grade, @PathVariable long schoolClassId) {
        return analytics.viewGradeCountInSchoolClass(grade, schoolClassId);
    }

    // Get the count of a specific grade for a specific subject
    // TODO state school
    @GetMapping("/count/subject/{subjectId}")
    public int countForSubject(@RequestParam BigDecimal grade, @PathVariable long subjectId) {
        return analytics.viewGradeCountForSubject(grade, subjectId);
    }

    // Get the count of a specific grade given by a specific teacher
    @GetMapping("/count/teacher/{teacherId}")
    public int countForTeacher(@RequestParam BigDecimal grade, @PathVariable long teacherId) {
        return analytics.viewGradeCountForTeacher(grade, teacherId);
    }

    // Get the count of a specific grade in a specific school
    @GetMapping("/count/school/{schoolId}")
    public int countInSchool(@RequestParam BigDecimal grade, @PathVariable long schoolId) {
        return analytics.viewGradeCountInSchool(grade, schoolId);
    }
}
