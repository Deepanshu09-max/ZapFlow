package com.zapflow.primarybackend.controller;

import com.zapflow.primarybackend.dto.ZapCreateRequest;
import com.zapflow.primarybackend.service.ZapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/zap")
@CrossOrigin(origins = "*")
public class ZapController {
    
    private static final Logger logger = LoggerFactory.getLogger(ZapController.class);
    
    @Autowired
    private ZapService zapService;
    
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> createZap(@RequestBody ZapCreateRequest request, Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(403).body(Map.of("error", "You are not logged in"));
            }
            
            Integer userId = (Integer) authentication.getPrincipal();
            logger.info("Creating zap for user: {}", userId);
            
            Map<String, Object> result = zapService.createZap(userId, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to create zap: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getUserZaps(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(403).body(Map.of("error", "You are not logged in"));
            }
            
            Integer userId = (Integer) authentication.getPrincipal();
            Map<String, Object> result = zapService.getUserZaps(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to get user zaps: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{zapId}")
    public ResponseEntity<Map<String, Object>> getZapById(@PathVariable String zapId, Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(403).body(Map.of("error", "You are not logged in"));
            }
            
            Integer userId = (Integer) authentication.getPrincipal();
            Map<String, Object> result = zapService.getZapById(userId, zapId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to get zap: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}