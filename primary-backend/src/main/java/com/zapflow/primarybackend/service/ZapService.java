package com.zapflow.primarybackend.service;

import com.zapflow.primarybackend.dto.ZapCreateRequest;
import com.zapflow.primarybackend.entity.Action;
import com.zapflow.primarybackend.entity.Trigger;
import com.zapflow.primarybackend.entity.Zap;
import com.zapflow.primarybackend.repository.ZapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ZapService {
    
    private static final Logger logger = LoggerFactory.getLogger(ZapService.class);
    
    @Autowired
    private ZapRepository zapRepository;
    
    @Transactional
    public Map<String, Object> createZap(Integer userId, ZapCreateRequest request) {
        logger.info("Creating zap for user: {}", userId);
        
        // Create the main Zap entity
        Zap zap = new Zap(userId, request.getAvailableTriggerId());
        zap = zapRepository.save(zap);
        
        // Create trigger
        Trigger trigger = new Trigger(zap.getId(), request.getAvailableTriggerId(), request.getTriggerMetadata());
        zap.setTrigger(trigger);
        
        // Create actions
        List<Action> actions = new ArrayList<>();
        for (int i = 0; i < request.getActions().size(); i++) {
            ZapCreateRequest.ActionRequest actionReq = request.getActions().get(i);
            Action action = new Action(zap.getId(), actionReq.getAvailableActionId(), actionReq.getActionMetadata(), i);
            actions.add(action);
        }
        zap.setActions(actions);
        
        // Save with all relationships
        zap = zapRepository.save(zap);
        
        logger.info("Zap created successfully with ID: {}", zap.getId());
        
        return Map.of(
            "zapId", zap.getId(),
            "message", "Zap created successfully"
        );
    }
    
    public Map<String, Object> getUserZaps(Integer userId) {
        logger.info("Fetching zaps for user: {}", userId);
        
        List<Zap> zaps = zapRepository.findByUserIdWithDetails(userId);
        
        List<Map<String, Object>> zapList = zaps.stream()
            .map(this::convertZapToMap)
            .toList();
        
        return Map.of("zaps", zapList);
    }
    
    public Map<String, Object> getZapById(Integer userId, String zapId) {
        logger.info("Fetching zap {} for user: {}", zapId, userId);
        
        Optional<Zap> zapOpt = zapRepository.findByIdAndUserIdWithDetails(zapId, userId);
        if (zapOpt.isEmpty()) {
            throw new RuntimeException("Zap not found");
        }
        
        return convertZapToMap(zapOpt.get());
    }
    
    private Map<String, Object> convertZapToMap(Zap zap) {
        Map<String, Object> zapMap = new HashMap<>();
        zapMap.put("id", zap.getId());
        zapMap.put("triggerId", zap.getTriggerId());
        
        if (zap.getTrigger() != null) {
            Map<String, Object> triggerMap = new HashMap<>();
            triggerMap.put("id", zap.getTrigger().getId());
            triggerMap.put("triggerId", zap.getTrigger().getTriggerId());
            triggerMap.put("metadata", zap.getTrigger().getMetadata());
            zapMap.put("trigger", triggerMap);
        }
        
        List<Map<String, Object>> actionsList = zap.getActions().stream()
            .map(action -> {
                Map<String, Object> actionMap = new HashMap<>();
                actionMap.put("id", action.getId());
                actionMap.put("actionId", action.getActionId());
                actionMap.put("metadata", action.getMetadata());
                actionMap.put("sortingOrder", action.getSortingOrder());
                return actionMap;
            })
            .toList();
        zapMap.put("actions", actionsList);
        
        return zapMap;
    }
}