package com.zapflow.primarybackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "\"Action\"")
public class Action {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    
    @Column(name = "zapId")
    private String zapId;
    
    @Column(name = "actionId")
    private String actionId;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;
    
    @Column(name = "sortingOrder")
    private Integer sortingOrder = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zapId", insertable = false, updatable = false)
    private Zap zap;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actionId", insertable = false, updatable = false)
    private AvailableAction type;
    
    // Constructors
    public Action() {}
    
    public Action(String zapId, String actionId, Map<String, Object> metadata, Integer sortingOrder) {
        this.zapId = zapId;
        this.actionId = actionId;
        this.metadata = metadata;
        this.sortingOrder = sortingOrder;
    }
    
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
    
    public AvailableAction getType() { return type; }
    public void setType(AvailableAction type) { this.type = type; }
}
