package com.chickenshop.dto;

public class OrderItemDTO {
    private Long id;
    private String chickenName;
    private Integer quantity;
    private Double price;
    
    // Constructors
    public OrderItemDTO() {}
    
    public OrderItemDTO(Long id, String chickenName, Integer quantity, Double price) {
        this.id = id;
        this.chickenName = chickenName;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getChickenName() { return chickenName; }
    public void setChickenName(String chickenName) { this.chickenName = chickenName; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}