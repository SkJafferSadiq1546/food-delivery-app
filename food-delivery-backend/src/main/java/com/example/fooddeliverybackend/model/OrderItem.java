package com.example.fooddeliverybackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // ✅ Add this import
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private int quantity;
    
    @Column(name = "price_per_item")
    private BigDecimal pricePerItem;
    
    // ✅ NEW: Add this relationship back to the parent Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference // This prevents infinite loops when sending JSON
    private Order order;
}