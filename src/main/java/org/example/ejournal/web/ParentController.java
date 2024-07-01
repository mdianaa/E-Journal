package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.services.ParentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showCreateParentPage() {
        return ResponseEntity.ok("create parent");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParentDtoResponse> createParent(@Valid @RequestBody ParentDtoRequest parentDto,
                                                          @Valid @RequestBody SchoolDtoRequest schoolDto,
                                                          @Valid @RequestBody UserRegisterDtoRequest userRegisterDtoRequest) {
        ParentDtoResponse createdParentDto = parentService.createParent(parentDto, schoolDto, userRegisterDtoRequest);
        return new ResponseEntity<>(createdParentDto, HttpStatus.CREATED);
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showEditParentPage() {
        return ResponseEntity.ok("edit parent");
    }

    @PutMapping("/edit/{parentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParentDtoResponse> editParent(@PathVariable long parentId,
                                                        @Valid @RequestBody ParentDtoRequest parentDto) {
        ParentDtoResponse editedParentDto = parentService.editParent(parentId, parentDto);
        if (editedParentDto != null) {
            return ResponseEntity.ok(editedParentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{parentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER', 'STUDENT', 'PARENT')")
    public ResponseEntity<ParentDtoResponse> viewParent(@PathVariable long parentId) {
        ParentDtoResponse parent = parentService.viewParent(parentId);
        return parent != null ? ResponseEntity.ok(parent) : ResponseEntity.notFound().build();
    }

    @GetMapping("/viewAll/{schoolId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'HEADMASTER')")
    public ResponseEntity<Set<ParentDtoResponse>> viewAllParentsInSchool(@PathVariable long schoolId) {
        Set<ParentDtoResponse> parents = parentService.viewAllParentsInSchool(schoolId);
        return ResponseEntity.ok(parents);
    }

    @DeleteMapping("/delete/{parentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteParent(@PathVariable long parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.noContent().build();
    }
}
