package com.zapflow.primarybackend.controller;

import com.zapflow.primarybackend.entity.AvailableAction;
import com.zapflow.primarybackend.repository.AvailableActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/action")
@CrossOrigin(origins = "*")
public class ActionController {
    
    @Autowired
    private AvailableActionRepository availableActionRepository;
    
    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableActions() {
        List<AvailableAction> actions = availableActionRepository.findAll();
        return ResponseEntity.ok(Map.of("availableActions", actions));
    }
}
