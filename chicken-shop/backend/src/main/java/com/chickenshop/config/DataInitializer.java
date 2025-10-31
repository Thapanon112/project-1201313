package com.chickenshop.config;

import com.chickenshop.entity.Chicken;
import com.chickenshop.repository.ChickenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ChickenRepository chickenRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // ตรวจสอบว่ามีข้อมูลอยู่แล้วหรือไม่
        if (chickenRepository.count() == 0) {
            System.out.println("🔄 กำลังเพิ่มข้อมูลตัวอย่างลงใน MySQL...");
            
            chickenRepository.save(new Chicken("ไก่เนื้อสด", "ไก่เนื้อ", 100.0, 
                "ไก่เนื้อสดคุณภาพดี เนื้อแน่น", "1.jpg", 50));
            
            chickenRepository.save(new Chicken("ไก่บ้านพื้นเมือง", "ไก่บ้าน", 110.0, 
                "ไก่บ้านเลี้ยงอิสระ เนื้อแน่นรสชาติดี", "kaiban.jpg", 30));
            
            chickenRepository.save(new Chicken("ไก่เบญจาพันธุ์แท้", "ไก่เบญจา", 150.0, 
                "ไก่เบญจาพันธุ์แท้ เนื้ออร่อย", "benja.jpg", 20));
            
            chickenRepository.save(new Chicken("ไก่ดำพรีเมี่ยม", "ไก่เนื้อ", 200.0, 
                "ไก่ดำเกรดพรีเมี่ยม", "dum.jpg", 15));
            
            System.out.println("✅ ข้อมูลตัวอย่างถูกเพิ่มลงใน MySQL แล้ว");
        } else {
            System.out.println("ℹ️  พบข้อมูลใน MySQL แล้ว (" + chickenRepository.count() + " รายการ)");
        }
        
        System.out.println("🔗 Admin: http://localhost:8080/admin");
        System.out.println("🔗 API: http://localhost:8080/api/chickens");
        System.out.println("🔗 Frontend: เปิดไฟล์ frontend/index.html ใน browser");
    }
}