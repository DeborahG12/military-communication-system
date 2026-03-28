package com.military.comms;

import com.military.comms.model.MilitaryUser;
import com.military.comms.repository.MilitaryUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

/**
 * Military Communication System - Main Application
 * Seeds the database with test users on startup.
 */
@SpringBootApplication
public class MilitaryCommsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilitaryCommsApplication.class, args);
    }

    /** Seed initial users so you can test immediately without registering */
    @Bean
    CommandLineRunner seedDatabase(MilitaryUserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByUsername("admin")) {
                repo.save(MilitaryUser.builder()
                        .username("admin")
                        .password(encoder.encode("admin123"))
                        .rank("General")
                        .unit("HQ Command")
                        .roles(Set.of("ROLE_ADMIN", "ROLE_OFFICER", "ROLE_SOLDIER"))
                        .build());
            }
            if (!repo.existsByUsername("col.smith")) {
                repo.save(MilitaryUser.builder()
                        .username("col.smith")
                        .password(encoder.encode("officer123"))
                        .rank("Colonel")
                        .unit("Bravo Battalion")
                        .roles(Set.of("ROLE_OFFICER", "ROLE_SOLDIER"))
                        .build());
            }
            if (!repo.existsByUsername("sgt.doe")) {
                repo.save(MilitaryUser.builder()
                        .username("sgt.doe")
                        .password(encoder.encode("soldier123"))
                        .rank("Sergeant")
                        .unit("Alpha Company")
                        .roles(Set.of("ROLE_SOLDIER"))
                        .build());
            }
            System.out.println("=== Military Comms System Ready ===");
            System.out.println("Admin: admin / admin123");
            System.out.println("Officer: col.smith / officer123");
            System.out.println("Soldier: sgt.doe / soldier123");
        };
    }
}
