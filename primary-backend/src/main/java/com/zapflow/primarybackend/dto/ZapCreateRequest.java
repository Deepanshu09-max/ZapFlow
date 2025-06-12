package com.zapflow.primarybackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class ZapCreateRequest {
    
    @NotBlank(message = "Trigger ID is required")
    private String availableTriggerId;
    
    @NotNull(message = "Trigger metadata is required")
    private Map<String, Object> triggerMetadata;
    
    @NotEmpty(message = "At least one action is required")
    private List<ActionRequest> actions;
    
    // Constructors
    public ZapCreateRequest() {}
    
    public ZapCreateRequest(String availableTriggerId, Map<String, Object> triggerMetadata, List<ActionRequest> actions) {
        this.availableTriggerId = availableTriggerId;
        this.triggerMetadata = triggerMetadata;
        this.actions = actions;
    }
    
    // Getters and Setters
    public String getAvailableTriggerId() { return availableTriggerId; }
    public void setAvailableTriggerId(String availableTriggerId) { this.availableTriggerId = availableTriggerId; }
    
    public Map<String, Object> getTriggerMetadata() { return triggerMetadata; }
    public void setTriggerMetadata(Map<String, Object> triggerMetadata) { this.triggerMetadata = triggerMetadata; }
    
    public List<ActionRequest> getActions() { return actions; }
    public void setActions(List<ActionRequest> actions) { this.actions = actions; }
    
    public static class ActionRequest {
        @NotBlank(message = "Action ID is required")
        private String availableActionId;
        
        @NotNull(message = "Action metadata is required")
        private Map<String, Object> actionMetadata;
        
        // Constructors
        public ActionRequest() {}
        
        public ActionRequest(String availableActionId, Map<String, Object> actionMetadata) {
            this.availableActionId = availableActionId;
            this.actionMetadata = actionMetadata;
        }
        
        // Getters and Setters
        public String getAvailableActionId() { return availableActionId; }
        public void setAvailableActionId(String availableActionId) { this.availableActionId = availableActionId; }
        
        public Map<String, Object> getActionMetadata() { return actionMetadata; }
        public void setActionMetadata(Map<String, Object> actionMetadata) { this.actionMetadata = actionMetadata; }
    }
}