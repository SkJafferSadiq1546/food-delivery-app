package com.example.fooddeliverybackend.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    private String status;
    private String deliveryAddress;
    private String paymentMethod;

    @Column(name = "estimated_delivery_time")
    private String estimatedDeliveryTime;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference // This prevents infinite loops when sending JSON
    private List<OrderItem> items;
    
}