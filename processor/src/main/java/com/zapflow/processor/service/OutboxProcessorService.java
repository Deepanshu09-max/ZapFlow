package com.zapflow.processor.service;

import com.zapflow.processor.dto.ZapRunMessage;
import com.zapflow.processor.entity.ZapRunOutbox;
import com.zapflow.processor.repository.ZapRunOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutboxProcessorService {
    
    private static final Logger logger = LoggerFactory.getLogger(OutboxProcessorService.class);
    private static final String KAFKA_TOPIC = "zap-events";
    
    @Autowired
    private ZapRunOutboxRepository zapRunOutboxRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${processor.batch-size:10}")
    private int batchSize;
    
    @Scheduled(fixedDelayString = "${processor.polling-interval:3000}")
    @Transactional
    public void processOutboxEntries() {
        try {
            logger.debug("Polling for outbox entries, batch size: {}", batchSize);
            
            Pageable pageable = PageRequest.of(0, batchSize);
            List<ZapRunOutbox> outboxEntries = zapRunOutboxRepository.findPendingOutboxEntries(pageable);
            
            if (outboxEntries.isEmpty()) {
                logger.debug("No outbox entries found");
                return;
            }
            
            logger.info("Processing {} outbox entries", outboxEntries.size());
            
            for (ZapRunOutbox outboxEntry : outboxEntries) {
                processOutboxEntry(outboxEntry);
            }
            
        } catch (Exception e) {
            logger.error("Error processing outbox entries", e);
        }
    }
    
    private void processOutboxEntry(ZapRunOutbox outboxEntry) {
        try {
            logger.info("Processing outbox entry: {}", outboxEntry.getId());
            
            ZapRunMessage message = buildZapRunMessage(outboxEntry);
            
            try {
                kafkaTemplate.send(KAFKA_TOPIC, message.getZapRunId(), message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            logger.info("Successfully sent message to Kafka for zapRunId: {}", message.getZapRunId());
                            deleteOutboxEntry(outboxEntry.getId());
                        } else {
                            logger.error("Failed to send message to Kafka for zapRunId: {} - Error: {}", 
                                       message.getZapRunId(), ex.getMessage());
                            logger.warn("Deleting outbox entry to prevent infinite retries during testing");
                            deleteOutboxEntry(outboxEntry.getId());
                        }
                    });
            } catch (Exception kafkaEx) {
                logger.error("Kafka is not available: {}. Deleting outbox entry to continue testing.", kafkaEx.getMessage());
                deleteOutboxEntry(outboxEntry.getId());
            }
                
        } catch (Exception e) {
            logger.error("Error processing outbox entry: {}", outboxEntry.getId(), e);
        }
    }
    
    private ZapRunMessage buildZapRunMessage(ZapRunOutbox outboxEntry) {
        var zapRun = outboxEntry.getZapRun();
        var zap = zapRun.getZap();
        
        List<ZapRunMessage.ActionDto> actionDtos = zap.getActions().stream()
            .map(action -> new ZapRunMessage.ActionDto(
                action.getActionId(),
                action.getMetadata(),
                action.getSortingOrder()
            ))
            .collect(Collectors.toList());
        
        return new ZapRunMessage(
            zapRun.getId(),
            zap.getId(),
            zap.getUserId(),
            zapRun.getMetadata(),
            actionDtos
        );
    }
    
    @Transactional
    private void deleteOutboxEntry(String outboxId) {
        try {
            zapRunOutboxRepository.deleteById(outboxId);
            logger.debug("Deleted outbox entry: {}", outboxId);
        } catch (Exception e) {
            logger.error("Failed to delete outbox entry: {}", outboxId, e);
        }
    }
}