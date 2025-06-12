package com.zapflow.processor.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "\"Action\"")
public class Action {
    @Id
    private String id;
    
    @Column(name = "zapId")
    private String zapId;
    
    @Column(name = "actionId")
    private String actionId;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;
    
    @Column(name = "sortingOrder")
    private Integer sortingOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zapId", insertable = false, updatable = false)
    private Zap zap;
    
    // Constructors
    public Action() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getZapId() { return zapId; }
    public void setZapId(String zapId) { this.zapId = zapId; }
    
    public String getActionId() { return actionId; }
    public void setActionId(String actionId) { this.actionId = actionId; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public Integer getSortingOrder() { return sortingOrder; }
    public void setSortingOrder(Integer sortingOrder) { this.sortingOrder = sortingOrder; }
    
    public Zap getZap() { return zap; }
    public void setZap(Zap zap) { this.zap = zap; }
}
