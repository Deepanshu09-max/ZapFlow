package com.zapflow.primarybackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapflow.primarybackend.dto.ZapCreateRequest;
import com.zapflow.primarybackend.entity.User;
import com.zapflow.primarybackend.repository.UserRepository;
import com.zapflow.primarybackend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class ZapIntegrationTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private MockMvc mockMvc;
    private String jwtToken;
    private User testUser;
    
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create test user
        testUser = new User("Test User", "test@example.com", passwordEncoder.encode("password123"));
        testUser = userRepository.save(testUser);
        
        // Generate JWT token
        jwtToken = jwtUtil.generateToken(testUser.getId());
    }
    
    @Test
    public void testCreateZap() throws Exception {
        ZapCreateRequest zapRequest = new ZapCreateRequest();
        zapRequest.setAvailableTriggerId("webhook");
        zapRequest.setTriggerMetadata(new HashMap<>());
        
        ZapCreateRequest.ActionRequest actionRequest = new ZapCreateRequest.ActionRequest();
        actionRequest.setAvailableActionId("email");
        Map<String, Object> actionMetadata = new HashMap<>();
        actionMetadata.put("email", "test@example.com");
        actionMetadata.put("body", "Test email");
        actionRequest.setActionMetadata(actionMetadata);
        
        zapRequest.setActions(List.of(actionRequest));
        
        mockMvc.perform(post("/api/v1/zap/")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zapRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zapId").exists());
    }
    
    @Test
    public void testGetUserZaps() throws Exception {
        mockMvc.perform(get("/api/v1/zap/")
                .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zaps").isArray());
    }
    
    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/zap/"))
                .andExpect(status().isForbidden());
    }
}