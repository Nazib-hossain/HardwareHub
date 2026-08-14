package com.hardware.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Public endpoints like / and /login should be accessible unauthenticated")
    void testPublicEndpointsAccessible() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin-only endpoint /products/delete/1 should redirect unauthenticated user to login")
    void testDeleteEndpointRedirectsUnauthenticated() throws Exception {
        mockMvc.perform(get("/products/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }



    @Test
    @DisplayName("Standard USER role should be forbidden (403) from /products/delete/1")
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteForbiddenForUserRole() throws Exception {
        mockMvc.perform(get("/products/delete/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ADMIN role should be allowed to call /products/delete/1")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteAllowedForAdminRole() throws Exception {
        mockMvc.perform(get("/products/delete/1"))
                .andExpect(status().is3xxRedirection()); // redirects to /products after deletion
    }

    @Test
    @DisplayName("Verify BCrypt password encoding for admin123")
    void testBcryptPassword() {
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String hash = "$2a$10$2LNC55pq.hWKszJszMTmk.C0q9kKS9qeDKlnyRW4fnedbuA.ZYRQi";
        boolean matches = encoder.matches("admin123", hash);
        org.assertj.core.api.Assertions.assertThat(matches).isTrue();
    }

}

