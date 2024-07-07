package org.example.ejournal.web;

import org.example.ejournal.dtos.request.UserLoginDtoRequest;
import org.example.ejournal.dtos.request.AdminRegisterDtoRequest;
import org.example.ejournal.dtos.response.LoginResponse;
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

//    @GetMapping("/login")
//    public ResponseEntity<String> showLoginPage() {
//        return ResponseEntity.ok("login");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody UserLoginDtoRequest request) {
//        boolean isAuthenticated = userAuthenticationService.login(request.getUsername(), request.getPassword());
//        if (isAuthenticated) {
//            return ResponseEntity.ok("Login successful");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }

    @GetMapping("/login")
    public ResponseEntity<String> showLoginPage() {
        return ResponseEntity.ok("login");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDtoRequest request) {
        boolean isAuthenticated = userAuthenticationService.login(request.getUsername(), request.getPassword());

        System.out.println("successful");
        if (isAuthenticated) {
            LoginResponse response = new LoginResponse("Login successful");
            return ResponseEntity.ok(response);
        } else {
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
    public ResponseEntity<String> register(@RequestBody AdminRegisterDtoRequest request) {
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

