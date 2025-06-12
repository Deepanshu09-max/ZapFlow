package com.zapflow.primarybackend.controller;

import com.zapflow.primarybackend.dto.UserSignupRequest;
import com.zapflow.primarybackend.dto.UserSigninRequest;
import com.zapflow.primarybackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody UserSignupRequest request) {
        try {
            logger.info("User signup request for email: {}", request.getUsername());
            
            userService.signup(request);
            
            return ResponseEntity.ok(Map.of(
                "message", "User created successfully",
                "email", request.getUsername()
            ));
        } catch (Exception e) {
            logger.error("Signup failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
    
    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signin(@RequestBody UserSigninRequest request) {
        try {
            logger.info("User signin request for email: {}", request.getUsername());
            
            String token = userService.signin(request);
            
            return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Login successful"
            ));
        } catch (Exception e) {
            logger.error("Signin failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getUserProfile(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(403).body(Map.of(
                    "error", "You are not logged in"
                ));
            }
            
            Integer userId = (Integer) authentication.getPrincipal();
            Map<String, Object> userProfile = userService.getUserProfile(userId);
            
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            logger.error("Failed to get user profile: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
}
