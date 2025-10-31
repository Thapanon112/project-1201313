package com.chickenshop.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private String customerPhone;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemDTO> items;
    
    // Constructors
    public OrderResponseDTO() {}
    
    public OrderResponseDTO(Long id, String customerName, String customerPhone, 
                        Double totalAmount, LocalDateTime orderDate, String status, 
                        List<OrderItemDTO> items) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.items = items;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}