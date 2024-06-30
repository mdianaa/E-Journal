package org.example.ejournal.web;

import org.example.ejournal.dtos.request.UserLoginDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.services.UserAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDtoRequest request) {
        boolean isAuthenticated = userAuthenticationService.login(request.getUsername(), request.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> register(@RequestBody UserRegisterDtoRequest request) {
        userAuthenticationService.register(request);
        return ResponseEntity.ok("Registration successful");
    }

    @GetMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String showChangeRolePage() {
        return "change role";
    }

    @PostMapping("/role/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserRole(@PathVariable long userId, @RequestBody RoleType roleType) {
        userAuthenticationService.changeUserRole(userId, roleType);
        return ResponseEntity.ok("Role changed successfully");
    }
}

