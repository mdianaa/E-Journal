package org.example.ejournal.controllers;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;
import org.example.ejournal.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schools")
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping("/create")
    public ResponseEntity<SchoolDtoRequest> createSchool(@RequestBody SchoolDtoRequest schoolDto) {
        try {
            SchoolDtoRequest createdSchool = schoolService.createSchool(schoolDto);
            return new ResponseEntity<>(createdSchool, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolDtoResponse> viewSchoolInfo(@PathVariable long schoolId) {
        try {
            SchoolDtoResponse schoolInfo = schoolService.viewSchoolInfo(schoolId);
            return new ResponseEntity<>(schoolInfo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{schoolId}")
    public ResponseEntity<Void> deleteSchool(@PathVariable long schoolId) {
        try {
            schoolService.deleteSchool(schoolId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
