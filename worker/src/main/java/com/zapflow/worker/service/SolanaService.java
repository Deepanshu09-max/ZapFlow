package com.zapflow.worker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SolanaService {
    
    private static final Logger logger = LoggerFactory.getLogger(SolanaService.class);
    
    public void sendSolana(Map<String, Object> metadata) {
        try {
            String toAddress = (String) metadata.get("address");
            Double amount = (Double) metadata.get("amount");
            
            if (toAddress == null || amount == null) {
                throw new IllegalArgumentException("Address and amount are required");
            }
            
            logger.info("Sending {} SOL to address: {}", amount, toAddress);
            
            // Simulate Solana transaction
            // In a real implementation, you would integrate with Solana SDK
            Thread.sleep(1000); // Simulate network delay
            
            logger.info("Successfully sent {} SOL to: {}", amount, toAddress);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Solana transaction interrupted", e);
            throw new RuntimeException("Solana transaction failed", e);
        } catch (Exception e) {
            logger.error("Failed to send Solana: {}", e.getMessage(), e);
            throw new RuntimeException("Solana transaction failed", e);
        }
    }
}
