package com.zapflow.primarybackend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "\"AvailableAction\"")
public class AvailableAction {
    @Id
    private String id;
    
    private String name;
    
    private String image;
    
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Action> actions;
    
    // Constructors
    public AvailableAction() {}
    
    public AvailableAction(String id, String name, String image) {
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
    
    public List<Action> getActions() { return actions; }
    public void setActions(List<Action> actions) { this.actions = actions; }
}
