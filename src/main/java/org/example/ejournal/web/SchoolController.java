package org.example.ejournal.web;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;
import org.example.ejournal.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schools")
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showCreateSchoolPage() {
        return ResponseEntity.ok("create school");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSchool(@RequestBody SchoolDtoRequest schoolDto) {
        try {
            SchoolDtoResponse createdSchool = schoolService.createSchool(schoolDto);
            return new ResponseEntity<>(createdSchool, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Handle specific cases where the input might be invalid
            return new ResponseEntity<>("Invalid school data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log the error for debugging (optional)
            // logger.error("Error creating school", e);
            // Return a generic internal server error
            return new ResponseEntity<>("An error occurred while creating the school.: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{schoolId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<SchoolDtoResponse> viewSchoolInfo(@PathVariable long schoolId) {
        try {
            SchoolDtoResponse schoolInfo = schoolService.viewSchoolInfo(schoolId);
            return new ResponseEntity<>(schoolInfo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> viewSchools(){
        try{
            List<SchoolDtoResponse> schoolDtoResponseList = schoolService.viewAllSchoolsInfo();
            return new ResponseEntity<>(schoolDtoResponseList,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{schoolId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSchool(@PathVariable long schoolId) {
        try {
            schoolService.deleteSchool(schoolId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
