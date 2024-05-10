package org.example.ejournal.controllers;

import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.services.UserAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserAuthenticationService userService;

    public UserController(UserAuthenticationService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegisterDtoRequest userDto) {
        userService.register(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable long userId, @RequestParam RoleType role) {
        UserRegisterDtoRequest updatedUser = userService.changeUserRole(userId, role);
        if (updatedUser != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestParam String username, @RequestParam String password) {
        boolean loggedIn = userService.login(username, password);
        if (loggedIn) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
