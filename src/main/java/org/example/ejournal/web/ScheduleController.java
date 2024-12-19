package org.example.ejournal.web;

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
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HEADMASTER')")
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleDtoRequest scheduleDtoRequest){
        try{
            ScheduleDtoResponse scheduleDtoResponse = scheduleService.createSchedule(scheduleDtoRequest);
            return ResponseEntity.ok(scheduleDtoResponse);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
//    @PostMapping("/create")
//    @PreAuthorize("hasRole('TEACHER')")
//    public ResponseEntity<ScheduleDtoResponse> createSchedule(@RequestBody ScheduleDtoRequest scheduleDto,
//                                                              @RequestBody SchoolClassDtoRequest schoolClassDto,
//                                                              @RequestBody SubjectDtoRequest subjectDtoRequest) {
//        try {
//            ScheduleDtoResponse createdSchedule = scheduleService.createSchedule(scheduleDto, schoolClassDto, subjectDtoRequest);
//            return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/{scheduleId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<Void> deleteSchedule(@PathVariable long scheduleId) {
        try {
            scheduleService.deleteSchedule(scheduleId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
