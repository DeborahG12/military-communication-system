package com.military.comms.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

/**
 * Represents a military personnel user in the communication system.
 * Roles: ROLE_SOLDIER, ROLE_OFFICER, ROLE_ADMIN
 */
@Entity
@Table(name = "military_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilitaryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;         // e.g. "sgt.doe"

    @Column(nullable = false)
    private String password;         // BCrypt-hashed

    @Column(nullable = false)
    private String rank;             // e.g. "Sergeant", "Colonel"

    @Column(nullable = false)
    private String unit;             // e.g. "Alpha Company"

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;       // e.g. {"ROLE_SOLDIER"}, {"ROLE_ADMIN"}

    private String email;
    private String oauthProvider;    // "google" or "github" if OAuth2 user
}
