package com.zapflow.worker.service;

import com.zapflow.worker.dto.ZapRunMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class ActionExecutorService {
    
    private static final Logger logger = LoggerFactory.getLogger(ActionExecutorService.class);
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private SolanaService solanaService;
    
    @Value("${worker.processing-delay:500}")
    private long processingDelay;
    
    public void executeActions(ZapRunMessage message) {
        try {
            logger.info("Executing actions for zapRunId: {}, zapId: {}", 
                       message.getZapRunId(), message.getZapId());
            
            // Sort actions by sortingOrder
            message.getActions().stream()
                .sorted(Comparator.comparing(ZapRunMessage.ActionDto::getSortingOrder))
                .forEach(this::executeAction);
            
            logger.info("All actions completed for zapRunId: {}", message.getZapRunId());
            
        } catch (Exception e) {
            logger.error("Failed to execute actions for zapRunId: {}", 
                        message.getZapRunId(), e);
            throw e;
        }
    }
    
    private void executeAction(ZapRunMessage.ActionDto action) {
        try {
            logger.info("Executing action: {} with order: {}", 
                       action.getActionId(), action.getSortingOrder());
            
            switch (action.getActionId()) {
                case "email":
                    emailService.sendEmail(action.getMetadata());
                    break;
                case "send-sol":
                    solanaService.sendSolana(action.getMetadata());
                    break;
                default:
                    logger.warn("Unknown action type: {}", action.getActionId());
                    break;
            }
            
            // Add delay between actions if configured
            if (processingDelay > 0) {
                Thread.sleep(processingDelay);
            }
            
            logger.info("Action {} completed successfully", action.getActionId());
            
        } catch (Exception e) {
            logger.error("Action {} failed: {}", action.getActionId(), e.getMessage(), e);
            throw new RuntimeException("Action execution failed: " + action.getActionId(), e);
        }
    }
}
