package com.zapflow.hooks.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "\"Zap\"")
public class Zap {
    @Id
    private String id;
    
    @Column(name = "triggerId")
    private String triggerId;
    
    @Column(name = "userId")
    private Integer userId;
    
    @OneToMany(mappedBy = "zap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ZapRun> zapRuns;
    
    // Constructors
    public Zap() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTriggerId() { return triggerId; }
    public void setTriggerId(String triggerId) { this.triggerId = triggerId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public List<ZapRun> getZapRuns() { return zapRuns; }
    public void setZapRuns(List<ZapRun> zapRuns) { this.zapRuns = zapRuns; }
}
