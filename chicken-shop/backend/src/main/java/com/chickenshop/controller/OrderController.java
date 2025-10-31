package com.chickenshop.controller;

import com.chickenshop.dto.OrderResponseDTO;
import com.chickenshop.dto.OrderItemDTO;
import com.chickenshop.entity.Chicken;
import com.chickenshop.entity.Order;
import com.chickenshop.entity.OrderItem;
import com.chickenshop.repository.ChickenRepository;
import com.chickenshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ChickenRepository chickenRepository;
    
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            System.out.println("📦 กำลังดึงข้อมูลคำสั่งซื้อทั้งหมด...");
            List<Order> orders = orderRepository.findAll();
            System.out.println("✅ พบคำสั่งซื้อ " + orders.size() + " รายการ");
            
            List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            System.err.println("❌ ข้อผิดพลาดในการดึงคำสั่งซื้อ: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error getting orders: " + e.getMessage());
        }
    }
    
    @PostMapping
    @Transactional
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderData) {
        System.out.println("🎯 ==== รับคำสั่งซื้อใหม่ ====");
        System.out.println("📩 ข้อมูลที่ได้รับ: " + orderData);
        
        try {
            // ตรวจสอบข้อมูลพื้นฐาน
            if (orderData.get("customerName") == null || orderData.get("customerPhone") == null) {
                return ResponseEntity.badRequest().body("กรุณากรอกชื่อและเบอร์โทรศัพท์");
            }
            
            if (orderData.get("items") == null) {
                return ResponseEntity.badRequest().body("ไม่มีรายการสินค้า");
            }
            
            Order order = new Order();
            order.setCustomerName(orderData.get("customerName").toString());
            order.setCustomerPhone(orderData.get("customerPhone").toString());
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) orderData.get("items");
            
            if (itemsData.isEmpty()) {
                return ResponseEntity.badRequest().body("รายการสินค้าว่าง");
            }
            
            System.out.println("📦 จำนวนรายการสินค้า: " + itemsData.size());
            
            double totalAmount = 0;
            
            for (Map<String, Object> itemData : itemsData) {
                // ตรวจสอบ field ที่จำเป็น
                if (itemData.get("chickenId") == null || itemData.get("quantity") == null) {
                    return ResponseEntity.badRequest().body("ข้อมูลสินค้าไม่ครบถ้วน");
                }
                
                Long chickenId;
                Integer quantity;
                
                try {
                    chickenId = Long.valueOf(itemData.get("chickenId").toString());
                    quantity = Integer.valueOf(itemData.get("quantity").toString());
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("รูปแบบข้อมูลสินค้าไม่ถูกต้อง");
                }
                
                System.out.println("🔍 ค้นหาสินค้า ID: " + chickenId + ", จำนวน: " + quantity);
                
                Optional<Chicken> chickenOpt = chickenRepository.findById(chickenId);
                if (!chickenOpt.isPresent()) {
                    return ResponseEntity.badRequest().body("ไม่พบสินค้า ID: " + chickenId);
                }
                
                Chicken chicken = chickenOpt.get();
                System.out.println("✅ พบสินค้า: " + chicken.getName() + ", สต็อก: " + chicken.getStock());
                
                // ตรวจสอบสต็อก
                if (chicken.getStock() == null || chicken.getStock() < quantity) {
                    return ResponseEntity.badRequest().body("สต็อกไม่พอสำหรับ: " + chicken.getName() + " (มี " + chicken.getStock() + " ตัว)");
                }
                
                // คำนวณราคา
                double itemTotal = chicken.getPrice() * quantity;
                totalAmount += itemTotal;
                
                // สร้าง OrderItem
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setChicken(chicken);
                orderItem.setQuantity(quantity);
                orderItem.setPrice(chicken.getPrice());
                
                order.getItems().add(orderItem);
                
                // อัพเดตสต็อก
                chicken.setStock(chicken.getStock() - quantity);
                chickenRepository.save(chicken);
                
                System.out.println("📊 อัพเดตสต็อก " + chicken.getName() + " เหลือ: " + chicken.getStock());
            }
            
            order.setTotalAmount(totalAmount);
            Order savedOrder = orderRepository.save(order);
            
            System.out.println("✅ บันทึกคำสั่งซื้อสำเร็จ ID: " + savedOrder.getId());
            System.out.println("💰 ยอดรวม: " + totalAmount + " บาท");
            
            return ResponseEntity.ok(convertToDTO(savedOrder));
            
        } catch (Exception e) {
            System.err.println("❌ เกิดข้อผิดพลาด: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาดในการสร้างคำสั่งซื้อ: " + e.getMessage());
        }
    }
    
    private OrderResponseDTO convertToDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
            .map(item -> {
                String chickenName = item.getChicken() != null ? item.getChicken().getName() : "ไม่ทราบชื่อ";
                return new OrderItemDTO(
                    item.getId(),
                    chickenName,
                    item.getQuantity(),
                    item.getPrice()
                );
            })
            .collect(Collectors.toList());
        
        return new OrderResponseDTO(
            order.getId(),
            order.getCustomerName(),
            order.getCustomerPhone(),
            order.getTotalAmount(),
            order.getOrderDate(),
            order.getStatus(),
            itemDTOs
        );
    }
}