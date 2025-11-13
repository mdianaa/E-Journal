package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDtoResponse> create(@Valid @RequestBody ScheduleDtoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(req));
    }

    @GetMapping("/class/{schoolClassId}")
    public ResponseEntity<ScheduleDtoResponse> viewForClass(@PathVariable long schoolClassId) {
        return ResponseEntity.ok(scheduleService.viewScheduleForClass(schoolClassId));
    }

    @GetMapping("/class/{schoolClassId}/day/{day}")
    public ResponseEntity<ScheduleDtoResponse> viewForDay(
            @PathVariable long schoolClassId,
            @PathVariable String day) {
        return ResponseEntity.ok(scheduleService.viewScheduleForDayForClass(day, schoolClassId));
    }

    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
    }
}