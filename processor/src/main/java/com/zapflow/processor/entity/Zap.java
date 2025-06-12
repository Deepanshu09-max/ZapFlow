package com.zapflow.processor.entity;

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
    
    @OneToMany(mappedBy = "zap", fetch = FetchType.LAZY)
    private List<Action> actions;
    
    // Constructors
    public Zap() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTriggerId() { return triggerId; }
    public void setTriggerId(String triggerId) { this.triggerId = triggerId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public List<Action> getActions() { return actions; }
    public void setActions(List<Action> actions) { this.actions = actions; }
}
