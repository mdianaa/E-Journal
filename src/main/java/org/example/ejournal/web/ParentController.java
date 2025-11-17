package org.example.ejournal.web;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.services.ParentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parent")
public class ParentController {

    private final ParentService parentService;

    // Get the parent of a given student
    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER', 'HEADMASTER')")
    public ResponseEntity<ParentDtoResponse> viewParentOfStudent(@PathVariable long studentId) {
        return ResponseEntity.ok(parentService.viewParentOfStudent(studentId));
    }

    // Get all parents that have children in a specific school
    @GetMapping("/schools/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER')")
    public ResponseEntity<Set<ParentDtoResponse>> viewAllParentsInSchool(@PathVariable long schoolId) {
        return ResponseEntity.ok(parentService.viewAllParentsInSchool(schoolId));
    }

    // Delete a parent (does NOT delete underlying User)
    @DeleteMapping("/{parentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteParent(@PathVariable long parentId) {
        parentService.deleteParent(parentId);
    }
}