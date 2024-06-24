package org.example.ejournal.web;

import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/create")
    public ResponseEntity<ScheduleDtoRequest> createSchedule(@RequestBody ScheduleDtoRequest scheduleDto,
                                                             @RequestBody SchoolClassDtoRequest schoolClassDto,
                                                             @RequestBody SubjectDtoRequest subjectDtoRequest) {
        try {
            ScheduleDtoRequest createdSchedule = scheduleService.createSchedule(scheduleDto, schoolClassDto, subjectDtoRequest);
            return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{day}/{schoolClass}/{semester}")
    public ResponseEntity<ScheduleDtoResponse> viewScheduleForDay(@PathVariable String day,
                                                                  @PathVariable String schoolClass,
                                                                  @PathVariable String semester) {
        try {
            ScheduleDtoResponse scheduleResponse = scheduleService.viewScheduleForDay(day, schoolClass, semester);
            return new ResponseEntity<>(scheduleResponse, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable long scheduleId) {
        try {
            scheduleService.deleteSchedule(scheduleId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
