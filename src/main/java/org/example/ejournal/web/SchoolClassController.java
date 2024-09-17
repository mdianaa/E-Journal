package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.services.SchoolClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/school-classes")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    public SchoolClassController(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<?> createClass(@Valid @RequestBody SchoolClassDtoRequest schoolClassDto) {
        try {
            SchoolClassDtoRequest createdClassDto = schoolClassService.createClass(schoolClassDto);
            return new ResponseEntity<>(createdClassDto, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/changeHeadTeacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> changeHeadTeacherPage() {
        return ResponseEntity.ok("change head teacher");
    }

    @PutMapping("/changeHeadTeacher/{classId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolClassDtoRequest> changeHeadTeacher(@PathVariable long classId,
                                                                    @Valid @RequestBody TeacherDtoRequest headTeacherDto) {
        SchoolClassDtoRequest changedClassDto = schoolClassService.changeHeadTeacher(classId, headTeacherDto);
        if (changedClassDto != null) {
            return ResponseEntity.ok(changedClassDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    
    @DeleteMapping("/delete/{classId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClass(@PathVariable long classId) {
        schoolClassService.deleteClass(classId);
        return ResponseEntity.noContent().build();
    }
}
