package com.military.comms.controller;

import com.military.comms.dto.*;
import com.military.comms.model.MilitaryUser;
import com.military.comms.repository.MilitaryUserRepository;
import com.military.comms.service.JwtUtil;
import com.military.comms.service.MilitaryUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

/**
 * PART 2 - AuthController
 * POST /api/auth/register  - register a new military personnel
 * POST /api/auth/login     - authenticate and receive a JWT
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final MilitaryUserDetailsService userDetailsService;
    private final MilitaryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /** Register a new military personnel user */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken.");
        }

        Set<String> roles = (request.getRoles() == null || request.getRoles().isEmpty())
                ? Set.of("ROLE_SOLDIER")
                : request.getRoles();

        MilitaryUser user = MilitaryUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .rank(request.getRank())
                .unit(request.getUnit())
                .email(request.getEmail())
                .roles(roles)
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("Personnel registered: " + request.getUsername());
    }

    /** Authenticate with username/password, return JWT on success */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        MilitaryUser user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRank(), user.getUnit()));
    }
}
