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
        System.out.println("กำลังดึงข้อมูลสินค้าทั้งหมด...");
        List<Chicken> chickens = chickenRepository.findAll();
        System.out.println("พบสินค้า: " + chickens.size() + " รายการ");
        return chickens;
    }
    
    @GetMapping("/{id}")
    public Chicken getChickenById(@PathVariable Long id) {
        return chickenRepository.findById(id).orElse(null);
    }
    
    @GetMapping("/type/{type}")
    public List<Chicken> getChickensByType(@PathVariable String type) {
        return chickenRepository.findByType(type);
    }
    
    // API สำหรับรีเซ็ตสต็อก (สำหรับทดสอบ)
    @PostMapping("/reset-stock")
    public String resetStock() {
        List<Chicken> chickens = chickenRepository.findAll();
        for (Chicken chicken : chickens) {
            // รีเซ็ตสต็อกเป็นค่าเริ่มต้น
            if (chicken.getName().contains("ไก่เนื้อสด")) chicken.setStock(50);
            else if (chicken.getName().contains("ไก่บ้าน")) chicken.setStock(30);
            else if (chicken.getName().contains("ไก่เบญจา")) chicken.setStock(20);
            else if (chicken.getName().contains("พรีเมี่ยม")) chicken.setStock(40);
            chickenRepository.save(chicken);
        }
        return "รีเซ็ตสต็อกเรียบร้อย";
    }
}