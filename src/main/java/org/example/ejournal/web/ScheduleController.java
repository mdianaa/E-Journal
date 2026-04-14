package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.services.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // Create schedule
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<ScheduleDtoResponse> create(@Valid @RequestBody ScheduleDtoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(req));
    }

    // Get schedule for a particular school class
    @GetMapping("/class/{schoolClassId}")
    @PreAuthorize("hasAuthority('ADMIN') " +
            "or (hasAuthority('TEACHER') and @authz.isTeacherOfSchoolClass(authentication, #schoolClassId)) " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchoolClass(authentication, #schoolClassId)) " +
            "or (hasAuthority('STUDENT') and @authz.isStudentInSchoolClass(authentication, #schoolClassId)) " +
            "or (hasAuthority('PARENT') and @authz.isParentOfStudentInSchoolClass(authentication, #schoolClassId))")
    public ResponseEntity<ScheduleDtoResponse> viewForClass(@PathVariable long schoolClassId) {
        return ResponseEntity.ok(scheduleService.viewScheduleForClass(schoolClassId));
    }

    // Get schedule for a particular school class for a particular day
    @GetMapping("/class/{schoolClassId}/day/{day}")
    @PreAuthorize("hasAuthority('ADMIN') " +
            "or (hasAuthority('TEACHER') and @authz.isTeacherOfSchoolClass(authentication, #schoolClassId)) " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchoolClass(authentication, #schoolClassId)) " +
            "or (hasAuthority('STUDENT') and @authz.isStudentInSchoolClass(authentication, #schoolClassId)) " +
            "or (hasAuthority('PARENT') and @authz.isParentOfStudentInSchoolClass(authentication, #schoolClassId))")
    public ResponseEntity<ScheduleDtoResponse> viewForDay(
            @PathVariable long schoolClassId,
            @PathVariable String day) {
        return ResponseEntity.ok(scheduleService.viewScheduleForDayForClass(day, schoolClassId));
    }

    // Delete schedule
    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN') " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchedule(authentication, #scheduleId))")
    public void delete(@PathVariable long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
    }
}