package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.response.SchoolClassDtoResponse;
import org.example.ejournal.services.SchoolClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/school-class")
public class SchoolClassController {

    private final SchoolClassService service;

    // TODO not to check through id, but check the class name

    // Create new school class
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<SchoolClassDtoResponse> create(@Valid @RequestBody SchoolClassDtoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createClass(req));
    }

    // Change the head teacher of a particular class
    @PatchMapping("/{classId}/head/{teacherId}")
    @PreAuthorize("hasAuthority('ADMIN') " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchoolClass(authentication, #classId))")
    public ResponseEntity<SchoolClassDtoResponse> changeHead(@PathVariable long classId, @PathVariable long teacherId) {
        return ResponseEntity.ok(service.changeHeadTeacher(classId, teacherId));
    }

    // Get a particular school class
    @GetMapping("/{classId}")
    @PreAuthorize("hasAuthority('ADMIN') " +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchoolClass(authentication, #classId)) " +
            "or (hasAuthority('TEACHER') and @authz.isTeacherOfSchoolClass(authentication, #classId))")
    public ResponseEntity<SchoolClassDtoResponse> show(@PathVariable long classId) {
        return ResponseEntity.ok(service.showSchoolClass(classId));
    }
    // Get a list of all the current active classes in a particular school
    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchool(authentication, #schoolId))")
    public ResponseEntity<Set<SchoolClassDtoResponse>> listActive(@PathVariable long schoolId) {
        return ResponseEntity.ok(service.showAllSchoolClassesInSchool(schoolId));
    }

    // Deactivate a graduated class
    @PostMapping("/{classId}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')" +
            "or (hasAuthority('HEADMASTER') and @authz.isHeadmasterOfSchoolClass(authentication, #classId))")
    public void deactivate(@PathVariable long classId) {
        service.deactivateClass(classId);
    }
}