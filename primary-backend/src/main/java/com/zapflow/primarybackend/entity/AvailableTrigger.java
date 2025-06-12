package com.zapflow.primarybackend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "\"AvailableTrigger\"")
public class AvailableTrigger {
    @Id
    private String id;
    
    private String name;
    
    private String image;
    
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Trigger> triggers;
    
    // Constructors
    public AvailableTrigger() {}
    
    public AvailableTrigger(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public List<Trigger> getTriggers() { return triggers; }
    public void setTriggers(List<Trigger> triggers) { this.triggers = triggers; }
}
