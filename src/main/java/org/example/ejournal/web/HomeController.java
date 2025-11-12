package org.example.ejournal.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/home")
    public ResponseEntity<Void> home() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/home")
    public ResponseEntity<Void> homeAdmin() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/headmaster/home")
    public ResponseEntity<Void> homeHeadmaster() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher/home")
    public ResponseEntity<Void> homeTeacher() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/parent/home")
    public ResponseEntity<Void> homeParent() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/home")
    public ResponseEntity<Void> homeStudent() {
        return ResponseEntity.ok().build();
    }
}
