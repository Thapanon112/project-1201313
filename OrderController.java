package com.chickenshop.controller;

import com.chickenshop.entity.Chicken;
import com.chickenshop.entity.Order;
import com.chickenshop.entity.OrderItem;
import com.chickenshop.repository.ChickenRepository;
import com.chickenshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ChickenRepository chickenRepository;
    
    @GetMapping
    public List<Order> getAllOrders() {
        System.out.println("กำลังดึงข้อมูลคำสั่งซื้อทั้งหมด...");
        List<Order> orders = orderRepository.findAll();
        System.out.println("พบคำสั่งซื้อ: " + orders.size() + " รายการ");
        return orders;
    }
    
    @PostMapping
    public Order createOrder(@RequestBody Map<String, Object> orderData) {
        System.out.println("ได้รับคำสั่งซื้อใหม่: " + orderData);
        
        try {
            Order order = new Order();
            order.setCustomerName((String) orderData.get("customerName"));
            order.setCustomerPhone((String) orderData.get("customerPhone"));
            
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) orderData.get("items");
            double totalAmount = 0;
            
            System.out.println("รายการสินค้า: " + itemsData.size() + " รายการ");
            
            for (Map<String, Object> itemData : itemsData) {
                Long chickenId = Long.valueOf(itemData.get("chickenId").toString());
                Integer quantity = Integer.valueOf(itemData.get("quantity").toString());
                
                System.out.println("สินค้า ID: " + chickenId + ", จำนวน: " + quantity);
                
                Optional<Chicken> chickenOpt = chickenRepository.findById(chickenId);
                if (chickenOpt.isPresent()) {
                    Chicken chicken = chickenOpt.get();
                    System.out.println("พบสินค้า: " + chicken.getName() + ", สต็อกเดิม: " + chicken.getStock());
                    
                    if (chicken.getStock() >= quantity) {
                        double itemTotal = chicken.getPrice() * quantity;
                        totalAmount += itemTotal;
                        
                        OrderItem orderItem = new OrderItem(order, chicken, quantity, chicken.getPrice());
                        order.getItems().add(orderItem);
                        
                        // อัพเดตสต็อก
                        int newStock = chicken.getStock() - quantity;
                        chicken.setStock(newStock);
                        chickenRepository.save(chicken);
                        
                        System.out.println("อัพเดตสต็อก " + chicken.getName() + " เป็น: " + newStock);
                    } else {
                        System.out.println("สต็อกไม่พอสำหรับ: " + chicken.getName());
                    }
                } else {
                    System.out.println("ไม่พบสินค้า ID: " + chickenId);
                }
            }
            
            order.setTotalAmount(totalAmount);
            Order savedOrder = orderRepository.save(order);
            
            System.out.println("บันทึกคำสั่งซื้อสำเร็จ ID: " + savedOrder.getId());
            return savedOrder;
            
        } catch (Exception e) {
            System.out.println("เกิดข้อผิดพลาด: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}