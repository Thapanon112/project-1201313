package com.chickenshop.controller;

import com.chickenshop.entity.Chicken;
import com.chickenshop.entity.Order;
import com.chickenshop.repository.ChickenRepository;
import com.chickenshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ChickenRepository chickenRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    // แดชบอร์ด
    @GetMapping
    public String dashboard(Model model) {
        try {
            long totalProducts = chickenRepository.count();
            long totalOrders = orderRepository.count();
            
            // คำนวณรายได้รวม
            Double totalRevenue = orderRepository.findAll().stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
            
            List<Order> recentOrders = orderRepository.findAll();
            
            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
            model.addAttribute("recentOrders", recentOrders);
            
            System.out.println("📊 Dashboard - Products: " + totalProducts + ", Orders: " + totalOrders);
            
        } catch (Exception e) {
            System.err.println("❌ Error in dashboard: " + e.getMessage());
            model.addAttribute("totalProducts", 0);
            model.addAttribute("totalOrders", 0);
            model.addAttribute("totalRevenue", 0.0);
            model.addAttribute("recentOrders", List.of());
        }
        
        return "dashboard";
    }

    // จัดการสินค้า
    @GetMapping("/products")
    public String products(Model model) {
        try {
            List<Chicken> chickens = chickenRepository.findAll();
            model.addAttribute("chickens", chickens);
            System.out.println("📦 Loaded " + chickens.size() + " products");
        } catch (Exception e) {
            System.err.println("❌ Error loading products: " + e.getMessage());
            model.addAttribute("chickens", List.of());
        }
        return "admin";
    }

    // เพิ่มสินค้า
    @PostMapping("/products")
    public String addProduct(@RequestParam String name,
                           @RequestParam String type,
                           @RequestParam Double price,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) String imageUrl,
                           @RequestParam Integer stock) {
        try {
            Chicken chicken = new Chicken(name, type, price, description, imageUrl, stock);
            chickenRepository.save(chicken);
            System.out.println("✅ Added product: " + name);
            return "redirect:/admin/products?success=เพิ่มสินค้า '" + name + "' เรียบร้อยแล้ว";
        } catch (Exception e) {
            System.err.println("❌ Error adding product: " + e.getMessage());
            return "redirect:/admin/products?error=ไม่สามารถเพิ่มสินค้าได้: " + e.getMessage();
        }
    }

    // อัพเดตสินค้า
    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String type,
                              @RequestParam Double price,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) String imageUrl,
                              @RequestParam Integer stock) {
        try {
            Chicken chicken = chickenRepository.findById(id).orElse(null);
            if (chicken != null) {
                chicken.setName(name);
                chicken.setType(type);
                chicken.setPrice(price);
                chicken.setDescription(description);
                chicken.setImageUrl(imageUrl);
                chicken.setStock(stock);
                chickenRepository.save(chicken);
                System.out.println("✅ Updated product: " + name);
            }
            return "redirect:/admin/products?success=อัพเดตสินค้า '" + name + "' เรียบร้อยแล้ว";
        } catch (Exception e) {
            System.err.println("❌ Error updating product: " + e.getMessage());
            return "redirect:/admin/products?error=ไม่สามารถอัพเดตสินค้าได้: " + e.getMessage();
        }
    }

    // ลบสินค้า
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            chickenRepository.deleteById(id);
            System.out.println("✅ Deleted product ID: " + id);
            return "redirect:/admin/products?success=ลบสินค้าเรียบร้อยแล้ว";
        } catch (Exception e) {
            System.err.println("❌ Error deleting product: " + e.getMessage());
            return "redirect:/admin/products?error=ไม่สามารถลบสินค้าได้: " + e.getMessage();
        }
    }

    // จัดการคำสั่งซื้อ
    @GetMapping("/orders")
    public String orders(Model model) {
        try {
            List<Order> orders = orderRepository.findAll();
            model.addAttribute("orders", orders);
            System.out.println("📋 Loaded " + orders.size() + " orders");
        } catch (Exception e) {
            System.err.println("❌ Error loading orders: " + e.getMessage());
            model.addAttribute("orders", List.of());
        }
        return "orders";
    }

    // อัพเดตสถานะคำสั่งซื้อ
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Order order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                order.setStatus(status);
                orderRepository.save(order);
                System.out.println("✅ Updated order " + id + " status to: " + status);
            }
            return "redirect:/admin/orders?success=อัพเดตสถานะคำสั่งซื้อเรียบร้อยแล้ว";
        } catch (Exception e) {
            System.err.println("❌ Error updating order status: " + e.getMessage());
            return "redirect:/admin/orders?error=ไม่สามารถอัพเดตสถานะได้: " + e.getMessage();
        }
    }

    // API สินค้า
    @GetMapping("/api/chickens")
    @ResponseBody
    public List<Chicken> getChickensApi() {
        return chickenRepository.findAll();
    }
}