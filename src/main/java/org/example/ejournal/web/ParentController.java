package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.services.ParentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @PostMapping("/create")
    public ResponseEntity<ParentDtoRequest> createParent(@Valid @RequestBody ParentDtoRequest parentDto,
                                                         @Valid @RequestBody SchoolDtoRequest schoolDto) {
        ParentDtoRequest createdParentDto = parentService.createParent(parentDto, schoolDto);
        return new ResponseEntity<>(createdParentDto, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{parentId}")
    public ResponseEntity<ParentDtoRequest> editParent(@PathVariable long parentId,
                                                       @Valid @RequestBody ParentDtoRequest parentDto) {
        ParentDtoRequest editedParentDto = parentService.editParent(parentId, parentDto);
        if (editedParentDto != null) {
            return ResponseEntity.ok(editedParentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/viewAll/{schoolId}")
    public ResponseEntity<Set<ParentDtoResponse>> viewAllParentsInSchool(@PathVariable long schoolId) {
        Set<ParentDtoResponse> parents = parentService.viewAllParentsInSchool(schoolId);
        return ResponseEntity.ok(parents);
    }

    @DeleteMapping("/delete/{parentId}")
    public ResponseEntity<Void> deleteParent(@PathVariable long parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.noContent().build();
    }
}
