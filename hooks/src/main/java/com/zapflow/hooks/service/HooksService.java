package com.zapflow.hooks.service;

import com.zapflow.hooks.entity.ZapRun;
import com.zapflow.hooks.entity.ZapRunOutbox;
import com.zapflow.hooks.repository.ZapRunRepository;
import com.zapflow.hooks.repository.ZapRunOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class HooksService {
    
    private static final Logger logger = LoggerFactory.getLogger(HooksService.class);
    
    @Autowired
    private ZapRunRepository zapRunRepository;
    
    @Autowired
    private ZapRunOutboxRepository zapRunOutboxRepository;
    
    @Transactional
    public Map<String, Object> processWebhook(String userId, String zapId, Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            logger.info("Processing webhook for userId: {}, zapId: {}", userId, zapId);
            
            // Create ZapRun
            ZapRun zapRun = new ZapRun(zapId, requestBody);
            zapRun = zapRunRepository.save(zapRun);
            
            // Create ZapRunOutbox entry
            ZapRunOutbox outboxEntry = new ZapRunOutbox(zapRun.getId());
            zapRunOutboxRepository.save(outboxEntry);
            
            logger.info("Successfully created ZapRun: {} and outbox entry", zapRun.getId());
            
            response.put("message", "Webhook received");
            return response;
            
        } catch (Exception e) {
            logger.error("Error processing webhook for zapId: {}", zapId, e);
            response.put("error", "Failed to process webhook: " + e.getMessage());
            return response;
        }
    }
}
