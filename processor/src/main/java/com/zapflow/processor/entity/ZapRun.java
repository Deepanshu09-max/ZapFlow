package com.zapflow.processor.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "\"ZapRun\"")
public class ZapRun {
    @Id
    private String id;
    
    @Column(name = "zapId")
    private String zapId;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zapId", insertable = false, updatable = false)
    private Zap zap;
    
    // Constructors
    public ZapRun() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getZapId() { return zapId; }
    public void setZapId(String zapId) { this.zapId = zapId; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public Zap getZap() { return zap; }
    public void setZap(Zap zap) { this.zap = zap; }
}
