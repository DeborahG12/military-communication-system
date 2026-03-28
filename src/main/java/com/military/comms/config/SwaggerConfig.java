package com.military.comms.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Paste your JWT token here. Get it from POST /api/auth/login"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI militaryCommsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Military Communication System API")
                        .description("Secure Military Communication Platform — JWT + OAuth2")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}