package com.zapflow.primarybackend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapflow.primarybackend.dto.UserSignupRequest;
import com.zapflow.primarybackend.dto.ZapCreateRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static UserSignupRequest createValidUserSignupRequest() {
        UserSignupRequest request = new UserSignupRequest();
        request.setUsername("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");
        return request;
    }
    
    public static ZapCreateRequest createValidZapCreateRequest() {
        ZapCreateRequest request = new ZapCreateRequest();
        request.setAvailableTriggerId("webhook");
        request.setTriggerMetadata(new HashMap<>());
        
        ZapCreateRequest.ActionRequest actionRequest = new ZapCreateRequest.ActionRequest();
        actionRequest.setAvailableActionId("email");
        Map<String, Object> actionMetadata = new HashMap<>();
        actionMetadata.put("email", "test@example.com");
        actionMetadata.put("body", "Test email");
        actionRequest.setActionMetadata(actionMetadata);
        
        request.setActions(List.of(actionRequest));
        return request;
    }
    
    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}