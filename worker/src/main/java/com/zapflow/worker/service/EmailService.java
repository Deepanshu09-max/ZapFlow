package com.zapflow.worker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendEmail(Map<String, Object> metadata) {
        try {
            String toEmail = (String) metadata.get("email");
            String body = (String) metadata.get("body");
            String subject = (String) metadata.getOrDefault("subject", "ZapFlow Notification");
            
            if (toEmail == null || body == null) {
                throw new IllegalArgumentException("Email and body are required");
            }
            
            logger.info("Sending email to: {}", toEmail);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@zapflow.com");
            
            mailSender.send(message);
            
            logger.info("Email sent successfully to: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Failed to send email: {}", e.getMessage(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
