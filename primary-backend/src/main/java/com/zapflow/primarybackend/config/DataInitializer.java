package com.zapflow.primarybackend.config;

import com.zapflow.primarybackend.entity.AvailableAction;
import com.zapflow.primarybackend.entity.AvailableTrigger;
import com.zapflow.primarybackend.repository.AvailableActionRepository;
import com.zapflow.primarybackend.repository.AvailableTriggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private AvailableTriggerRepository availableTriggerRepository;
    
    @Autowired
    private AvailableActionRepository availableActionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing default data...");
        
        // Initialize Available Triggers
        if (!availableTriggerRepository.existsById("webhook")) {
            AvailableTrigger webhookTrigger = new AvailableTrigger(
                "webhook", 
                "Webhook", 
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIovxkR9l-OlwpjTXV1B4YNh0W_s618ijxAQ&s"
            );
            availableTriggerRepository.save(webhookTrigger);
            logger.info("Created webhook trigger");
        }
        
        // Initialize Available Actions
        if (!availableActionRepository.existsById("send-sol")) {
            AvailableAction sendSolAction = new AvailableAction(
                "send-sol",
                "Send Solana",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT10458YI0Lf1-Zx4fGwhWxI_x4oPCD034xaw&s"
            );
            availableActionRepository.save(sendSolAction);
            logger.info("Created send-sol action");
        }
        
        if (!availableActionRepository.existsById("email")) {
            AvailableAction emailAction = new AvailableAction(
                "email",
                "Send Email",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4nd82eFk5SaBPRIeCpmwL7A4YSokA-kXSmw&s"
            );
            availableActionRepository.save(emailAction);
            logger.info("Created email action");
        }
        
        logger.info("Data initialization completed");
    }
}
