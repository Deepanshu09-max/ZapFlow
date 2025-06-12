package com.zapflow.worker.dto;

import java.util.List;
import java.util.Map;

public class ZapRunMessage {
    private String zapRunId;
    private String zapId;
    private Integer userId;
    private Map<String, Object> metadata;
    private List<ActionDto> actions;
    
    // Constructors
    public ZapRunMessage() {}
    
    public ZapRunMessage(String zapRunId, String zapId, Integer userId, Map<String, Object> metadata, List<ActionDto> actions) {
        this.zapRunId = zapRunId;
        this.zapId = zapId;
        this.userId = userId;
        this.metadata = metadata;
        this.actions = actions;
    }
    
    // Getters and Setters
    public String getZapRunId() { return zapRunId; }
    public void setZapRunId(String zapRunId) { this.zapRunId = zapRunId; }
    
    public String getZapId() { return zapId; }
    public void setZapId(String zapId) { this.zapId = zapId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public List<ActionDto> getActions() { return actions; }
    public void setActions(List<ActionDto> actions) { this.actions = actions; }
    
    public static class ActionDto {
        private String actionId;
        private Map<String, Object> metadata;
        private Integer sortingOrder;
        
        public ActionDto() {}
        
        public ActionDto(String actionId, Map<String, Object> metadata, Integer sortingOrder) {
            this.actionId = actionId;
            this.metadata = metadata;
            this.sortingOrder = sortingOrder;
        }
        
        // Getters and Setters
        public String getActionId() { return actionId; }
        public void setActionId(String actionId) { this.actionId = actionId; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        
        public Integer getSortingOrder() { return sortingOrder; }
        public void setSortingOrder(Integer sortingOrder) { this.sortingOrder = sortingOrder; }
    }
}