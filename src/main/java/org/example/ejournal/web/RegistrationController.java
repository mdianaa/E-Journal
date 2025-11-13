package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.services.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/create-user")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class RegistrationController {

    // registration logic from the admin

    private final RegistrationService registration;

    @PostMapping("/headmaster")
    public ResponseEntity<Void> createHeadmaster(@Valid @RequestBody HeadmasterDtoRequest req) {
        registration.createHeadmaster(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/teacher")
    public ResponseEntity<Void> createTeacher(@Valid @RequestBody TeacherDtoRequest req) {
        registration.createTeacher(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/student")
    public ResponseEntity<Void> createStudent(@Valid @RequestBody StudentDtoRequest req) {
        registration.createStudent(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/parent")
    public ResponseEntity<Void> createParent(@Valid @RequestBody ParentDtoRequest req) {
        registration.createParent(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

