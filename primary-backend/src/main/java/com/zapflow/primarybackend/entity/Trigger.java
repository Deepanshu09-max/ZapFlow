package com.zapflow.primarybackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "\"Trigger\"")
public class Trigger {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    
    @Column(name = "zapId", unique = true)
    private String zapId;
    
    @Column(name = "triggerId")
    private String triggerId;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zapId", insertable = false, updatable = false)
    private Zap zap;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "triggerId", insertable = false, updatable = false)
    private AvailableTrigger type;
    
    // Constructors
    public Trigger() {}
    
    public Trigger(String zapId, String triggerId, Map<String, Object> metadata) {
        this.zapId = zapId;
        this.triggerId = triggerId;
        this.metadata = metadata;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getZapId() { return zapId; }
    public void setZapId(String zapId) { this.zapId = zapId; }
    
    public String getTriggerId() { return triggerId; }
    public void setTriggerId(String triggerId) { this.triggerId = triggerId; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public Zap getZap() { return zap; }
    public void setZap(Zap zap) { this.zap = zap; }
    
    public AvailableTrigger getType() { return type; }
    public void setType(AvailableTrigger type) { this.type = type; }
}
