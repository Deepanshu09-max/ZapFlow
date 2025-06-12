package com.zapflow.worker.consumer;

import com.zapflow.worker.dto.ZapRunMessage;
import com.zapflow.worker.service.ActionExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ZapEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(ZapEventConsumer.class);
    
    @Autowired
    private ActionExecutorService actionExecutorService;
    
    @KafkaListener(topics = "zap-events", groupId = "worker-group")
    public void handleZapEvent(
            @Payload ZapRunMessage message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            logger.info("Received Kafka message - Topic: {}, Partition: {}, Offset: {}, Key: {}, ZapRunId: {}", 
                       topic, partition, offset, key, message.getZapRunId());
            
            actionExecutorService.executeActions(message);
            acknowledgment.acknowledge();
            
            logger.info("Successfully processed and acknowledged message for zapRunId: {}", 
                       message.getZapRunId());
            
        } catch (Exception e) {
            logger.error("Failed to process Kafka message for zapRunId: {} - Error: {}", 
                        message.getZapRunId(), e.getMessage(), e);
            
            acknowledgment.acknowledge();
            logger.warn("Acknowledged failed message to prevent infinite retries");
        }
    }
}
