package com.military.comms.config;

import com.military.comms.filter.JwtAuthenticationFilter;
import com.military.comms.service.MilitaryUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * PART 1 - SecurityConfig
 * Configures the full Spring Security filter chain:
 *  - Public endpoints (login, register, public messages)
 *  - JWT-protected REST endpoints
 *  - OAuth2 Google login
 *  - Role-based method security (@PreAuthorize)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // PART 3: enables @PreAuthorize on controller methods
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final MilitaryUserDetailsService userDetailsService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // PART 1: Disable CSRF (acceptable for stateless REST APIs)
            .csrf(csrf -> csrf.disable())

            // URL-level authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**"
                    ).permitAll()

                // All other /api/** routes require authentication
                .anyRequest().authenticated()
            )

            // PART 1: Stateless session (no HttpSession created)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // PART 4: OAuth2 login with Google
            .oauth2Login(oauth -> oauth
                .successHandler(oAuth2LoginSuccessHandler))

            // Allow H2 console frames
            .headers(headers -> headers.frameOptions(f -> f.disable()))

            // PART 2: Register JwtAuthenticationFilter BEFORE the username/password filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** PART 1: BCrypt for password hashing */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** AuthenticationManager used in AuthController to validate credentials */
    @Bean
    public AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}

