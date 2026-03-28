package com.military.comms.controller;

import com.military.comms.filter.JwtAuthenticationFilter;
import com.military.comms.repository.MilitaryUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * PART 5 - Unit Tests for Secured Endpoints
 *
 * Uses:
 *  @WebMvcTest  - loads only the web layer (no full Spring context)
 *  @WithMockUser - injects a fake authenticated user without a real JWT
 *  MockMvc      - test HTTP client for calling endpoints and asserting responses
 */
@WebMvcTest(value = PersonnelController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            classes = JwtAuthenticationFilter.class))
class PersonnelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MilitaryUserRepository userRepository;

    /**
     * Test 1: GET /api/personnel returns 200 WITHOUT authentication.
     * This endpoint is public - anyone can view the roster.
     */
    @Test
    void getPersonnel_publicEndpoint_returns200WithoutAuth() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/personnel"))
                .andExpect(status().isOk());
    }

    /**
     * Test 2: DELETE /api/personnel/1 returns 403 when called by a SOLDIER.
     * Only ADMINs can discharge personnel.
     */
    @Test
    @WithMockUser(roles = "SOLDIER")
    void dischargePersonnel_asSoldier_returns403Forbidden() throws Exception {
        mockMvc.perform(delete("/api/personnel/1"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test 3: DELETE /api/personnel/1 returns 204 when called by ADMIN.
     * The admin has authority to discharge personnel.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void dischargePersonnel_asAdmin_returns204NoContent() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/personnel/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Test 4: GET /api/personnel/1 returns 401 when NOT authenticated.
     * Individual record lookup requires authentication.
     */
    @Test
    void getPersonnelById_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/api/personnel/1"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test 5: GET /api/personnel/1 returns 200 when authenticated as SOLDIER.
     */
    @Test
    @WithMockUser(roles = "SOLDIER")
    void getPersonnelById_authenticated_returns200() throws Exception {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/personnel/1"))
                .andExpect(status().isNotFound()); // Not found, but auth passed (not 401/403)
    }
}
