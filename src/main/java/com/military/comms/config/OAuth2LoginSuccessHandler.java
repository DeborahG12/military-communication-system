package com.military.comms.config;

import com.military.comms.model.MilitaryUser;
import com.military.comms.repository.MilitaryUserRepository;
import com.military.comms.service.JwtUtil;
import com.military.comms.service.MilitaryUserDetailsService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MilitaryUserRepository userRepository;
    private final MilitaryUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // ✅ Better way to detect provider — uses Spring's built-in token
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String provider = oauthToken.getAuthorizedClientRegistrationId(); // "google" or "github"

        String email;
        String username;

        if ("github".equals(provider)) {
            // GitHub uses "login" as the username field
            username = oAuth2User.getAttribute("login");   // e.g. "sgt-doe"
            email    = oAuth2User.getAttribute("email");   // may be null if user set email to private

            // If email is null (private GitHub account), use username@github.com as fallback
            if (email == null) {
                email = username + "@github.com";
            }

        } else {
            // Google always provides email and name
            email    = oAuth2User.getAttribute("email");
            username = email != null
                    ? email.split("@")[0]                          // e.g. "jane.doe"
                    : oAuth2User.getAttribute("name");
        }

        // Safety check — if username is still null for any reason
        if (username == null || username.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Could not determine username from provider\"}");
            return;
        }

        // Create the user in our local DB if they don't exist yet
        // If they already exist (returning user), just load them
        final String finalUsername = username;
        final String finalEmail    = email;

        MilitaryUser user = userRepository.findByUsername(finalUsername)
                .orElseGet(() -> userRepository.save(
                        MilitaryUser.builder()
                                .username(finalUsername)
                                .password("OAUTH2_NO_PASSWORD")  // No password for OAuth2 users
                                .rank("Civilian")
                                .unit("Unassigned")
                                .email(finalEmail)
                                .oauthProvider(provider)         // "google" or "github"
                                .roles(Set.of("ROLE_SOLDIER"))   // Default role
                                .build()
                ));

        // Issue our own JWT so the rest of the app stays stateless
        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);

        // Return the JWT as JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{"
                        + "\"token\":\"" + jwt + "\","
                        + "\"username\":\"" + user.getUsername() + "\","
                        + "\"rank\":\"" + user.getRank() + "\","
                        + "\"unit\":\"" + user.getUnit() + "\","
                        + "\"provider\":\"" + provider + "\""
                        + "}"
        );
    }
}