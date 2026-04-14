package org.example.ejournal.web;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.services.GradeAnalyticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/grade/analytics")
@RequiredArgsConstructor
public class GradeAnalyticsController {

    private final GradeAnalyticsService analytics;

    // Get average grade for a specific subject in a specific school for a specific class
    @GetMapping("/avg/subject")
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchoolClass(authentication, schoolClassId)")
    public BigDecimal avgForSubjectType(@RequestParam long schoolId,
                                        @RequestParam String subject,
                                        @RequestParam Long schoolClassId) {
        return analytics.viewAverageGradeForSubject(schoolId, subject, schoolClassId);
    }

    // Get average grade given by a specific teacher
    @GetMapping("/avg/teacher/{teacherId}")
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or hasAuthority('HEADMASTER') and @authz.isHeadmasterOfTeacher(authentication, #teacherId)")
    public BigDecimal avgForTeacher(@PathVariable long teacherId) {
        return analytics.viewAverageGradeForTeacher(teacherId);
    }

    // Get average grade for a specific student
    @GetMapping("/avg/student/{studentId}")
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or hasAuthority('HEADMASTER') and @authz.isHeadmasterOfStudent(authentication, #studentId)")
    public BigDecimal avgForStudent(@PathVariable long studentId) {
        return analytics.viewAverageGradeForStudent(studentId);
    }

    // Get average grade in a specific school
    @GetMapping("/avg/school/{schoolId}")
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchool(authentication, #schoolId)")
    public BigDecimal avgForSchool(@PathVariable long schoolId) {
        return analytics.viewAverageGradeForSchool(schoolId);
    }

    // Get the count of a specific grade in a specific class
    @GetMapping("/count/class/{schoolClassId}")
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchoolClass(authentication, #schoolClassId)")
    public int countInClass(@RequestParam BigDecimal grade, @PathVariable long schoolClassId) {
        return analytics.viewGradeCountInSchoolClass(grade, schoolClassId);
    }

    // Get the count of a specific grade for a specific subject
    @GetMapping("/count/subject/{subjectId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public int countForSubject(@RequestParam BigDecimal grade, @PathVariable long subjectId) {
        return analytics.viewGradeCountForSubject(grade, subjectId);
    }

    // Get the count of a specific grade given by a specific teacher
    @GetMapping("/count/teacher/{teacherId}")
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or hasAuthority('HEADMASTER') and @authz.isHeadmasterOfTeacher(authentication, #teacherId)")
    public int countForTeacher(@RequestParam BigDecimal grade, @PathVariable long teacherId) {
        return analytics.viewGradeCountForTeacher(grade, teacherId);
    }

    // Get the count of a specific grade in a specific school
    @GetMapping("/count/school/{schoolId}")
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchool(authentication, #schoolId)")
    public int countInSchool(@RequestParam BigDecimal grade, @PathVariable long schoolId) {
        return analytics.viewGradeCountInSchool(grade, schoolId);
    }
}
