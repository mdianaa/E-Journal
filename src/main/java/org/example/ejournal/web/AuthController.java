package org.example.ejournal.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.LoginDtoRequest;
import org.example.ejournal.dtos.response.LoginDtoResponse;
import org.example.ejournal.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // login logic

    private final AuthenticationManager authManager;

    private final JwtUtil jwt;

    @PostMapping("/login")
    public ResponseEntity<LoginDtoResponse> login(@Valid @RequestBody LoginDtoRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail().trim().toLowerCase(Locale.ROOT), req.getPassword()));

        String token = jwt.generate((UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(new LoginDtoResponse(token, "Bearer", 900L));
    }
}