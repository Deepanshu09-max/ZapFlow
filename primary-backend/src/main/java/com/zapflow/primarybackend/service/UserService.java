package com.zapflow.primarybackend.service;

import com.zapflow.primarybackend.dto.UserSigninRequest;
import com.zapflow.primarybackend.dto.UserSignupRequest;
import com.zapflow.primarybackend.entity.User;
import com.zapflow.primarybackend.repository.UserRepository;
import com.zapflow.primarybackend.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public void signup(UserSignupRequest request) {
        logger.info("Processing signup for email: {}", request.getUsername());
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getUsername())) {
            throw new RuntimeException("User with this email already exists");
        }
        
        // Create new user
        User user = new User(
            request.getName(),
            request.getUsername(),
            passwordEncoder.encode(request.getPassword())
        );
        
        userRepository.save(user);
        logger.info("User created successfully: {}", request.getUsername());
    }
    
    public String signin(UserSigninRequest request) {
        logger.info("Processing signin for email: {}", request.getUsername());
        
        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(request.getUsername());
        if (userOpt.isEmpty()) {
            logger.error("User not found with email: {}", request.getUsername());
            throw new RuntimeException("Invalid email or password");
        }
        
        User user = userOpt.get();
        logger.info("Found user: {} with ID: {}", user.getEmail(), user.getId());
        
        // Verify password
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        
        if (!passwordMatches) {
            logger.error("Password mismatch for user: {}", request.getUsername());
            throw new RuntimeException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId());
        logger.info("User signed in successfully: {}", request.getUsername());
        
        return token;
    }
    
    public Map<String, Object> getUserProfile(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        return Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail()
        );
    }
}
