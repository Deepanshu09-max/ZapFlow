package com.zapflow.hooks.controller;

import com.zapflow.hooks.service.HooksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hooks")
public class HooksController {
    
    private static final Logger logger = LoggerFactory.getLogger(HooksController.class);
    
    @Autowired
    private HooksService hooksService;
    
    @PostMapping("/catch/{userId}/{zapId}")
    public ResponseEntity<Map<String, Object>> catchWebhook(
            @PathVariable String userId,
            @PathVariable String zapId,
            @RequestBody Map<String, Object> requestBody) {
        
        logger.info("Received webhook for userId: {}, zapId: {}", userId, zapId);
        
        Map<String, Object> response = hooksService.processWebhook(userId, zapId, requestBody);
        
        if (response.containsKey("error")) {
            return ResponseEntity.status(500).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}
