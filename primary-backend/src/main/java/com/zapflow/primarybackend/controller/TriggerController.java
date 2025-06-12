package com.zapflow.primarybackend.controller;

import com.zapflow.primarybackend.entity.AvailableTrigger;
import com.zapflow.primarybackend.repository.AvailableTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trigger")
@CrossOrigin(origins = "*")
public class TriggerController {
    
    @Autowired
    private AvailableTriggerRepository availableTriggerRepository;
    
    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableTriggers() {
        List<AvailableTrigger> triggers = availableTriggerRepository.findAll();
        return ResponseEntity.ok(Map.of("availableTriggers", triggers));
    }
}
