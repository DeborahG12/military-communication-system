package com.military.comms.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class RegisterRequest {
    @NotBlank private String username;
    @NotBlank private String password;
    @NotBlank private String rank;
    @NotBlank private String unit;
    private String email;
    private Set<String> roles;
}
