package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.TeacherScheduleDtoRequest;
import org.example.ejournal.dtos.response.TeacherScheduleDtoResponse;
import org.example.ejournal.dtos.response.TeacherScheduleSlotDtoResponse;
import org.example.ejournal.services.TeacherScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/teacher-schedule")
@RequiredArgsConstructor
public class TeacherScheduleController {

    private final TeacherScheduleService teacherScheduleService;

    // Create or replace full schedule for a teacher (semester + shift)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<TeacherScheduleDtoResponse> createOrReplaceSchedule(@RequestBody @Valid TeacherScheduleDtoRequest request) {
        TeacherScheduleDtoResponse response = teacherScheduleService.createSchedule(request);
        return ResponseEntity.ok(response);
    }

    // Get full weekly schedule for a particular teacher
    @GetMapping("/{teacherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<TeacherScheduleDtoResponse> getSchedule(
            @PathVariable Long teacherId,
            @RequestParam String semester,
            @RequestParam String shift
    ) {
        TeacherScheduleDtoResponse response =
                teacherScheduleService.getTeacherSchedule(teacherId, semester, shift);
        return ResponseEntity.ok(response);
    }

    // Get schedule only for one day for a particular teacher
    @GetMapping("/{teacherId}/day")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<Set<TeacherScheduleSlotDtoResponse>> getDailySchedule(
            @PathVariable Long teacherId,
            @RequestParam String semester,
            @RequestParam String shift,
            @RequestParam String day
    ) {
        Set<TeacherScheduleSlotDtoResponse> response =
                teacherScheduleService.getDailySchedule(teacherId, semester, shift, day);
        return ResponseEntity.ok(response);
    }

    // Delete whole schedule (for semester + shift) for a particular teacher
    @DeleteMapping("/{teacherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long teacherId,
            @RequestParam String semester,
            @RequestParam String shift
    ) {
        teacherScheduleService.deleteSchedule(teacherId, semester, shift);
        return ResponseEntity.noContent().build();
    }
}
