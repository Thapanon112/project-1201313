package com.chickenshop.controller;

import com.chickenshop.entity.Chicken;
import com.chickenshop.repository.ChickenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chickens")
@CrossOrigin(origins = "*")
public class ChickenController {
    
    @Autowired
    private ChickenRepository chickenRepository;
    
    @GetMapping
    public List<Chicken> getAllChickens() {
        return chickenRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Chicken getChickenById(@PathVariable Long id) {
        return chickenRepository.findById(id).orElse(null);
    }
    
    @GetMapping("/type/{type}")
    public List<Chicken> getChickensByType(@PathVariable String type) {
        return chickenRepository.findByType(type);
    }
}