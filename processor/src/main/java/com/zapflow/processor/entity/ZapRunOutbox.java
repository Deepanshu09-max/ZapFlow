package com.zapflow.processor.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "\"ZapRunOutbox\"")
public class ZapRunOutbox {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    
    @Column(name = "zapRunId", unique = true)
    private String zapRunId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zapRunId", insertable = false, updatable = false)
    private ZapRun zapRun;
    
    // Constructors
    public ZapRunOutbox() {}
    
    public ZapRunOutbox(String zapRunId) {
        this.zapRunId = zapRunId;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getZapRunId() { return zapRunId; }
    public void setZapRunId(String zapRunId) { this.zapRunId = zapRunId; }
    
    public ZapRun getZapRun() { return zapRun; }
    public void setZapRun(ZapRun zapRun) { this.zapRun = zapRun; }
}
