package com.military.comms.controller;

import com.military.comms.model.MilitaryUser;
import com.military.comms.repository.MilitaryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * PART 4 - GET /api/me
 * Returns the currently authenticated user's profile.
 * Works for both JWT-authenticated and OAuth2-authenticated users.
 */
@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class ProfileController {

    private final MilitaryUserRepository userRepository;

    @GetMapping
    public ResponseEntity<MilitaryUser> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
