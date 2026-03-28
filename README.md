# 🪖 Military Communication System
## She Can Code - Week 10 Assignment
### Spring Boot Security: JWT Authentication & OAuth2

## 📋 Project Overview
A fully secured Military Communication System built with Spring Boot.
Implements all 5 parts of the Week 10 assignment using a real-world military context.

## ✅ Assignment Parts Covered
| Part | Topic | Implementation |
|---|---|---|
| Part 1 | Spring Security | Secure all API routes with filter chain |
| Part 2 | JWT Authentication | Login as soldier/officer/admin → get Bearer token |
| Part 3 | Role-Based Access Control | ADMIN only delete, OFFICER sees TOP_SECRET messages |
| Part 4 | OAuth2 Google + GitHub | Social login → local user created → JWT issued |
| Part 5 | Unit Tests | 5 tests with @WithMockUser verifying RBAC rules |

## 🚀 How to Run
```bash
mvn spring-boot:run
```
Access Swagger UI at: http://localhost:9090/swagger-ui.html

## 👤 Test Users
| Username | Password | Role |
|---|---|---|
| admin | admin123 | ROLE_ADMIN |
| col.smith | officer123 | ROLE_OFFICER |
| sgt.doe | soldier123 | ROLE_SOLDIER |

## 📸 Screenshots
| # | Description |
|---|---|
| MI2 | Login and JWT token response |
| MI3 | DELETE with soldier token - 403 Forbidden |
| MI4 | DELETE with admin token - 204 No Content |
| MI5 | GitHub OAuth2 authorization page |

Screenshots are located in `src/main/resources/screenshot/`

## 🔐 Security Features
- BCrypt password hashing
- Stateless JWT authentication (15 min expiry)
- Role-based access control with @PreAuthorize
- OAuth2 login with Google and GitHub
- CSRF disabled for stateless REST API

## 📁 Project Structure
```
src/
├── main/java/com/military/comms/
│   ├── config/          # SecurityConfig, SwaggerConfig, OAuth2Handler
│   ├── controller/      # Auth, Personnel, Message, Profile controllers
│   ├── dto/             # Request/Response DTOs
│   ├── filter/          # JwtAuthenticationFilter
│   ├── model/           # MilitaryUser, Message entities
│   ├── repository/      # JPA repositories
│   └── service/         # JwtUtil, UserDetailsService
└── test/                # Unit tests with @WithMockUser
```