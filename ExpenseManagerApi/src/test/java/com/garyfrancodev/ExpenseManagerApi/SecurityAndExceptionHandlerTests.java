package com.garyfrancodev.ExpenseManagerApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAndExceptionHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 401 Unauthorized when requesting protected endpoints without token")
    void shouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/account?userId=" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    @DisplayName("Should permit registration and login to return JWT token, then allow access to protected endpoints")
    void shouldRegisterAndAuthenticate() throws Exception {
        String uniqueEmail = "user_" + UUID.randomUUID() + "@example.com";
        String password = "Password123!";
        String registerJson = String.format("""
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "%s",
                    "password": "%s"
                }
                """, uniqueEmail, password);

        // Register returns UUID
        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk())
                .andReturn();

        String userIdStr = registerResult.getResponse().getContentAsString().replace("\"", "").trim();

        // Login returns jwt
        String loginJson = String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, uniqueEmail, password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").isString())
                .andReturn();

        JsonNode responseNode = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        String token = responseNode.get("jwt").asText();

        // Access protected endpoint with Bearer token
        mockMvc.perform(get("/api/account?userId=" + userIdStr)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 422 Unprocessable Entity or 400 Bad Request when business rule is violated")
    void shouldHandleBusinessRuleViolation() throws Exception {
        // Register & login to obtain a token
        String uniqueEmail = "rule_test_" + UUID.randomUUID() + "@example.com";
        String password = "Password123!";
        String registerJson = String.format("""
                {
                    "firstName": "Jane",
                    "lastName": "Doe",
                    "email": "%s",
                    "password": "%s"
                }
                """, uniqueEmail, password);

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk())
                .andReturn();

        String userIdStr = registerResult.getResponse().getContentAsString().replace("\"", "").trim();

        String loginJson = String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, uniqueEmail, password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("jwt").asText();

        // Attempt to create an account with negative initial balance to trigger domain business rule violation
        String invalidAccountJson = String.format("""
                {
                    "accountName": "Checking",
                    "description": "My checking account",
                    "amount": -100.00,
                    "userId": "%s"
                }
                """, userIdStr);

        mockMvc.perform(post("/api/account")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAccountJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Business Rule Violation"))
                .andExpect(jsonPath("$.message").value("Initial balance cannot be negative"));
    }
}
