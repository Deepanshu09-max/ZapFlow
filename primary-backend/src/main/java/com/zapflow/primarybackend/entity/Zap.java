package com.zapflow.primarybackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "\"Zap\"")
public class Zap {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    
    @Column(name = "triggerId")
    private String triggerId;
    
    @Column(name = "userId")
    private Integer userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    
    @OneToOne(mappedBy = "zap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Trigger trigger;
    
    @OneToMany(mappedBy = "zap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sortingOrder ASC")
    private List<Action> actions;
    
    @OneToMany(mappedBy = "zap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ZapRun> zapRuns;
    
    // Constructors
    public Zap() {}
    
    public Zap(Integer userId, String triggerId) {
        this.userId = userId;
        this.triggerId = triggerId;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTriggerId() { return triggerId; }
    public void setTriggerId(String triggerId) { this.triggerId = triggerId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Trigger getTrigger() { return trigger; }
    public void setTrigger(Trigger trigger) { this.trigger = trigger; }
    
    public List<Action> getActions() { return actions; }
    public void setActions(List<Action> actions) { this.actions = actions; }
    
    public List<ZapRun> getZapRuns() { return zapRuns; }
    public void setZapRuns(List<ZapRun> zapRuns) { this.zapRuns = zapRuns; }
}
