package com.military.comms.controller;

import com.military.comms.model.MilitaryUser;
import com.military.comms.repository.MilitaryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * PART 1 + PART 3 - PersonnelController
 * GET  /api/personnel         - public (all users can list personnel)
 * GET  /api/personnel/{id}    - authenticated users
 * DELETE /api/personnel/{id}  - ADMIN only (@PreAuthorize)
 */
@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class PersonnelController {

    private final MilitaryUserRepository userRepository;

    /** Public endpoint - list all military personnel */
    @GetMapping
    public ResponseEntity<List<MilitaryUser>> getAllPersonnel() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    /** Authenticated endpoint - get one personnel record */
    @GetMapping("/{id}")
    public ResponseEntity<MilitaryUser> getPersonnel(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PART 3 - Role-Based Access Control
     * Only ROLE_ADMIN can discharge (delete) personnel.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> dischargePersonnel(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
