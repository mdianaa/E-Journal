package org.example.ejournal.web;

import org.example.ejournal.dtos.request.UserLoginDtoRequest;
import org.example.ejournal.dtos.request.AdminRegisterDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.LoginResponseDto;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.services.UserAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> showLoginPage() {
        return ResponseEntity.ok("login");
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> testAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authenticated user");
        }
        
        return ResponseEntity.ok("Authenticated user: " + authentication.getName());
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDtoRequest request) {
        try {
            LoginResponseDto loginResponse = userAuthenticationService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showRegisterPage() {
        return ResponseEntity.ok("register");
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> register(@RequestBody UserRegisterDtoRequest request) {
        userAuthenticationService.register(request);
        return ResponseEntity.ok("Registration successful");
    }

    @GetMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showChangeRolePage() {
        return ResponseEntity.ok("change role");
    }

    @PostMapping("/role/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> changeUserRole(@PathVariable long userId, @RequestBody RoleType roleType) {
        userAuthenticationService.changeUserRole(userId, roleType);
        return ResponseEntity.ok("Role changed successfully");
    }
}

