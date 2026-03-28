package com.military.comms.service;

import com.military.comms.model.MilitaryUser;
import com.military.comms.repository.MilitaryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

/**
 * PART 1 - UserDetailsService
 * Loads a MilitaryUser by username and wraps it as Spring Security UserDetails.
 */
@Service
@RequiredArgsConstructor
public class MilitaryUserDetailsService implements UserDetailsService {

    private final MilitaryUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MilitaryUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Military personnel not found: " + username));

        var authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
