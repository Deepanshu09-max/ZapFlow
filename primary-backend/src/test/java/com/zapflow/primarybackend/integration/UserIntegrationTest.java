package com.zapflow.primarybackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapflow.primarybackend.dto.UserSignupRequest;
import com.zapflow.primarybackend.dto.UserSigninRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class UserIntegrationTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    
    @Test
    public void testUserSignupAndSignin() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test user signup
        UserSignupRequest signupRequest = new UserSignupRequest();
        signupRequest.setUsername("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setName("Test User");
        
        mockMvc.perform(post("/api/v1/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created successfully"));
        
        // Test user signin
        UserSigninRequest signinRequest = new UserSigninRequest();
        signinRequest.setUsername("test@example.com");
        signinRequest.setPassword("password123");
        
        mockMvc.perform(post("/api/v1/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signinRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
    
    @Test
    public void testInvalidUserSignup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        UserSignupRequest invalidRequest = new UserSignupRequest();
        invalidRequest.setUsername("invalid-email");
        invalidRequest.setPassword("123"); // Too short
        invalidRequest.setName("");
        
        mockMvc.perform(post("/api/v1/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}